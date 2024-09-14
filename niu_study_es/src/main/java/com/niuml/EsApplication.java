package com.niuml;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.niuml.es.entity.ESTestEntity;
import com.niuml.es.repository.ESTestRepository;
import com.niuml.es.repository.ESUserInfoRepository;
import com.niuml.mysql.entity.UserInfo;
import com.niuml.mysql.mapper.UserInfoMapper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQuery;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @author niumengliang
 * Date:2024/7/25
 * Time:10:20
 */
@RestController
@SpringBootApplication
@MapperScan("com.niuml.mysql.mapper")
public class EsApplication {
    public static void main(String[] args) {
        SpringApplication.run(EsApplication.class, args);
    }


    @Autowired
    ESTestRepository esTestRepository;

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ElasticsearchClient client;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private ESUserInfoRepository esUserInfoRepository;

    //    @GetMapping("c/list")
//    private List<ESTestEntity> tt() throws Exception{
//        SearchResponse<ESTestEntity> test = client.search(s -> s.index(Arrays.asList("test"))
//                .query(q -> q.matchAll(m -> m)), ESTestEntity.class);
//
//    }
    @GetMapping("getUserAll/{id}")
    public Object getUserAll(@PathVariable("id") int id) throws InterruptedException {
        String str = "27,94,95,96,97,98,99,100,101,102,103,104,105,106,107,108,109,110,111,112,113";
        String[] split = str.split(",");
        for (String s : split) {
            List<UserInfo> list = userInfoMapper.findByOrgId(Integer.parseInt(s));
            esUserInfoRepository.saveAll(list);
            Thread.sleep(3*1000);
        }
//        List<UserInfo> list = userInfoMapper.findByOrgId(id);
//        esUserInfoRepository.saveAll(list);
        return null;
    }

    @GetMapping("getUserAllList")
    public Object userInfoList(@RequestBody UserInfo map) {
//        System.out.println("map = " + map);
        //这个排序字段需要是int long这样的类型  不然会有异常
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        FieldSortBuilder fsb = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        Criteria criteria = new Criteria();
        if (Objects.nonNull(map.getRealName())) {
            criteria.and(new Criteria("realName").is(map.getRealName()));
        }
        if (Objects.nonNull(map.getEnforceNum())) {
            criteria.and(new Criteria("enforceNum").is(map.getEnforceNum()));
        }
        if (Objects.nonNull(map.getOrgId())) {
            criteria.and(new Criteria("orgId").is(map.getOrgId()));
        }
        if (Objects.nonNull(map.getCreateTime())) {//小于等于map.getCreateTime()
            criteria.and(new Criteria("createTime").lessThanEqual(map.getCreateTime()));
        }


        Query query = new CriteriaQueryBuilder(criteria)
                .withPageable(PageRequest.of(0, 2))
                // 不需要查询的字段
                .build();
        SearchHits<UserInfo> search = elasticsearchTemplate.search(query, UserInfo.class);
        List<UserInfo> collect = search.getSearchHits().stream().map(a -> {
            UserInfo content = a.getContent();
            UserInfo re = new UserInfo();
            BeanUtils.copyProperties(content, re);
            return re;
        }).collect(Collectors.toList());
        return collect;
    }


    //    @Autowired
//    ElasticsearchRestTemplate elasticsearchRestTemplate;
    @GetMapping("save/{id}")
    void save(@PathVariable("id") Long id) {
        ESTestEntity esTestEntity = new ESTestEntity();
        esTestEntity.setId(id);
        esTestEntity.setContent("不安全连接");
        esTestEntity.setTitle("world");
        esTestEntity.setExcerpt("test");
        System.out.println(esTestRepository.save(esTestEntity));
    }

    @GetMapping("saveET")
    void save2(ESTestEntity esTestEntity) {
        System.out.println(esTestRepository.save(esTestEntity));
    }

    @GetMapping("get/{id}")
    ESTestEntity get(@PathVariable("id") String id) {
        Optional<ESTestEntity> byId = esTestRepository.findById(id);
        System.out.println(byId.get());
        return byId.get();
    }

    @GetMapping("del/{id}")
    String del(@PathVariable("id") String id) {
        esTestRepository.deleteById(id);
        return "ok";
    }

    @GetMapping("save")
    void insert(@RequestBody ESTestEntity esTestEntity) {
        System.out.println(esTestRepository.save(esTestEntity));
    }

    //分页页码默认是从0开始的
    @GetMapping("list/{page}/{size}")
    Map getListByPage(@PathVariable("page") int page, @PathVariable("size") int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Page<ESTestEntity> all = esTestRepository.findAll(PageRequest.of(page, size, sort));
        List<ESTestEntity> content = all.getContent();
        int te = (int) all.getTotalElements();
        int totalPages = all.getTotalPages();
        return new HashMap<>() {{
            put("list", content);
            put("te", te);
            put("totalPages", totalPages);
        }};
    }

    @GetMapping("list/")
    List<ESTestEntity> list(ESTestEntity esTestEntity) {
        System.out.println("esTestEntity = " + esTestEntity);
        //这个排序字段需要是int long这样的类型  不然会有异常
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
//        FieldSortBuilder fsb = SortBuilders.fieldSort("id").order(SortOrder.DESC);
        Criteria criteria = new Criteria();
        if (Objects.nonNull(esTestEntity.getContent()))
            criteria.and(new Criteria("content").is(esTestEntity.getContent()));
        if (Objects.nonNull(esTestEntity.getTitle()))
            criteria.and(new Criteria("title").is(esTestEntity.getTitle()));


        Query query = new CriteriaQueryBuilder(criteria)
                .withPageable(PageRequest.of(0, 2, sort))
                // 不需要查询的字段
                .build();
        SearchHits<ESTestEntity> search = elasticsearchTemplate.search(query, ESTestEntity.class);
        List<ESTestEntity> collect = search.getSearchHits().stream().map(a -> {
            ESTestEntity content = a.getContent();
            ESTestEntity re = new ESTestEntity();
            BeanUtils.copyProperties(content, re);
            return re;
        }).collect(Collectors.toList());
        return collect;
    }
}

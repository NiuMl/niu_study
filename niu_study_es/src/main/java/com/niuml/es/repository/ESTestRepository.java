package com.niuml.es.repository;

import com.niuml.es.entity.ESTestEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/***
 * User:niumengliang
 * Date:2024/7/25
 * Time:10:23
 */
public interface ESTestRepository extends ElasticsearchRepository<ESTestEntity, String> {
    List<ESTestEntity> findAllByContentLike(String content, Sort sort);
}

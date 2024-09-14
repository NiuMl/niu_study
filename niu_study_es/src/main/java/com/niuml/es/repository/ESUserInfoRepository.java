package com.niuml.es.repository;

import com.niuml.es.entity.ESTestEntity;
import com.niuml.mysql.entity.UserInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/***
 * User:niumengliang
 * Date:2024/7/25
 * Time:10:23
 */
public interface ESUserInfoRepository extends ElasticsearchRepository<UserInfo, Long> {
}

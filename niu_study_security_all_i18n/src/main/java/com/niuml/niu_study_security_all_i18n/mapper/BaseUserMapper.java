package com.niuml.niu_study_security_all_i18n.mapper;

import com.niuml.niu_study_security_all_i18n.entity.BaseUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.binding.jpa.JpaTable;

/**
* @author niumengliang
* @description 针对表【ef_base_user(用户基础信息)】的数据库操作Mapper
* @createDate 2024-12-11 17:00:13
* @Entity generator.domain.EfBaseUser
*/
@JpaTable("base_user")
public interface BaseUserMapper {

    BaseUser findById(@Param("id") Integer baseUserId);
}





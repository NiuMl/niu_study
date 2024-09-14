package com.niuml.niu_study_sharding_sphere.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

/***
 * @author niumengliang
 * Date:2024/7/31
 * Time:10:41
 */
@Data
@Entity
public class VxCharge {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /**
     * 收费项目名称
     */
    private String name;
    /**
     * 费用 整形
     */
    private Integer money;
    /**
     * 1可用  2弃用，在维修详情那不显示2的
     */
    private Integer status;
}

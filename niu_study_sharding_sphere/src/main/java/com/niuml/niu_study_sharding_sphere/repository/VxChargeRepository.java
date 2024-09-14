package com.niuml.niu_study_sharding_sphere.repository;

import com.niuml.niu_study_sharding_sphere.entity.VxCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/***
 * @author niumengliang
 * Date:2024/7/31
 * Time:14:22
 */
@Repository
public interface VxChargeRepository extends JpaRepository<VxCharge,Integer> {
    VxCharge queryById(Integer id);
}

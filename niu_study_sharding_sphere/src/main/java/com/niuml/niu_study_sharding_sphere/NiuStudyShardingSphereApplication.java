package com.niuml.niu_study_sharding_sphere;

import com.niuml.niu_study_sharding_sphere.entity.VxCharge;
import com.niuml.niu_study_sharding_sphere.repository.VxChargeRepository;
import jakarta.annotation.Resource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
@SpringBootApplication
public class NiuStudyShardingSphereApplication {

    public static void main(String[] args) {
        SpringApplication.run(NiuStudyShardingSphereApplication.class, args);
    }

    @Resource
    private VxChargeRepository vxChargeRepository;

    @GetMapping("test")
    public void add(){
        VxCharge vx = new VxCharge();
        vx.setName(UUID.randomUUID().toString());
        vx.setMoney(new Random().nextInt(10));
        vx.setStatus(1);
        vxChargeRepository.save(vx);
    }
    @GetMapping("test/{id}")
    public VxCharge search(@PathVariable("id") Integer id){
        return vxChargeRepository.queryById(id);
        //findById有事务 有事务就会走主库
//        return vxChargeRepository.findById(id).orElseThrow(()->new RuntimeException("略略略略"));
    }
}

package com.niuml.niu_study_sb_mqtt;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/***
 * @author niumengliang
 * Date:2024/12/27
 * Time:12:45
 */
@Log4j2
@Controller
public class TestController {
    @Resource
    private MqttConsumer client;

    @Value("${mqtt.clientId}")
    private String clientId;


    @GetMapping("publish")
    @ResponseBody
    public String publish(String topic, String message) {
        client.subscribe(topic, message);
        return "ok";
    }

    @RequestMapping("connect")
    @ResponseBody
    public String connect() {
        client.connect();
        return clientId + "连接到服务器";
    }

//    @RequestMapping("againSubscribe")
//    @ResponseBody
//    public String againSubscribe() {
//        try {
//            client.againSubscribe();
//            return "ok";
//        } catch (Exception e) {
//            log.error("重新订阅失败", e);
//        }
//        return "error";
//    }

    @RequestMapping("disConnect")
    @ResponseBody
    public String disConnect() {
        client.disConnect();
        return clientId + "与服务器断开连接";
    }

}

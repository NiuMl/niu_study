package com.niuml.niu_study_sb_mqtt;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/***
 * @author niumengliang
 * Date:2024/12/27
 * Time:11:11
 */
@Log4j2
@Configuration
public class MqttConsumer {

    @Value("${mqtt.username}")
    private String username;
    @Value("${mqtt.password}")
    private String password;
    @Value("${mqtt.hostUrl}")
    private String hostUrl;
    @Value("${mqtt.clientId}")
    private String clientId;
    @Value("${mqtt.topic}")
    private String[] topic;
    @Value("${mqtt.qos}")
    private int[] qos;

    /**
     * 客户端对象
     */
    @Resource
    private MqttClient client;


    @Resource
    MqttConsumerCallBack mqttConsumerCallBack;

    @Resource
    MqttConnectOptions options;

    /**
     * 在bean初始化后连接到服务器
     */
    @PostConstruct
    public void init() {
        connect();
    }

    /**
     * 客户端连接服务端
     */
    public void connect() {
        log.info("开始创建创建MQTT客户端对象，客户端连接到服务器");
        try {
            //创建MQTT客户端对象
//            client = new MqttClient(hostUrl, clientId, new MemoryPersistence());
            //连接设置
            //设置遗嘱消息的话题，若客户端和服务器之间的连接意外断开，服务器将发布客户端的遗嘱信息
            options.setWill("willTopic", (clientId + "与服务器断开连接").getBytes(), 0, false);
            //设置回调
            client.setCallback(mqttConsumerCallBack);
            client.connect(options);
            //订阅主题 默认的也是固定的
            client.subscribe(topic,qos);
        } catch (Exception e) {
            log.error("创建MQTT客户端对象失败，客户端连接到服务器失败", e);
        }
    }




    /**
     * 断开连接
     */
    public void disConnect() {
        try {
            client.disconnect();
        } catch (MqttException e) {
            log.info("断开连接失败：{}",e.getMessage());
        }
    }



    public void subscribe(String topic, int qos, IMqttMessageListener listener) {
        try {
            client.subscribe(topic, qos, listener);
        } catch (MqttException e) {
            log.error("订阅主题失败：{}",e.getMessage());
        }
    }
    public void subscribe(String topic,String msg) {
        try {
            client.publish(topic, new MqttMessage(msg.getBytes()));
        } catch (MqttException e) {
            log.error("订阅主题失败：{}",e.getMessage());
        }
    }

}

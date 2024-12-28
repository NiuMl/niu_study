package com.niuml.niu_study_sb_mqtt;

import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/***
 * @author niumengliang
 * Date:2024/12/28
 * Time:09:49
 */
@Log4j2
@Component
public class BeanConfig {
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
    @Bean
    public MqttClient initMC() {
        try {
            return new MqttClient(hostUrl, clientId, new MemoryPersistence());
        } catch (MqttException e) {
            log.error("创建MQTT客户端对象失败，客户端连接到服务器失败", e);
            throw new RuntimeException(e);
        }
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions options = new MqttConnectOptions();
        //重新连接
        options.setAutomaticReconnect(true);
        //保持会话不被清理自动重连后才能收到到主题消息（包括离线时发布的消息）
        options.setCleanSession(false);
        //是否清空session，设置为false表示服务器会保留客户端的连接记录（订阅主题，qos），
        // 客户端重连之后能获取到服务器在客户端断开连接期间推送的消息
        //设置为true表示每次连接到服务端都是以新的身份
        options.setCleanSession(true);
        //设置连接用户名
        options.setUserName(username);
        //设置连接密码
        options.setPassword(password.toCharArray());
        //设置超时时间，单位为秒
        options.setConnectionTimeout(100);
        //设置心跳时间 单位为秒，表示服务器每隔1.5*20秒的时间向客户端发送心跳判断客户端是否在线
        options.setKeepAliveInterval(20);
        return options;
    }
}

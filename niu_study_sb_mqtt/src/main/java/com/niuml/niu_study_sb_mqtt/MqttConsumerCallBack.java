package com.niuml.niu_study_sb_mqtt;

import jakarta.annotation.Resource;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


/***
 * @author niumengliang
 * Date:2024/12/27
 * Time:11:47
 */
@Log4j2
@Component
public class MqttConsumerCallBack implements MqttCallbackExtended {


    @Resource
    TestService testService;

    @Lazy
    @Resource
    private MqttClient client;
    @Value("${mqtt.topic}")
    private String[] topic;
    @Value("${mqtt.qos}")
    private int[] qos;

    @Resource
    private MqttConnectOptions options;

//    public MqttConsumerCallBack(String[] topic, int[] qos) {
////        this.client = client;
//        this.topic = topic;
//        this.qos = qos;
////        this.options = options;
//    }

    /**
     * 客户端断开连接的回调
     */
    @Override
    public void connectionLost(Throwable throwable) {
        log.info("MQTT连接断开，发起重连......");
        try {
            if (null != client && !client.isConnected()) {
                client.reconnect();
                log.info("尝试重新连接");
            } else {
                if (client != null) {
                    client.connect(options);
                }
                log.info("尝试建立新连接");
            }
        } catch (Exception e) {
            log.error("MQTT重连失败，请检查网络是否正常:{}", e.getMessage());
        }
    }

    /**
     * 消息到达的回调
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        log.info("接收消息主题 : " + topic);
        log.info("接收消息Qos : " + message.getQos());
        log.info("接收消息内容 : " + new String(message.getPayload()));
        log.info("接收消息retained : " + message.isRetained());
        //TODO 得到上传的数据后 进行一系列处理
        testService.test();
    }

    /**
     * 消息发布成功的回调
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        log.info("消息发布成功:{}", iMqttDeliveryToken.isComplete());

    }

    @Override
    public void connectComplete(boolean b, String s) {
        log.info("连接mqtt成功？:{},地址:{}", b,s);
        try {
            if (null != topic && null != qos) {
                if (client.isConnected()) {
                    client.subscribe(topic, qos);
                    log.info("订阅ok");
                } else {
                   log.info("订阅失败");
                }
            }
        } catch (Exception e) {
            log.info("mqtt订阅主题异常：{}",e.getMessage());
        }
    }
}

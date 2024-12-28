//package com.niuml.niu_study_sb_mqtt.mj;
//
//import lombok.extern.log4j.Log4j2;
//import org.eclipse.paho.client.mqttv3.MqttMessage;
//import org.springframework.stereotype.Component;
//
///***
// * @author niumengliang
// * Date:2024/12/27
// * Time:11:35
// */
//@Log4j2
//@Component
//public class MqttMessageListenerService extends AbstractMqttMessageListenerService {
//
//    public MqttMessageListenerService(MqttTopicAnnotationProcessor mqttTopicAnnotationProcessor) {
//        super(mqttTopicAnnotationProcessor);
//    }
//
//    /**
//     * 测试
//     */
//    @MqttTopicListener(topic = "wlcx/equ", qos = 1)
//    public void testMessage(String topic, MqttMessage message) {
//        log.info("注解：：接收消息主题 : " + topic);
//        log.info("注解：：接收消息Qos : " + message.getQos());
//        log.info("注解：：接收消息内容 : " + new String(message.getPayload()));
//        log.info("注解：：接收消息retained : " + message.isRetained());
//        log.info("注解：：------------------------------------------------");
//    }
//    /**
//     * 测试
//     */
//    @MqttTopicListener(topic = "wlcx/equ2", qos = 1)
//    public void testMessage1(String topic, MqttMessage message) {
//        log.info("----注解：：接收消息主题 : " + topic);
//        log.info("----注解：：接收消息Qos : " + message.getQos());
//        log.info("----注解：：接收消息内容 : " + new String(message.getPayload()));
//        log.info("----注解：：接收消息retained : " + message.isRetained());
//        log.info("------------------------------------------------");
//    }
//
//}

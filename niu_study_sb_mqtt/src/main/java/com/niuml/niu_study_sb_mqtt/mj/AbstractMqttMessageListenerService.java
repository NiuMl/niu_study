//package com.niuml.niu_study_sb_mqtt.mj;
//
//
//import jakarta.annotation.PostConstruct;
//
///***
// * @author niumengliang
// * Date:2024/12/27
// * Time:11:34
// */
//public abstract class AbstractMqttMessageListenerService {
//
//    protected MqttTopicAnnotationProcessor mqttTopicAnnotationProcessor;
//
//    public AbstractMqttMessageListenerService(MqttTopicAnnotationProcessor mqttTopicAnnotationProcessor) {
//        this.mqttTopicAnnotationProcessor = mqttTopicAnnotationProcessor;
//    }
//
//    @PostConstruct
//    public void initialize() {
//        mqttTopicAnnotationProcessor.processAnnotations(this);
//    }
//}

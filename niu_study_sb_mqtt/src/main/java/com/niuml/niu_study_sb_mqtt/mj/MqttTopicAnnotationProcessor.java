//package com.niuml.niu_study_sb_mqtt.mj;
//
///*
// @author niumengliang
//  * Date:2024/12/27
//  * Time:11:32
// */
//
//import com.niuml.niu_study_sb_mqtt.MqttConsumerConfig;
//import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Method;
//
///**
// * method.invoke(objectWithAnnotations, mqttTopic, message);
// * 上面这种方式绕过了 Spring 的 AOP 代理，也就是说，这个调用并不会触发 Spring AOP 的切面逻辑。
// * 也就是说直接使用 objectWithAnnotations 不会经过 Spring 容器，导致 AOP 切面无法拦截和处理这个调用。
// * 改用手动代理方式
// * Object targetObject = applicationContext.getBean(objectWithAnnotations.getClass());
// * method.invoke(targetObject, mqttTopic, message);
// * 或者采用消息适配器
// */
//@Component
//public class MqttTopicAnnotationProcessor {
//
//    private final MqttConsumerConfig mqttClientUtils;
//    private final ApplicationContext applicationContext;
//
//    public MqttTopicAnnotationProcessor(MqttConsumerConfig mqttClientUtils,ApplicationContext applicationContext) {
//        this.mqttClientUtils = mqttClientUtils;
//        this.applicationContext = applicationContext;
//    }
//
//    public void processAnnotations(Object objectWithAnnotations) {
//        Class<?> clazz = objectWithAnnotations.getClass();
//        for (Method method : clazz.getDeclaredMethods()) {
//            if (method.isAnnotationPresent(MqttTopicListener.class)) {
//                MqttTopicListener annotation = method.getAnnotation(MqttTopicListener.class);
//                String topic = annotation.topic();
//                int qos = annotation.qos();
//                IMqttMessageListener listener = (mqttTopic, message) -> {
//                    Object targetObject = applicationContext.getBean(objectWithAnnotations.getClass());
//                    method.invoke(targetObject, mqttTopic, message);
//                };
//                mqttClientUtils.subscribe(topic, qos, listener);
//            }
//        }
//    }
//}

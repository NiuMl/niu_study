//package com.niuml;
//
//import org.eclipse.paho.client.mqttv3.*;
//import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
//
//import java.util.concurrent.CountDownLatch;
//
///***
// * @author niumengliang
// * Date:2024/12/26
// * Time:15:25
// */
//public class MqttPublish {
//    public static void main(String[] args) throws MqttException {
//        String broker = "tcp://47.121.114.64:1883";
//        String clientId = "demo_client1";
//        String topic = "niuml/topic";
//        // 创建一个 CountDownLatch，确保程序持续运行以接收消息
//        CountDownLatch latch = new CountDownLatch(1);
//        try {
//            // 1. 创建 MqttClient 对象
//            MqttClient mqttClient = new MqttClient(broker, clientId, new MemoryPersistence());
//            // 2. 配置连接选项
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setUserName("niuniu");
//            options.setPassword("niuniu".toCharArray());
//            options.setCleanSession(true);
//            // 3. 设置回调函数
//            mqttClient.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable cause) { // 连接丢失处理
//                    System.out.println("连接断开: " + cause.getMessage());
//                }
//                @Override
//                public void messageArrived(String topic, MqttMessage message) throws Exception { // 接收消息处理
//                    System.out.println("接收到消息 - 主题: " + topic);
//                    byte[] payload = message.getPayload();
//                    System.out.println("消息内容: " + new String(payload));
//                }
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken token) { // 消息传输完毕
//                    System.out.println("消息传输完毕");
//                }
//            });
//            // 4. 连接到 MQTT 服务器
//            mqttClient.connect(options);
//            System.out.println("成功连接到 MQTT 服务器");
//            // 5. 订阅主题
//            mqttClient.subscribe(topic, 2); // 订阅 "java/b"，QoS 等级为 2
//            System.out.println("成功订阅主题: "+topic);
//            // 保持运行状态，持续监听消息
//            latch.await();
//        } catch (MqttException e) {
//            System.err.println("连接或订阅失败: " + e.getMessage());
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            System.err.println("程序被中断: " + e.getMessage());
//            e.printStackTrace();
//        }
//
//
//
//    }
//}

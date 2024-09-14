package com.niuml;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.TopicAndPartition;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/***
 * @author niumengliang
 * Date:2024/7/26
 * Time:15:08
 */
@RestController
@SpringBootApplication
public class KafkaApplication {
    private static final Logger logger = LoggerFactory.getLogger(KafkaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @Autowired
    static KafkaConsumer<String, String> consumer;

    @Bean
    public KafkaConsumer<String, String> initConsumer() {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:9092"); // Kafka broker地址
        props.put("group.id", "test-consumer-group"); // 消费者组
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props); // 创建消费者
        return consumer;
    }

    @GetMapping("/send/{topic}/{input}")
    public void sendFoo(@PathVariable("topic") String topic, @PathVariable("input") String input) {
        this.template.send(topic, input);
    }
    @GetMapping("/send/{topic}")
    public void sendFoo2(@PathVariable("topic") String topic, String input) {
        this.template.send(topic, input);
    }

//    @KafkaListener(topics = "niuniuml-topic2")
//    public void listen(String input, Acknowledgment ack) {
//        logger.info("input value: {}", input);
////        ack.acknowledge();//没有这个 kafka消息的 offset不会提交  下次启动的时候 会重复消费
//    }

    @GetMapping("/topicDataCount")
    public void topicDataCount() {
        List<String> strings = Collections.singletonList("127.0.0.1:9092");
        List<String> list = Arrays.asList("niuniuml-topic", "niuniuml-topic2", "niuniuml-topic3", "test-kafka");
        Map<String, Long> monitor = monitor(strings, list);
        monitor.forEach((k, v) -> {
            System.out.println(k + "  " + v);
        });
        Map<String, Long> offsetSum = list.stream().collect(Collectors.toMap(k -> k, v -> getOffsetSum(getPartitionsForTopic(v))));
        offsetSum.forEach((k, v) -> {
            System.out.println(k + "  " + v);
        });
    }

    public List<TopicPartition> getPartitionsForTopic(String topic) {
        return template.partitionsFor(topic).stream()
                .map(info -> new TopicPartition(info.topic(), info.partition())) // 将每个分区信息转换为TopicPartition
                .toList();
//        return consumer.partitionsFor(topic).stream()
//                .map(info -> new TopicPartition(info.topic(), info.partition())) // 将每个分区信息转换为TopicPartition
//                .toList();
    }

    //下面代码的顺序不可改动
    public long getOffsetSum(List<TopicPartition> partitions) {
        long totalOffset = 0;
        consumer.assign(partitions); // 分配分区给消费组
        return partitions.stream().map(partition -> {
            long startOffset = consumer.position(partition); // 获取起始偏移量
            long endOffset = consumer.endOffsets(Collections.singleton(partition)).get(partition);
            consumer.seekToBeginning(Collections.singleton(partition));
            return endOffset - startOffset;
        }).reduce(0L, Long::sum);
        // 返回总消息条数
    }

    public long getLastOffset(SimpleConsumer consumer, String topic, int partition, long whichTime, String clientName) {
        TopicAndPartition topicAndPartition = new TopicAndPartition(topic, partition);
        Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = Maps.newHashMap();
        requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(whichTime, 1));
        kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(requestInfo, kafka.api.OffsetRequest.CurrentVersion(), clientName);
        OffsetResponse response = consumer.getOffsetsBefore(request);

        if (response.hasError()) {
            logger.error("Error fetching data Offset Data the Broker. Reason: " + response.errorCode(topic, partition));
            return 0;
        }
        long[] offsets = response.offsets(topic, partition);
        return offsets[0];
    }

    /**
     * @param brokers broker 地址
     * @param topic   topic
     * @return map<分区, 分区count信息>
     */
    public Map<Integer, PartitionMetadata> findLeader(List<String> brokers, String topic) {
        Map<Integer, PartitionMetadata> map = Maps.newHashMap();
        for (String broker : brokers) {
            SimpleConsumer consumer = null;
            try {
                String[] hostAndPort = broker.split(":");
                consumer = new SimpleConsumer(hostAndPort[0], Integer.parseInt(hostAndPort[1]), 100000, 64 * 1024, "leaderLookup" + new Date().getTime());
                List<String> topics = Lists.newArrayList(topic);
                TopicMetadataRequest req = new TopicMetadataRequest(topics);
                kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

                List<TopicMetadata> metaData = resp.topicsMetadata();
                for (TopicMetadata item : metaData) {
                    for (PartitionMetadata part : item.partitionsMetadata()) {
                        map.put(part.partitionId(), part);
                    }
                }
            } catch (Exception e) {
                logger.error("Error communicating with Broker [" + broker + "] to find Leader for [" + topic + ", ] Reason: " + e);
            } finally {
                if (consumer != null)
                    consumer.close();
            }
        }
        return map;
    }

    public Map<String, Long> monitor(List<String> brokers, List<String> topics) {
        if (brokers == null || brokers.isEmpty()) {
            return null;
        }
        if (topics == null || topics.isEmpty()) {
            return null;
        }
        Map<String, Long> map = Maps.newTreeMap();
        for (String topicName : topics) {
            Map<Integer, PartitionMetadata> metadata = findLeader(brokers, topicName);
            long size = 0L;
            for (Map.Entry<Integer, PartitionMetadata> entry : metadata.entrySet()) {
                int partition = entry.getKey();
                String leadBroker = entry.getValue().leader().host();
                String clientName = "Client_" + topicName + "_" + partition;
                SimpleConsumer consumer = new SimpleConsumer(leadBroker, entry.getValue().leader().port(), 100000, 64 * 1024, clientName);
                long readOffset = getLastOffset(consumer, topicName, partition, kafka.api.OffsetRequest.LatestTime(), clientName);
                size += readOffset;
                consumer.close();
            }
            map.put(topicName, size);
        }
        return map;
    }

}

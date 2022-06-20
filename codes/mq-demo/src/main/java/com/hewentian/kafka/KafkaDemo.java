package com.hewentian.kafka;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * <p>
 * <b>KafkaDemo</b> 是
 * </p>
 *
 * @author <a href="mailto:wentian.he@qq.com">hewentian</a>
 * @date 2019-03-15 14:16:08
 * @since JDK 1.8
 */
public class KafkaDemo {
    private static String bootstrapServers = "kafka.hewentian.com:9092";
    private static String topicName = "myTopic";
    private static String groupId = "groupA";

    /**
     * 创建主题
     */
    public static void createTopic() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        ArrayList<NewTopic> topics = new ArrayList<>();
        NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
        topics.add(newTopic);

        AdminClient adminClient = AdminClient.create(props);
        CreateTopicsResult result = adminClient.createTopics(topics);

        try {
            System.out.println(result.all()); // KafkaFuture{value=null,exception=null,done=false}
            System.out.println(result.all().get()); // null
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
    }

    /**
     * 删除主题
     */
    public static void deleteTopic() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        ArrayList<String> topics = new ArrayList<>();
        topics.add(topicName);

        AdminClient adminClient = AdminClient.create(props);
        DeleteTopicsResult result = adminClient.deleteTopics(topics);

        try {
            System.out.println(result.all()); // KafkaFuture{value=null,exception=null,done=false}
            System.out.println(result.all().get()); // null
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
    }

    /**
     * 生产消息
     */
    public static void produceMsg() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = null;

        try {
            producer = new KafkaProducer<String, String>(properties);
            for (int i = 0; i < 100; i++) {
                String msg = "Message " + i;
                producer.send(new ProducerRecord<String, String>(topicName, msg));
//                producer.send(new ProducerRecord<String, String>(topicName, "key-" + i, msg));
                System.out.println("Sent:" + msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    /**
     * 消费消息
     */
    public static void consumeMsg() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Consumer分组ID
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = null;

        try {
            consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(Arrays.asList(topicName));

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.unsubscribe();
            consumer.close();
        }
    }

    /**
     * 列出所有浪费者组
     */
    public static void listConsumerGroups() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        AdminClient adminClient = AdminClient.create(props);

        try {
            ListConsumerGroupsResult listConsumerGroupsResult = adminClient.listConsumerGroups();
            Collection<ConsumerGroupListing> consumerGroupListings = listConsumerGroupsResult.all().get();
            System.out.println("list.size: " + consumerGroupListings.size());

            for (ConsumerGroupListing g : consumerGroupListings) {
                System.out.println(g.toString());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
    }

    /**
     * 显示某个浪费者组的偏移量
     */
    public static void listConsumerGroupOffsets() {
        Properties props = new Properties();
        props.put(CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        AdminClient adminClient = AdminClient.create(props);

        try {
            ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = adminClient.listConsumerGroupOffsets(groupId);
            KafkaFuture<Map<TopicPartition, OffsetAndMetadata>> mapKafkaFuture = listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata();
            Map<TopicPartition, OffsetAndMetadata> topicPartitionOffsetAndMetadataMap = mapKafkaFuture.get();

            for (Map.Entry<TopicPartition, OffsetAndMetadata> en : topicPartitionOffsetAndMetadataMap.entrySet()) {
                TopicPartition topicPartition = en.getKey();
                System.out.println("topic: " + topicPartition.topic() + ", partition: " + topicPartition.partition());

                OffsetAndMetadata offsetAndMetadata = en.getValue();
                System.out.println(offsetAndMetadata);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
    }

    /**
     * 显示某个主题的长度
     */
    public static void endOffsets() {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId); // Consumer分组ID
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");

        Consumer<String, String> consumer = null;

        try {
            consumer = new KafkaConsumer<>(properties);

            TopicPartition tp = new TopicPartition(topicName, 0);
            Map<TopicPartition, Long> topicPartitionLongMap = consumer.endOffsets(Arrays.asList(tp));

            for (Map.Entry<TopicPartition, Long> en : topicPartitionLongMap.entrySet()) {
                System.out.println(en.getKey() + " -> " + en.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            consumer.close();
        }
    }

    public static void main(String[] args) {
        createTopic();
        deleteTopic();
        produceMsg();
        consumeMsg();
        listConsumerGroups();
        listConsumerGroupOffsets();
        endOffsets();
    }
}

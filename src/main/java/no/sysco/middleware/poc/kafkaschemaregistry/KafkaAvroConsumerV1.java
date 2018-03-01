package no.sysco.middleware.poc.kafkaschemaregistry;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import java.util.Collections;
import java.util.Properties;
import no.sysco.middleware.poc.kafkaschemaregistry.avro.Business;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class KafkaAvroConsumerV1 {
  public static void main(String[] args) {
    Properties properties = new Properties();

    properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
    properties.put(ConsumerConfig.GROUP_ID_CONFIG, "customer-consumer-group-v1");
    properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
    properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

    properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class.getName());
    properties.setProperty("schema.registry.url", "http://localhost:8081");
    properties.setProperty("specific.avro.reader", "true");

    KafkaConsumer<String, Business> kafkaConsumer = new KafkaConsumer<>(properties);
    kafkaConsumer.subscribe(Collections.singleton(Utils.TOPIC));

    while (true){
      ConsumerRecords<String, Business> records = kafkaConsumer.poll(1000);
      for (ConsumerRecord<String, Business> record : records){
        Business business = record.value();
        System.out.println(business);
      }

      kafkaConsumer.commitSync();
    }
  }
}

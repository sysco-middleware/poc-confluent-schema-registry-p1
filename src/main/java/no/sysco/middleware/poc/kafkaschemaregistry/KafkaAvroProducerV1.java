package no.sysco.middleware.poc.kafkaschemaregistry;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import no.sysco.middleware.poc.kafkaschemaregistry.avro.Business;
import no.sysco.middleware.poc.kafkaschemaregistry.avro.Customer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Collections;
import java.util.Properties;

public class KafkaAvroProducerV1 {
  public static void main(String[] args) {
    Properties properties = new Properties();

    properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    properties.setProperty(ProducerConfig.ACKS_CONFIG, "all");
    properties.setProperty(ProducerConfig.RETRIES_CONFIG, "10");

    properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
    properties.setProperty("schema.registry.url", "http://localhost:8081");

    Producer<String, Business> producer = new KafkaProducer<>(properties);


    // copied from avro examples
    Customer c1 = Customer.newBuilder()
        .setFirstName("some-first-name-v1")
        .setLastName("some-last-name-v1")
        .build();

    Business business = Business.newBuilder()
        .setCompanyName("some-company-name-v1")
        .setCustomers(Collections.singletonList(c1))
        .build();


    ProducerRecord<String, Business> producerRecord = new ProducerRecord<>(
        Utils.TOPIC, business
    );

    System.out.println(business);
    producer.send(producerRecord, (metadata, exception) -> {
      if (exception == null) {
        System.out.println(metadata);
      } else {
        exception.printStackTrace();
      }
    });

    producer.flush();
    producer.close();

  }
}

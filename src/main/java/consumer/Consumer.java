package consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {



    private static KafkaConsumer<String, String> createKafkaConsumer(){

        String boostrapServers = "my-cluster-kafka-bootstrap.kafka:9092";
        String groupId = "group-1";

        // create consumer configs
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, boostrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        // create consumer
        return new KafkaConsumer<>(properties);

    }



    public static void main(String[] args) throws IOException, InterruptedException {
        String var_1 = System.getenv("VAR_1");

        Logger log = LoggerFactory.getLogger(Consumer.class.getSimpleName());
        KafkaConsumer<String, String> consumer = createKafkaConsumer();
        consumer.subscribe(Collections.singleton("test"));

        while(true) {
            //Step 1 Poll message

            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000)); //timeout



            log.info(var_1 + " Polled " + records.count() );



            //Step 2 Process message
            for (ConsumerRecord<String, String> record : records) {
                try {
                    Thread.sleep(7000);
                    log.info(record.value() + " is done processing");
                } catch (Exception e){
                    log.info(e.getMessage());
                }
            }

            //Step 3 Send ack
            consumer.commitSync();
            log.info("Acknowledgment message sent to kafka, ready for new poll");
        }
    }

}

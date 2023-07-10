package fuse.quarkus.camel.producer;

import java.util.logging.Logger;

import org.apache.camel.builder.RouteBuilder;

import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class KafkaProducerListener extends RouteBuilder {
	
   @Override
   public void configure() throws Exception {
      // Kafka Producer
       from("timer://foo?period=1000")
       .to("kafka:myTopic?brokers=localhost:9092");
       
      // Kafka Consumer
       from("kafka:myTopic?brokers=localhost:9092")
       .log("Message received from Kafka : ${body}")
       .log("    on the topic ${headers[kafka.TOPIC]}")
       .log("    on the partition ${headers[kafka.PARTITION]}")
       .log("    with the offset ${headers[kafka.OFFSET]}")
       .log("    with the key ${headers[kafka.KEY]}");
   }
	
	

	 private static final Logger LOGGER = Logger.getLogger(KafkaProducerListener.class.getName());

}
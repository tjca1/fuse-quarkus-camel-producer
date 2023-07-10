package fuse.quarkus.camel.producer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.message.ConsumerProtocolAssignment.TopicPartition;
import io.smallrye.common.annotation.Identifier;
import io.smallrye.reactive.messaging.kafka.KafkaConsumerRebalanceListener;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.kafka.clients.consumer.Consumer;


@ApplicationScoped
@Identifier("rebalanced-example.rebalancer")
public class KafkaRebalancedConsumerRebalanceListener implements KafkaConsumerRebalanceListener {
	
	 private static final Logger LOGGER = Logger.getLogger(KafkaRebalancedConsumerRebalanceListener.class.getName());

	    @Override
	    public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<org.apache.kafka.common.TopicPartition> partitions) {
	        long now = System.currentTimeMillis();
	        long shouldStartAt = now - 600_000L; //10 minute ago

	        Map<org.apache.kafka.common.TopicPartition, Long> request = new HashMap<>();
	        for (org.apache.kafka.common.TopicPartition partition : partitions) {
	            LOGGER.info("Assigned " + partition);
	            request.put(partition, shouldStartAt);
	        }
	        Map<org.apache.kafka.common.TopicPartition, OffsetAndTimestamp> offsets = consumer.offsetsForTimes(request);
	        for (Map.Entry<org.apache.kafka.common.TopicPartition, OffsetAndTimestamp> position : offsets.entrySet()) {
	            long target = position.getValue() == null ? 0L : position.getValue().offset();
	            LOGGER.info("Seeking position " + target + " for " + position.getKey());
	            consumer.seek(position.getKey(), target);
	        }
	    }
    

}
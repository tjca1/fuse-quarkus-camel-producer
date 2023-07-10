package fuse.quarkus.camel.message;


import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fuse.quarkus.camel.dto.QuotationDTO;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class KafkaEvents {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("quotation-channel") /* CANAL QUE VAI TER ACESSO AO TOPICO DO KAFKA*/
    Emitter<QuotationDTO> quotationRequestEmitter;

    public void sendNewKafkaEvent(QuotationDTO dto){
        LOG.info("-- Enviando contacao para o topico kafka");
        quotationRequestEmitter.send(dto).toCompletableFuture().join();
    }
    
    
}

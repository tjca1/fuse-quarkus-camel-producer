package fuse.quarkus.camel.kafka.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DescriptionTopic {
    private Date date;
    private String nameTopic;
    private String nameHost;
    private String producerOrConsumer;

}

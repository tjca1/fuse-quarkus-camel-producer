package fuse.quarkus.camel.entity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "register_topic_entity")
@Data
@NoArgsConstructor
public class RegisterTopicEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "date")
    private Date date;

    @Column(name = "name_topic")
    private String nameTopic;

    @Column(name = "name_host")
    private String nameHost;

    @Column(name = "producer_or_consumer")
    private String producerOrConsumer;
}

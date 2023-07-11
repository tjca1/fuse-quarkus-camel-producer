/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fuse.quarkus.camel.kafka;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fuse.quarkus.camel.entity.RegisterTopicEntity;
import fuse.quarkus.camel.kafka.dto.DescriptionTopic;
import fuse.quarkus.camel.repository.RegisterTopicRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @ConfigProperty(name = "kafka.topic.name")
    String nameTopic;

    @ConfigProperty(name = "camel.component.kafka.brokers")
    String host;

    @Inject
    RegisterTopicRepository repository;

    @Override
    @Transactional
    public void configure() throws Exception {
        /* TESTES */
        DescriptionTopic descriptionTopic = new DescriptionTopic(new Date(),
                nameTopic, host, "PRODUCER");
        //
        RegisterTopicEntity entity = new RegisterTopicEntity();
        BeanUtils.copyProperties(entity, descriptionTopic);
        //persisten table//
        repository.persist(entity);
        //
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(descriptionTopic);
        // produces messages to kafka
        from("timer:foo?period={{timer.period}}&delay={{timer.delay}}")
                .routeId("FromTimer2Kafka")
                .setBody().simple(json)
                .to("kafka:{{kafka.topic.name}}")
                .log("Message correctly sent to the topic! : \"${body}\" ");

        /*
        from("kafka:{{kafka.topic.name}}")
                .routeId("FromKafka2Seda")
                .log("Received : \"${body}\"")
                .to("seda:kafka-messages");
        */
    }
}

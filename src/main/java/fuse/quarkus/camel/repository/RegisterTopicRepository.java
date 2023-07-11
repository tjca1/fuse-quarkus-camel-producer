package fuse.quarkus.camel.repository;

import fuse.quarkus.camel.entity.RegisterTopicEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RegisterTopicRepository implements PanacheRepository<RegisterTopicEntity> {

}

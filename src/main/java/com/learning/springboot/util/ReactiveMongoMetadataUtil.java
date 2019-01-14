package com.learning.springboot.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.springboot.model.Person;
import com.learning.springboot.model.User;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.UUID;

@Slf4j
@Component
@AllArgsConstructor
public class ReactiveMongoMetadataUtil {
    private final ReactiveMongoTemplate mongoTemplate;

    private final ObjectMapper objectMapper;

    public Mono<MongoCollection<Document>> recreateCollection(Class<? extends Serializable> entity) {
        return mongoTemplate.collectionExists(entity)
                .flatMap(exists -> exists ? mongoTemplate.dropCollection(entity) : Mono.just(exists))
                .then(mongoTemplate.createCollection(entity, CollectionOptions.empty()
                        .size(1024 * 1024)
                        .maxDocuments(100)
                        .capped()));

    }

    @PostConstruct
    public void init() {
        log.debug("ReactiveMongoMetadataUtil:init: {}", this);
        recreateCollection(Person.class)
                .subscribe(p -> log.debug("Person Output: {}", p));

        recreateCollection(User.class)
                .subscribe(m -> insertSystemDefaultUser());
    }

    private void insertSystemDefaultUser() {
        try {
            User user = User.builder()
                    .email("default@admin.com")
                    .password("noPassword")
                    .fullName("System User")
                    .enabled(false)
                    .id(UUID.randomUUID().toString())
                    .build();
            log.debug("Creating default user: {}", user);
            mongoTemplate.save(objectMapper.writeValueAsString(user), "users")
                    .subscribe(u -> log.debug("Created Default User: {}", u));
        } catch (JsonProcessingException e) {
            log.error("Error on method insertSystemDefaultUser", e);
            throw new RuntimeException("Error on method insertSystemDefaultUser", e);
        }
    }
}

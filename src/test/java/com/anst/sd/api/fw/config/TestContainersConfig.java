package com.anst.sd.api.fw.config;

import com.redis.testcontainers.RedisContainer;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;

@Configuration
public class TestContainersConfig {
    public static final KafkaContainer kafka = new KafkaContainer(
            DockerImageName.parse("confluentinc/cp-kafka:7.3.3"))
            .waitingFor(Wait.forListeningPort());
    public static final MongoDBContainer mongo = new MongoDBContainer(
            DockerImageName.parse("mongo:5"))
            .waitingFor(Wait.forListeningPort());
    public static final RedisContainer redis = new RedisContainer(
            DockerImageName.parse("redis:5.0.3-alpine"))
            .waitingFor(Wait.forListeningPort());
    public static final PostgreSQLContainer postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:14.6"))
            .waitingFor(Wait.forListeningPort());

    static {
        mongo.start();
        System.setProperty("spring.data.mongodb.uri", mongo.getReplicaSetUrl());
        kafka.start();
        System.setProperty("spring.kafka.bootstrap-servers", kafka.getBootstrapServers());
        redis.start();
        System.setProperty("spring.data.redis.url", redis.getRedisURI());
        postgres.start();
    }

    @Primary
    @Bean
    public DataSource dataSource() {
        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(postgres.getJdbcUrl());
        hikariConfig.setUsername(postgres.getUsername());
        hikariConfig.setPassword(postgres.getPassword());
        return new HikariDataSource(hikariConfig);
    }

    @Bean(destroyMethod = "stop")
    public MongoDBContainer mongoDBContainer() {
        return mongo;
    }

    @Bean(destroyMethod = "stop")
    public KafkaContainer kafkaContainer() {
        return kafka;
    }

    @Bean(destroyMethod = "stop")
    public RedisContainer redisContainer() {
        return redis;
    }

    @Bean(destroyMethod = "stop")
    public PostgreSQLContainer postgreSQLContainer() {
        return postgres;
    }
}
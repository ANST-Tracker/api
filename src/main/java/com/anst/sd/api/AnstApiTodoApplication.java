package com.anst.sd.api;

import com.anst.sd.api.adapter.persistence.UserCodeMongoRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackageClasses = UserCodeMongoRepository.class)
@EnableJpaRepositories(excludeFilters =
@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = UserCodeMongoRepository.class))
@SpringBootApplication(exclude = {
        SecurityAutoConfiguration.class,
        ManagementWebSecurityAutoConfiguration.class,
        UserDetailsServiceAutoConfiguration.class}
)
public class AnstApiTodoApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnstApiTodoApplication.class, args);
    }

}

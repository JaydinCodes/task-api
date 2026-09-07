package com.jaydin.taskapi.config;

import com.jaydin.taskapi.service.DatabaseConnectionProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseConfig {

    @Bean
    public DatabaseConnectionProvider databaseConnectionProvider(){
        return new DatabaseConnectionProvider("jdbc:h2:mem:testdb", "sa", "ps");
    }
}

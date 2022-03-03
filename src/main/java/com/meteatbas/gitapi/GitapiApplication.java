package com.meteatbas.gitapi;

import com.meteatbas.gitapi.config.ApplicationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationProperties.class)
public class GitapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(GitapiApplication.class, args);
    }

}

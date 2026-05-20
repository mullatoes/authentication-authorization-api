package com.jdevs.securebankapi;

import com.jdevs.securebankapi.auth.token.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(
        JwtProperties.class
)
public class SecureBankApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecureBankApiApplication.class, args);
    }

}

package ru.otus.testingExample.services.boot;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan({"ru.otus.testingExample.config", "ru.otus.testingExample.dao", "ru.otus.testingExample.services"})
public class TestContextConfig {
}

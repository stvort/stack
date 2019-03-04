package ru.otus.testingExample.dao.boot;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@SpringBootConfiguration
@EnableConfigurationProperties
@ComponentScan({"ru.otus.testingExample.config", "ru.otus.testingExample.dao"})
public class TestContextConfig {
}

package ru.otus.testingExample.spring.services;

import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import ru.otus.testingExample.dao.GreetingDao;
import ru.otus.testingExample.services.IOService;

// Класс для замены некоторых бинов в контексте на моки
@Configuration
public class TestContextConfig {

    @Bean
    @Primary
    GreetingDao greetingDao(){
        return Mockito.mock(GreetingDao.class);
    }

    @Bean
    @Primary
    IOService ioService(){
        return Mockito.mock(IOService.class);
    }
}

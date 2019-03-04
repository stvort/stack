package ru.otus.testingExample.services.boot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.testingExample.dao.GreetingDao;
import ru.otus.testingExample.services.GreetingService;
import ru.otus.testingExample.services.IOService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.testingExample.services.CountryCodes.*;

@DisplayName("Методы сервиса приветствий должны вызывать методы ioService и greetingDao с нужными параметрами. ")
@SpringBootTest
class GreetingServiceImplTest {

    @MockBean
    private IOService ioService;

    @MockBean
    private GreetingDao greetingDao;

    @Autowired
    private GreetingService greetingService;

    @BeforeEach
    void setUp() {
        given(greetingDao.findGreetingByCountryCode(any())).willReturn(Optional.of(""));
    }

    @Test
    @DisplayName("Текущий метод: sayRussianGreeting")
    void sayRussianGreeting() {
        greetingService.sayRussianGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_RU);
    }

    @Test
    @DisplayName("Текущий метод: sayEnglishGreeting")
    void sayEnglishGreeting() {
        greetingService.sayEnglishGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_EN);
    }

    @Test
    @DisplayName("Текущий метод: sayChinaGreeting")
    void sayChinaGreeting() {
        greetingService.sayChinaGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_CN);
    }
}
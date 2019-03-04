package ru.otus.testingExample.services.plain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.testingExample.dao.GreetingDao;
import ru.otus.testingExample.services.GreetingService;
import ru.otus.testingExample.services.GreetingServiceImpl;
import ru.otus.testingExample.services.IOService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static ru.otus.testingExample.services.CountryCodes.*;

@DisplayName("Методы сервиса приветствий должны вызывать методы ioService и greetingDao с нужными параметрами. ")
@ExtendWith(MockitoExtension.class)
class GreetingServiceImplTest {

    @Mock
    private IOService ioService;

    @Mock
    private GreetingDao greetingDao;

    private GreetingService greetingService;

    @BeforeEach
    void setUp() {
        given(greetingDao.findGreetingByCountryCode(any())).willReturn(Optional.of(""));
        greetingService = new GreetingServiceImpl(ioService, greetingDao);
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
package ru.otus.testingExample.spring.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.testingExample.TestingExampleSpringApplication;
import ru.otus.testingExample.dao.GreetingDao;
import ru.otus.testingExample.services.GreetingNotFoundException;
import ru.otus.testingExample.services.GreetingService;
import ru.otus.testingExample.services.IOService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static ru.otus.testingExample.services.CountryCodes.*;

// Тест с поднятием контекста spring
@DisplayName("Методы сервиса приветствий должны ")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestingExampleSpringApplication.class)
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
    @DisplayName("вызывать методы ioService и greetingDao с нужными параметрами. Текущий метод: sayRussianGreeting")
    void shouldExecuteServiceMethodsForRussianGreeting() {
        greetingService.sayRussianGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_RU);
    }

    @Test
    @DisplayName("вызывать методы ioService и greetingDao с нужными параметрами. Текущий метод: sayEnglishGreeting")
    void shouldExecuteServiceMethodsForEnglishGreeting() {
        greetingService.sayEnglishGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_EN);
    }

    @Test
    @DisplayName("вызывать методы ioService и greetingDao с нужными параметрами. Текущий метод: sayChinaGreeting")
    void shouldExecuteServiceMethodsForChinaGreeting() {
        greetingService.sayChinaGreeting();
        verify(ioService, times(1)).out("");
        verify(greetingDao, times(1)).findGreetingByCountryCode(COUNTRY_CODE_CN);
    }

    @Test
    @DisplayName(" не бросать исключение если приветствие найдено")
    void shouldNotThrowExceptionIfGreetingExists() {
        assertThatCode(() -> greetingService.sayChinaGreeting()).doesNotThrowAnyException();

    }

    @Test
    @DisplayName(" бросать исключение если приветствие не найдено")
    void shouldThrowFoundExceptionIfGreetingNotExists() {
        given(greetingDao.findGreetingByCountryCode(any())).willReturn(Optional.empty());
        assertThatThrownBy(() -> greetingService.sayChinaGreeting()).isInstanceOf(GreetingNotFoundException.class);

    }

}
package ru.otus;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.otus.config.AppConfigSpring;
import ru.otus.services.GameProcessor;

public class SpringApp {

    public static void main(String[] args) throws Exception {
        ApplicationContext container = new AnnotationConfigApplicationContext(AppConfigSpring.class);
        GameProcessor gameProcessor = container.getBean(GameProcessor.class);

        gameProcessor.startGame();
    }
}

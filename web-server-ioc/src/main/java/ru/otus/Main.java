package ru.otus;

import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.config.AppConfig;
import ru.otus.config.DBConfig;
import ru.otus.config.ToolsConfig;
import ru.otus.server.UsersWebServer;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class Main {

    public static void main(String[] args) throws Exception {
        //AppComponentsContainer container = new AppComponentsContainerImpl(new Class[]{AppConfig.class, ToolsConfig.class, DBConfig.class});
        AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");
        //UsersWebServer usersWebServer = container.getAppComponent(UsersWebServer.class);
        UsersWebServer usersWebServer = container.getAppComponent("usersWebServer");
        usersWebServer.start();
        usersWebServer.join();
    }
}

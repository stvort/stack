package ru.otus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerSimple;
import ru.otus.server.UsersWebServerWithBasicSecurity;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.*;

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
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        UserDao userDao = new InMemoryUserDao();
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        //UsersWebServer usersWebServer = buildUsersWebServerSimple(userDao, gson, templateProcessor);
        //UsersWebServer usersWebServer = buildUsersWebServerWithFilterBasedSecurity(userDao, gson, templateProcessor);
        UsersWebServer usersWebServer = buildUsersWebServerWithBasicSecurity(userDao, gson, templateProcessor);


        usersWebServer.start();
        usersWebServer.join();
    }

    private static UsersWebServer buildUsersWebServerSimple(UserDao userDao, Gson gson, TemplateProcessor templateProcessor)  {
        return new UsersWebServerSimple(WEB_SERVER_PORT, userDao, gson, templateProcessor);
    }

    private static UsersWebServer buildUsersWebServerWithFilterBasedSecurity(UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        return new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT, authService, userDao, gson, templateProcessor);
    }

    private static UsersWebServer buildUsersWebServerWithBasicSecurity(UserDao userDao, Gson gson, TemplateProcessor templateProcessor) {
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        LoginService loginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        //LoginService loginService = new InMemoryLoginServiceImpl(userDao);

        return new UsersWebServerWithBasicSecurity(WEB_SERVER_PORT, loginService, userDao, gson, templateProcessor);
    }
}

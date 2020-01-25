package ru.otus.config;

import com.google.gson.Gson;
import org.eclipse.jetty.security.LoginService;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.dao.UserDao;
import ru.otus.server.SecurityType;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerImpl;
import ru.otus.services.*;


import static ru.otus.server.SecurityType.*;

@AppComponentsContainerConfig(order = 1)
public class AppConfig {
    private static final int WEB_SERVER_PORT = 8080;

    //@AppComponent(order = 0, name = "securityType")
    public SecurityType securityTypeFilterBased() {
        return FILTER_BASED;
    }

    @AppComponent(order = 0, name = "securityType")
    public SecurityType securityTypeBasic() {
        return BASIC;
    }

    @AppComponent(order = 1, name = "usersWebServer")
    public UsersWebServer usersWebServer(SecurityType securityType,
                                         UserAuthService userAuthServiceForFilterBasedSecurity,
                                         LoginService loginServiceForBasicSecurity,
                                         UserDao userDao,
                                         Gson gson,
                                         TemplateProcessor templateProcessor) {
        return new UsersWebServerImpl(WEB_SERVER_PORT,
                securityType,
                userAuthServiceForFilterBasedSecurity,
                loginServiceForBasicSecurity,
                userDao,
                gson,
                templateProcessor);
    }

}

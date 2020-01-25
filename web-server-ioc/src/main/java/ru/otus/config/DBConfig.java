package ru.otus.config;

import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.dao.InMemoryUserDao;
import ru.otus.dao.UserDao;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.InMemoryLoginServiceImpl;
import ru.otus.services.UserAuthService;
import ru.otus.services.UserAuthServiceImpl;

@AppComponentsContainerConfig(order = 0)
public class DBConfig {
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    @AppComponent(order = 0, name = "userDao")
    public UserDao userDao() {
        return new InMemoryUserDao();
    }

    @AppComponent(order = 1, name = "userAuthService")
    public UserAuthService userAuthService(UserDao userDao) {
        return new UserAuthServiceImpl(userDao);
    }

    @AppComponent(order = 2, name = "loginService")
    public LoginService loginService() throws Exception {
        String hashLoginServiceConfigPath = FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        HashLoginService hashLoginService = new HashLoginService(REALM_NAME, hashLoginServiceConfigPath);
        hashLoginService.start();
        return hashLoginService;
    }

    //@AppComponent(order = 2, name = "loginService")
    public LoginService loginService(UserDao userDao){
        return new InMemoryLoginServiceImpl(userDao);
    }

}

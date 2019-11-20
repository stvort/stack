package ru.otus.services;

import org.eclipse.jetty.security.AbstractLoginService;
import ru.otus.dao.UserDao;
import ru.otus.model.User;

import java.util.Optional;

public class InMemoryLoginService extends AbstractLoginService {

    private final UserDao userDao;

    public InMemoryLoginService(UserDao userDao) {
        this.userDao = userDao;
    }


    @Override
    protected String[] loadRoleInfo(UserPrincipal userPrincipal) {
        return new String[] {"user"};
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        Optional<User> dbUser = userDao.findByLogin(login);
        //UserPrincipal userPrincipal = new UserPrincipal();
        return null;
    }
}

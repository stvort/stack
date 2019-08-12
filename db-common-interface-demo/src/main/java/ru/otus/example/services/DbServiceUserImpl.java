package ru.otus.example.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.example.dao.UserDao;
import ru.otus.example.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
  private static Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);
  private final UserDao userDao;

  public DbServiceUserImpl(UserDao userDao) {
    this.userDao = userDao;
  }

  @Override
  public long saveUser(User user) throws Exception {
    userDao.getSessionManager().beginSession();
    try {
      long userId = userDao.saveUser(user);
      userDao.getSessionManager().endSession();

      logger.info("created user: {}", userId);
      return userId;
    } catch (Exception e){
      logger.error(e.getMessage(), e);
      userDao.getSessionManager().rollbackSession();
    }
    return 0;
  }

  @Override
  public Optional<User> getUser(long id) throws Exception {
    userDao.getSessionManager().beginSession();
    try {
      Optional<User> userOptional = userDao.findById(id);
      userDao.getSessionManager().endSession();

      logger.info("user: {}", userOptional.orElse(null));
      return userOptional;
    } catch (Exception e){
      logger.error(e.getMessage(), e);
      userDao.getSessionManager().rollbackSession();
    }
    return Optional.empty();
  }
}

package ru.otus.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.example.dao.UserDao;
import ru.otus.example.dao.UserDaoImpl;
import ru.otus.example.db.SessionManager;
import ru.otus.example.db.jdbc.SessionManagerJdbcImpl;
import ru.otus.example.model.User;
import ru.otus.example.services.DbServiceUserImpl;
import ru.otus.example.h2.DataSourceH2;
import ru.otus.example.services.DBServiceUser;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

public class Demo {

  private static Logger logger = LoggerFactory.getLogger(Demo.class);

  public static void main(String[] args) throws Exception {
    DataSource dataSource = new DataSourceH2();
    Demo demo = new Demo();
    createTable(dataSource);

    SessionManager sessionManager = new SessionManagerJdbcImpl(dataSource);
    UserDao userDao = new UserDaoImpl(sessionManager);
    DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);

    long id = dbServiceUser.saveUser(new User(0, "dbServiceUser"));
    loadUserAndOutput(dbServiceUser, id);

    Thread thread = new Thread(() -> {
      try {
        loadUserAndOutput(dbServiceUser, id);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    thread.start();
    thread.join();
  }

  private static void createTable(DataSource dataSource) throws SQLException {
    try (Connection connection = dataSource.getConnection();
         PreparedStatement pst = connection.prepareStatement("create table user(id long auto_increment, name varchar(50))")) {
      pst.executeUpdate();
    }
    System.out.println("table created");
  }

  private static void loadUserAndOutput(DBServiceUser dbServiceUser, long id) throws Exception {
    Optional<User> mayBeUser = dbServiceUser.getUser(id);
    mayBeUser.ifPresentOrElse(
            u -> logger.info("Loaded user, name:{}", u.getName()),
            () -> logger.info("user was not loaded")
    );
  }
}

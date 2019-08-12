package ru.otus.example.dao;

import ru.otus.example.db.DatabaseSession;
import ru.otus.example.db.SessionManager;
import ru.otus.example.db.jdbc.DatabaseSessionJdbc;
import ru.otus.example.db.jdbc.DbExecutor;
import ru.otus.example.model.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

public class UserDaoImpl implements UserDao {

  private final SessionManager sm;

  public UserDaoImpl(SessionManager sm) {
    this.sm = sm;
  }


  @Override
  public SessionManager getSessionManager() {
    return sm;
  }

  @Override
  public Optional<User> findById(long id) {
    try {
      Optional<User> user = DbExecutor.selectRecord(getConnection(), "select id, name from user where id  = ?", id, resultSet -> {
        try {
          if (resultSet.next()) {
            return new User(resultSet.getLong("id"), resultSet.getString("name"));
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        return null;
      });
      return user;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Optional.empty();
  }

  @Override
  public long saveUser(User user) {
    try {
      return DbExecutor.insertRecord(getConnection(), "insert into user(name) values (?)", Collections.singletonList(user.getName()));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  private Connection getConnection() throws Exception {
    DatabaseSession s = sm.currentSession();
    if (s == null) {
      throw new RuntimeException("No opened connection");
    }
    if (!(s instanceof DatabaseSessionJdbc)) {
      throw new RuntimeException("Bad session type");
    }
    return ((DatabaseSessionJdbc) s).getConnection();
  }
}

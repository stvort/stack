package ru.otus.example.dao;

import ru.otus.example.db.SessionManager;
import ru.otus.example.model.User;

import java.util.Optional;

public interface UserDao {
  SessionManager getSessionManager();

  Optional<User> findById(long id);
  long saveUser(User user);
}

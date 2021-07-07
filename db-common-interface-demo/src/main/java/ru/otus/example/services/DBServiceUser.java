package ru.otus.example.services;

import ru.otus.example.model.User;

import java.util.Optional;

public interface DBServiceUser {

  long saveUser(User user) throws Exception;

  Optional<User> getUser(long id) throws Exception;

}

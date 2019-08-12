package ru.otus.example.db;

public interface SessionManager {

  DatabaseSession beginSession() throws Exception;

  void endSession() throws Exception;

  void rollbackSession() throws Exception;

  DatabaseSession currentSession() throws Exception;
}

package ru.otus.example.db.jdbc;

import ru.otus.example.db.DatabaseSession;

import java.sql.Connection;

public class DatabaseSessionJdbc implements DatabaseSession {
  private final Connection connection;

  public DatabaseSessionJdbc(Connection connection) {
    this.connection = connection;
  }

  public Connection getConnection() {
    return connection;
  }
}

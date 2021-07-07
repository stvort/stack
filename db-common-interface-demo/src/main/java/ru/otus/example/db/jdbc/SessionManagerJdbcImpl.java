package ru.otus.example.db.jdbc;

import ru.otus.example.db.DatabaseSession;
import ru.otus.example.db.SessionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManagerJdbcImpl implements SessionManager {

  private final DataSource dataSource;
  private static final int TIMEOUT_IN_SECONDS = 5;
  private final Map<Thread, Connection> connectionMap;

  public SessionManagerJdbcImpl(DataSource dataSource) {

    if (dataSource == null) {
      throw new RuntimeException("Datasource is null");
    }
    this.dataSource = dataSource;

    connectionMap = new ConcurrentHashMap<>();
  }

  @Override
  public DatabaseSession beginSession() throws Exception {
    Thread currentThread = Thread.currentThread();
    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: " + currentThread.getName());
    if (connectionMap.containsKey(currentThread)) {
      throw new RuntimeException("Session already opened");
    }

    Connection connection = dataSource.getConnection();
    connectionMap.put(currentThread, connection);
    return new DatabaseSessionJdbc(connection);
  }

  @Override
  public void endSession() throws Exception {
    Connection connection = getConnection();
    connection.commit();
    connectionMap.remove(Thread.currentThread());
  }

  @Override
  public void rollbackSession() throws Exception {
    Connection connection = getConnection();
    connection.rollback();
    connectionMap.remove(Thread.currentThread());
  }

  @Override
  public DatabaseSession currentSession() throws Exception {
    return new DatabaseSessionJdbc(getConnection());
  }

  private void checkConnection(Connection connection) throws SQLException {
    if (connection == null || !connection.isValid(TIMEOUT_IN_SECONDS)) {
      throw new RuntimeException("Connection is invalid");
    }
  }

  private Connection getConnection() throws SQLException {
    Connection connection = connectionMap.get(Thread.currentThread());
    checkConnection(connection);
    return connection;
  }

}

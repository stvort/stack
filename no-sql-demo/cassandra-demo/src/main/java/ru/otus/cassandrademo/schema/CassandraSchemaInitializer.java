package ru.otus.cassandrademo.schema;

public interface CassandraSchemaInitializer {
    void initSchema();
    void dropSchemaifExists();
}

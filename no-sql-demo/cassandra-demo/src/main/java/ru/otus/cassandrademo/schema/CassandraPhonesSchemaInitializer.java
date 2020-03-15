package ru.otus.cassandrademo.schema;

import com.datastax.driver.core.Session;
import lombok.RequiredArgsConstructor;
import ru.otus.cassandrademo.template.CassandraConnection;

@RequiredArgsConstructor
public class CassandraPhonesSchemaInitializer implements CassandraSchemaInitializer {
    private static final String SIMPLE_REPLICATION_STRATEGY = "SimpleStrategy";
    private static final int REPLICATION_FACTOR = 1;

    public static final String PRODUCTS_KEY_SPACE = "Products";
    public static final String PHONES_TABLE = "Phones";
    public static final String FULL_PHONES_TABLE_NAME = PRODUCTS_KEY_SPACE + "." + PHONES_TABLE;

    private final CassandraConnection cassandraConnection;

    @Override
    public void initSchema() {
        Session session = cassandraConnection.getSession();
        createKeySpace(session);
        createTable(session);
    }

    @Override
    public void dropSchemaifExists() {
        String query = "DROP KEYSPACE IF EXISTS " + PRODUCTS_KEY_SPACE;
        cassandraConnection.getSession().execute(query);
    }

    private void createKeySpace(Session session) {
        String query = "CREATE KEYSPACE IF NOT EXISTS " +
                PRODUCTS_KEY_SPACE + " WITH replication = {" +
                "'class':'" + SIMPLE_REPLICATION_STRATEGY +
                "','replication_factor':" + REPLICATION_FACTOR +
                "};";
        session.execute(query);
    }

    private void createTable(Session session) {
        String query = "CREATE TABLE IF NOT EXISTS " +
                FULL_PHONES_TABLE_NAME + "(" +
                "id uuid PRIMARY KEY, " +
                "model text," +
                "color text," +
                "serialNumber text," +
                "operatingSystem text);";
        session.execute(query);
    }
}

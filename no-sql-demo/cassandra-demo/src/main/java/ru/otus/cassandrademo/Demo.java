package ru.otus.cassandrademo;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import lombok.val;
import ru.otus.cassandrademo.model.Phone;
import ru.otus.cassandrademo.model.SmartPhone;
import ru.otus.cassandrademo.schema.CassandraPhonesSchemaInitializer;
import ru.otus.cassandrademo.schema.CassandraSchemaInitializer;
import ru.otus.cassandrademo.template.CassandraConnection;
import ru.otus.cassandrademo.template.PhoneRepository;
import ru.otus.cassandrademo.template.PhoneRepositoryImpl;

import java.util.UUID;

import static ru.otus.cassandrademo.schema.CassandraPhonesSchemaInitializer.FULL_PHONES_TABLE_NAME;


public class Demo {

    private static final int CASSANDRA_PORT = 9042;
    private static final String CASSANDRA_HOST = "192.168.99.100";

    public static void main(String[] args) throws Throwable {
        val motorolaC350 = new Phone(UUID.randomUUID(), "C350", "silver", "000001");
        val motorolaZ800i = new Phone(UUID.randomUUID(), "Z800i", "silver", "000002");
        val huaweiP20 = new SmartPhone(UUID.randomUUID(),"p20", "black", "000003", "Android");

        try (CassandraConnection connector = new CassandraConnection(CASSANDRA_HOST, CASSANDRA_PORT)) {
            CassandraSchemaInitializer initializer = new CassandraPhonesSchemaInitializer(connector);
            PhoneRepository repository = new PhoneRepositoryImpl(connector);

            initializer.dropSchemaifExists();
            initializer.initSchema();

            Session session = connector.getSession();
            repository.insert(motorolaC350, Phone.class);
            repository.insert(motorolaZ800i, Phone.class);
            repository.insert(huaweiP20, SmartPhone.class);

            ResultSet resultSet = session.execute("SELECT * FROM " + FULL_PHONES_TABLE_NAME);
            resultSet.all().forEach(System.out::println);

        }
    }



}

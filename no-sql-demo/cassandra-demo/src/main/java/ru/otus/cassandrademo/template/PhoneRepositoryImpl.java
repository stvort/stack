package ru.otus.cassandrademo.template;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import lombok.RequiredArgsConstructor;
import ru.otus.cassandrademo.model.Phone;
import ru.otus.cassandrademo.model.SmartPhone;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static ru.otus.cassandrademo.schema.CassandraPhonesSchemaInitializer.FULL_PHONES_TABLE_NAME;

@RequiredArgsConstructor
public class PhoneRepositoryImpl implements PhoneRepository {
    private final CassandraConnection cassandraConnection;

    @Override
    public <T> void insert(T value, Class<T> tClass) {
        Session session = cassandraConnection.getSession();

        if (tClass.equals(Phone.class)) {

            Phone phone = (Phone) value;
            session.execute("INSERT INTO " + FULL_PHONES_TABLE_NAME + "(id, model, color, serialNumber) " +
                            "VALUES (?, ?, ?, ?)", phone.getId(), phone.getModel(), phone.getColor(),
                    phone.getSerialNumber());

        } else if (tClass.equals(SmartPhone.class)) {

            SmartPhone phone = (SmartPhone) value;
            session.execute("INSERT INTO " + FULL_PHONES_TABLE_NAME + "(id, model, color, serialNumber, operatingSystem) " +
                            "VALUES (?, ?, ?, ?, ?)", phone.getId(), phone.getModel(), phone.getColor(),
                    phone.getSerialNumber(), phone.getOperatingSystem());

        } else {
            throw new RuntimeException("Unsupported object class");
        }
    }

    @Override
    public <T> Optional<T> findOne(UUID id, Class<T> tClass) {
        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAll(Class<T> tClass) {
        if (!(tClass.equals(Phone.class) || tClass.equals(SmartPhone.class))) {
            throw new RuntimeException("Unsupported object class");
        }

        Session session = cassandraConnection.getSession();
        ResultSet resultSet = session.execute("SELECT * FROM " + FULL_PHONES_TABLE_NAME);

        List<Object> collect = resultSet.all().stream().map(row -> {
            UUID id = row.getUUID("id");
            String model = row.getString("model");
            String color = row.getString("color");
            String serialNumber = row.getString("serialNumber");
            String operatingSystem = row.getString("operatingSystem");

            if (tClass.equals(Phone.class)) {
                return new Phone(id, model, color, serialNumber);
            }
            return new SmartPhone(id, model, color, serialNumber, operatingSystem);
        }).collect(Collectors.toList());

        return (List<T>) collect;
    }

    
}

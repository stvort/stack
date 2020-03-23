package ru.otus.mongodemo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import lombok.val;
import ru.otus.mongodemo.model.Phone;
import ru.otus.mongodemo.model.SmartPhone;
import ru.otus.mongodemo.template.MongoTemplateImpl;

import java.util.Objects;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;


public class Demo {
  private static final String MONGODB_URL = "mongodb://localhost";
  private static final String MONGO_DATABASE_NAME = "mongo-db-test";

  private static final String PHONES_COLLECTION_NAME = "phones";

  public static void main(String[] args) throws Throwable {
    val motorolaC350 = new Phone(null, "C350", "silver", "000001");
    val motorolaZ800i = new Phone(null, "Z800i", "silver", "000002");
    val huaweiP20 = new SmartPhone(null, "p20", "black", "000003", "Android");

    try (val mongoClient = MongoClients.create(MONGODB_URL)) {

      val mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      val database = mongoClient.getDatabase(MONGO_DATABASE_NAME);
      val collection = database.getCollection(PHONES_COLLECTION_NAME);
      val mongoTemplate = new MongoTemplateImpl(collection, mapper);

      database.drop();

      var id = mongoTemplate.insert(motorolaC350);
      motorolaC350.set_id(id);

      id = mongoTemplate.insert(motorolaZ800i);
      motorolaZ800i.set_id(id);

      id = mongoTemplate.insert(huaweiP20);
      huaweiP20.set_id(id);

      System.out.println();

      val huaweiP20Optional = mongoTemplate.findOne(eq("_id", huaweiP20.get_id()), SmartPhone.class);
      huaweiP20Optional.ifPresent(sm -> System.out.printf("Smartphone from db is:\n%s", sm));

      System.out.println("\n");

      val allSilverPhones = mongoTemplate.find(eq("color", "silver"), Phone.class);
      System.out.println("All sliver phones from db:\n" + allSilverPhones.stream()
              .map(Objects::toString).collect(Collectors.joining("\n")));

      System.out.println();

      val allPhones = mongoTemplate.findAll(Phone.class);
      System.out.println("All phones from db:\n" + allPhones.stream()
              .map(Objects::toString).collect(Collectors.joining("\n")));

      System.out.println();
    }
  }
}

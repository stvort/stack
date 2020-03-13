package ru.otus.mongodemo;

import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static ru.otus.mongodemo.SubscriberHelpers.ObservableSubscriber;

/**
 * @author sergey
 * created on 16.10.18.
 * <p>
 * sudo service mongod insertAndFind
 * mongo
 * show dbs
 * use test
 * show collections
 * db.products.find();
 * db.products.find().pretty();
 * db.products.find({"item":"apple"}).pretty();
 * db.products.find({"item":"apple", "qty":11, "counter":19}).pretty();
 * db.products.insert({item: "apple", counter: -1});
 */
public class Demo {
  private static final Logger logger = LoggerFactory.getLogger(Demo.class);

  public static void main(String[] args) throws Throwable {
    new Demo().start();
  }

  private void start() throws Throwable {
    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost")) {
      MongoDatabase database = mongoClient.getDatabase("test");
      MongoCollection<Document> collection = database.getCollection("products");


       insertAndFind(collection);
     // writeAndRead(collection);
    }
  }

  private void insertAndFind(MongoCollection<Document> collection) throws Throwable {
    Document doc = new Document("key", System.currentTimeMillis())
        .append("item", "apple")
        .append("qty", 112);

    ObservableSubscriber<Success> subscriber = new ObservableSubscriber<>();
    collection.insertOne(doc).subscribe(subscriber);
    subscriber.await();

    ObservableSubscriber<Document> subscriberPrinter = new ObservableSubscriber<>();
    collection.find(new Document("item", "apple")).subscribe(subscriberPrinter);
    subscriberPrinter.await();
    List<Document> results = subscriberPrinter.getResult();
    logger.info("result.size:{}", results.size());
  }

  private void writeAndRead(MongoCollection<Document> collection) throws Throwable {
   // writer(collection);
    reader(collection);
  }

  private void writer(MongoCollection<Document> collection) {
    Thread thread = new Thread(() -> {
      try {
        int counter = 0;
        ObservableSubscriber<Success> subscriber = new ObservableSubscriber<>();
        while (true) {
          logger.info("counter:{}", counter);
          Document doc = new Document("key", System.currentTimeMillis())
              .append("item", "apple")
              .append("counter", counter++)
              .append("qty", 11);

          collection.insertOne(doc).subscribe(subscriber);
          subscriber.await();
          Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        }
      } catch (Throwable ex) {
        logger.error(ex.getMessage(), ex);
      }
    });

    thread.setName("Writer");
    thread.start();
  }

  private void reader(MongoCollection<Document> collection) throws Throwable {
    // Create the change stream publisher.
    ChangeStreamPublisher<Document> publisher = collection.watch();

    // Create a subscriber
    SubscriberHelpers.ObservableSubscriberChangeDocument subscriber = new SubscriberHelpers.ObservableSubscriberChangeDocument();
    publisher.subscribe(subscriber);

    subscriber.await();
  }
}

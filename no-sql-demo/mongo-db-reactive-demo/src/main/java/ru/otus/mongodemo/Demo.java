package ru.otus.mongodemo;

import com.mongodb.reactivestreams.client.ChangeStreamPublisher;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoCollection;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.mongodb.reactivestreams.client.Success;
import lombok.val;
import org.bson.Document;
import ru.otus.mongodemo.subscribers.ObservableSubscriber;
import ru.otus.mongodemo.subscribers.ObservableSubscriberChangeDocument;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Demo {

    //public static final String MONGODB_URL = "mongodb://localhost"; // Работа без DockerToolbox
    public static final String MONGODB_URL = "mongodb://192.168.99.100"; // Работа через DockerToolbox
    private static final String DB_NAME = "mongo-db-test";
    private static final String PRODUCTS_COLLECTION = "products";

    public static void main(String[] args) throws Throwable {
        try (MongoClient mongoClient = MongoClients.create(MONGODB_URL)) {
            MongoDatabase database = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Document> collection = database.getCollection(PRODUCTS_COLLECTION);

            System.out.println("\n");

            dropDatabase(database);

            //doInsertAndFindDemo(collection);
            doWriteAndReadDemo(collection);

            System.out.println("\n");
        }
    }

    private static void dropDatabase(MongoDatabase database) throws Throwable {
        val subscriber = new ObservableSubscriber<Success>(false);
        database.drop().subscribe(subscriber);
        subscriber.await();
    }


    private static void doInsertAndFindDemo(MongoCollection<Document> collection) throws Throwable {
        val doc = new Document("key", System.currentTimeMillis())
                .append("item", "apple")
                .append("qty", 112);

        System.out.println("Insert one document");
        val subscriber = new ObservableSubscriber<Success>();
        collection.insertOne(doc).subscribe(subscriber);
        subscriber.await();

        System.out.println();

        System.out.println("Find all apples");
        val subscriberPrinter = new ObservableSubscriber<Document>();
        collection.find(new Document("item", "apple")).subscribe(subscriberPrinter);
        subscriberPrinter.await();
        List<Document> results = subscriberPrinter.getResult();
        System.out.println(String.format("result.size: %d", results.size()));
    }

    private static void doWriteAndReadDemo(MongoCollection<Document> collection) throws Throwable {
        subscribeForCollectionChanges(collection);
        startWritingToCollectionInANewThread(collection);
    }

    private static void startWritingToCollectionInANewThread(MongoCollection<Document> collection) {
        val thread = new Thread(() -> {
            try {
                int counter = 0;
                val subscriber = new ObservableSubscriber<Success>();
                while (true) {
                    System.out.println(String.format("counter: %d", counter));
                    val doc = new Document("key", System.currentTimeMillis())
                            .append("item", "apple")
                            .append("counter", counter++)
                            .append("qty", 11);

                    collection.insertOne(doc).subscribe(subscriber);
                    subscriber.await();
                    Thread.sleep(TimeUnit.SECONDS.toMillis(10));
                }
            } catch (Throwable ex) {
                System.err.println(ex.getMessage());
            }
        });

        thread.setName("Writer");
        thread.start();
    }

    private static void subscribeForCollectionChanges(MongoCollection<Document> collection) throws Throwable {
        // Create the change stream publisher.
        ChangeStreamPublisher<Document> publisher = collection.watch();

        // Create a subscriber
        val subscriber = new ObservableSubscriberChangeDocument();
        publisher.subscribe(subscriber);

        subscriber.await();
    }
}

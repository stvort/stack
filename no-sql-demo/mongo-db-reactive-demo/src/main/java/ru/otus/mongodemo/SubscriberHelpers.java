package ru.otus.mongodemo;


import com.mongodb.MongoTimeoutException;
import com.mongodb.client.model.changestream.ChangeStreamDocument;
import com.mongodb.client.model.changestream.OperationType;
import org.bson.Document;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.util.concurrent.TimeUnit.MINUTES;


/**
 * @author sergey
 * created on 16.10.18.
 */
public class SubscriberHelpers {
  private static final Logger logger = LoggerFactory.getLogger(SubscriberHelpers.class);

  public static class ObservableSubscriber<T> implements Subscriber<T> {
    private final CountDownLatch latch = new CountDownLatch(1);
    private volatile Throwable error;
    private List<T> result = new ArrayList<>();

    @Override
    public void onSubscribe(Subscription subscription) {
      subscription.request(Integer.MAX_VALUE);
    }

    @Override
    public void onNext(T record) {
      result.add(record);
      logger.info("onNext, result: {}", record);
    }

    @Override
    public void onError(Throwable t) {
      error = t;
      logger.error(t.getMessage());
      onComplete();
    }

    @Override
    public void onComplete() {
      latch.countDown();
    }

    void await() throws Throwable {
      if (!latch.await(10, MINUTES)) {
        throw new MongoTimeoutException("Publisher timed out");
      }
      if (error != null) {
        throw error;
      }
    }

    List<T> getResult() {
      return result;
    }
  }

  public static class ObservableSubscriberChangeDocument extends ObservableSubscriber<ChangeStreamDocument<Document>> {

    @Override
    public void onNext(ChangeStreamDocument<Document> changedDocument) {
      Document document = changedDocument.getFullDocument();
      OperationType operation = changedDocument.getOperationType();
      logger.info("operation:{}, changed document: {}", operation, document);
    }
  }
}

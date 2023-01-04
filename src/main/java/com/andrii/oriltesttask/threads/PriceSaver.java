package com.andrii.oriltesttask.threads;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class PriceSaver extends Thread {
  private Logger logger = LoggerFactory.getLogger(getClass());
  private final String cryptocurrencyName;
  private final Integer threadSleepTime;
  private MongoClient mongoClient;
  private MongoDatabase database;
  private MongoCollection<Document> collection;

  public PriceSaver(String cryptocurrencyName, Integer threadSleepTimeInMillis) {
    this.cryptocurrencyName = cryptocurrencyName;
    this.threadSleepTime = threadSleepTimeInMillis;
    this.mongoClient = MongoClients.create("mongodb://localhost:27017");
    this.database = mongoClient.getDatabase("cryptocurrency-price");
    this.collection = database.getCollection("statistic");
  }

  private void priceSaver() {
    String URL = String.format("https://cex.io/api/last_price/%s/USD", cryptocurrencyName);

    try {
      URL BTCurl = new URL(URL);
      BufferedReader reader = new BufferedReader(new InputStreamReader(BTCurl.openStream()));
      String line = reader.readLine();
      JSONObject jsonObject = new JSONObject(line);
      Float price = jsonObject.getFloat("lprice");
      String name = jsonObject.get("curr1").toString();

      OffsetDateTime createdTimeStamp = OffsetDateTime.now(ZoneOffset.UTC);
      Document document = new Document("price", price)
          .append("cryptocurrencyName", name)
          .append("createdTime", createdTimeStamp.toLocalDateTime());


      collection.insertOne(document);

      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    logger.info("Parsing data started");
    while (true) {
      try {
        priceSaver();
        Thread.sleep(threadSleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}

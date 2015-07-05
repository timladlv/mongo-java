package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by timcarter on 05/07/2015.
 */
public class MongoAggregationExample {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("course");
        MongoCollection<Document> collection = database.getCollection("zipcodes");

        List<Document> pipeline;
        pipeline = Arrays.asList(
                new Document("$group",
                    new Document("_id", "$state")).append("totalPop", new Document("$sum", "$pop")),
                new Document("$match",
                        new Document("totalPop", new Document("$gte", "20000"))));
        List<Document> results = collection.aggregate(pipeline).into(new ArrayList<Document>());
        for (Document result : results) {
            System.out.println(result.toJson());
        }
    }
}

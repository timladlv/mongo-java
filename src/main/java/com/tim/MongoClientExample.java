package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timcarter on 07/06/2015.
 */
public class MongoClientExample {
    public static void main(String[] args) {
        // mongoClient heavy - make singleton
        MongoClient mongoClient = new MongoClient();
        // mongoDatabase and MongoCollection lightweight
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        MongoCollection<Document> collectionName = mongoDatabase.getCollection("names");
        collectionName.drop();

        Document tim = new Document(makeNameAgeMap("tim", 45));
        Document james = new Document(makeNameAgeMap("james", 10));
        collectionName.insertMany(Arrays.asList(tim, james));
        System.out.println(collectionName.find().first().toJson());

        printEverythingInCollectionThroughIteration(collectionName);
        printEverythingInCollectionThroughListTraversal(collectionName);
        printByFilterOnAge(collectionName, 10);

    }

    private static void printByFilterOnAge(MongoCollection<Document> collectionName, int age) {
        Bson ageFilter = Filters.eq("age", age);
        Bson projectionNameOnly = Projections.fields(Projections.include("name"), Projections.excludeId());
        Document tenYearOld = collectionName.find(ageFilter).projection(projectionNameOnly).first();
        System.out.println("age: " + age + " document: " + tenYearOld.toJson());
    }

    private static Map<String, Object> makeNameAgeMap(final String name, final int age) {
        Map<String, Object> answer = new HashMap<>();
        answer.put("name", name);
        answer.put("age", age);
        return answer;
    }

    private static void printEverythingInCollectionThroughIteration(MongoCollection<Document> collectionName) {
        Bson ageAscending = Sorts.ascending("age");
        FindIterable<Document> documents = collectionName.find().sort(ageAscending);
        MongoCursor<Document> iterator = documents.iterator();
        try {
            System.out.println("iteration ...");
            while (iterator.hasNext()) {
                Document next = iterator.next();
                System.out.println(next.toJson());
            }
        }
        finally {
            iterator.close();
        }
    }

    private static void printEverythingInCollectionThroughListTraversal(MongoCollection<Document> collectionName) {
        ArrayList<Document> into = collectionName.find().into(new ArrayList<Document>());
        System.out.println("for loop");
        for (Document document : into) {
            System.out.println(document.toJson());
        }
    }
}

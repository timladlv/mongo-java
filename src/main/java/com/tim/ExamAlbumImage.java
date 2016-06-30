package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by timcarter on 11/07/2015.
 */
public class ExamAlbumImage {
    public static void main(String[] args) {
        // mongoClient heavy - make singleton
        MongoClient mongoClient = new MongoClient();
        try {
            // mongoDatabase and MongoCollection lightweight
            MongoDatabase mongoDatabase = mongoClient.getDatabase("exam7");
            MongoCollection<Document> albums = mongoDatabase.getCollection("albums");
            MongoCollection<Document> images = mongoDatabase.getCollection("images");
            MongoCollection<Document> answerCollection = mongoDatabase.getCollection("answer");
            answerCollection.drop();
            Set<Integer> notOrphans = new HashSet<>();
            ArrayList<Document> albumList = albums.find().into(new ArrayList<Document>());
            for (Document album : albumList) {
                List<Integer> imageSet = album.get("images", List.class);
                notOrphans.addAll(imageSet);
            }
            System.out.println("notOrphans count: " + notOrphans.size());
            for (Integer id : notOrphans) {
                Bson equalsId = Filters.eq("_id", id);
                Document image = images.find(equalsId).first();
                answerCollection.insertOne(image);
            }
            System.out.println("answers written: " + answerCollection.count());
        } finally {
            mongoClient.close();
        }
    }
}

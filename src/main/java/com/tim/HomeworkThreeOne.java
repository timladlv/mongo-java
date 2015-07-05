package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by timcarter on 07/06/2015.
 */
public class HomeworkThreeOne {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        try {

            MongoDatabase mongoDatabase = mongoClient.getDatabase("school");
            MongoCollection<Document> studentsCollection = mongoDatabase.getCollection("students");
            MongoCollection<Document> answerCollection = mongoDatabase.getCollection("answer");
            answerCollection.drop();

            assert (200 == studentsCollection.count());
            assert (0 == answerCollection.count());

            List<Document> students = studentsCollection.find().into(new ArrayList<Document>());
            for (Document student : students) {
                List<Document> scores = student.get("scores", List.class);
                Double lowestScore = Double.MAX_VALUE;
                Document lowestHomework = null;
                for (Document score : scores) {
                    if ("homework".equals(score.getString("type"))) {
                        Double homeworkScore = score.getDouble("score");
                        if (homeworkScore < lowestScore) {
                            lowestScore = homeworkScore;
                            lowestHomework = score;
                        }
                    }
                }
                scores.remove(lowestHomework);
                student.put("scores", scores);
                System.out.println("adding to answer ...");
                answerCollection.insertOne(student);
            }
            assert (200 == answerCollection.count());
        } finally {
            mongoClient.close();
        }
    }
}
package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by timcarter on 07/06/2015.
 */
public class HomeworkTwoThree {
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient();
        try {

            MongoDatabase mongoDatabase = mongoClient.getDatabase("students");
            MongoCollection<Document> gradesCollection = mongoDatabase.getCollection("grades");
            MongoCollection<Document> answerCollection = mongoDatabase.getCollection("answer");
            answerCollection.drop();

            assert (800 == gradesCollection.count());
            assert (0 == answerCollection.count());

            Bson homeworkFilter = Filters.eq("type", "homework");
            Bson studentThenScore = Sorts.ascending("student_id", "score");
            List<Document> homeworkSorted = gradesCollection.find(homeworkFilter).sort(studentThenScore).into(new ArrayList<Document>());
            List<Document> goodHomework = new ArrayList<>();
            int currentStudent = -1;
            for (Document homework : homeworkSorted) {
                Integer studentId = homework.getInteger("student_id");
                if (studentId != currentStudent) {
                    currentStudent = studentId;
                } else {
                    goodHomework.add(homework);
                }
            }
            Bson nonHomeworkFilter = Filters.ne("type", "homework");
            answerCollection.insertMany(goodHomework);
            answerCollection.insertMany(gradesCollection.find(nonHomeworkFilter).into(new ArrayList<Document>()));
            assert (600 == answerCollection.count());
        } finally {
            mongoClient.close();
        }
    }
}
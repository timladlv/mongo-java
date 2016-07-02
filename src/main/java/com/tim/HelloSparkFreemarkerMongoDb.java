package com.tim;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.bson.Document;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.halt;

/**
 * Created by timcarter on 31/05/2015.
 */
public class HelloSparkFreemarkerMongoDb {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setClassForTemplateLoading(HelloSparkFreemarkerMongoDb.class, "/");

        MongoClient mongoClient = new MongoClient();
        MongoDatabase mongoDatabase = mongoClient.getDatabase("test");
        final MongoCollection<Document> collectionName = mongoDatabase.getCollection("names");
        collectionName.drop();
        collectionName.insertOne(new Document("name", "Tim from a MongoDoc"));

        Spark.get("/", (request, response) -> {
            final StringWriter output = new StringWriter();
            try {
                final Template template = configuration.getTemplate("hello.ftl");
                final Document document = collectionName.find().first();
                template.process(document, output);
            } catch (Exception e) {
                halt(500);
                e.printStackTrace();
            }
            return output;
        });
    }
}

package com.tim;

import freemarker.template.Configuration;
import freemarker.template.Template;
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
public class HelloSparkFreemarker {
    public static void main(String[] args) {
        final Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setClassForTemplateLoading(HelloSparkFreemarker.class, "/");

        Spark.get("/", (request, response) -> {
            StringWriter output = new StringWriter();
            try {
                Template template = configuration.getTemplate("hello.ftl");
                Map<String, Object> hello = new HashMap<>();
                hello.put("name", "Freemarker");
                template.process(hello, output);
            } catch (Exception e) {
                halt(500);
                e.printStackTrace();
            }
            return output;
        });
    }
}

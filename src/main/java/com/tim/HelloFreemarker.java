package com.tim;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by timcarter on 31/05/2015.
 */
public class HelloFreemarker {
    public static void main(String[] args) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setClassForTemplateLoading(HelloFreemarker.class, "/");
        try {
            Template template = configuration.getTemplate("hello.ftl");
            StringWriter stringWriter = new StringWriter();
            Map<String, Object> hello = new HashMap<>();
            hello.put("name", "Freemarker");
            template.process(hello, stringWriter);
            System.out.println(stringWriter.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

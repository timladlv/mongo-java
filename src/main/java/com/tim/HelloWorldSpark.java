package com.tim;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

/**
 * Created by timcarter on 31/05/2015.
 */
public class HelloWorldSpark {
    public static void main(String[] args) {
        Spark.get(new Route("/") {

            @Override
            public Object handle(Request request, Response response) {
                return "Hello Tim";
            }
        });
    }
}

package de.unisiegen.locationtools;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Created by brodo on 08.07.15.
 */
public class KMLRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        return "Test";
    }
}

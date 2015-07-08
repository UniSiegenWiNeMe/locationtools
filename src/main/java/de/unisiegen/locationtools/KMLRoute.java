package de.unisiegen.locationtools;

import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

/**
 * Created by brodo on 08.07.15.
 */
public class KMLRoute implements Route {
    @Override
    public Object handle(Request request, Response response) throws Exception {
        KMLParser parser = new KMLParser();
        HashMap<Long,Location> locations = parser.parseLocations(request.body());
        return locations.keySet().size() + " Locations added";
    }
}

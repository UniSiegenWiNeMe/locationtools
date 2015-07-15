package de.unisiegen.sensortools;

import de.unisiegen.sensortools.db.DataAdapter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

/**
 * Created by brodo on 08.07.15.
 */
public class KMLRoute implements Route {
    private final DataAdapter adapter;
    public KMLRoute(DataAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        KMLParser parser = new KMLParser();
        HashMap<Long,Location> locations = parser.parseLocations(request.body());
        adapter.saveLocations((String)request.queryParams("user"), (String)request.queryParams("namespace"), locations);
        return locations.keySet().size() + " Locations added";
    }
}

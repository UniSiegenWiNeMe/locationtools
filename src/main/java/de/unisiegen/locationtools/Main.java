package de.unisiegen.locationtools;

import de.unisiegen.locationtools.db.DataAdapter;
import de.unisiegen.locationtools.db.InfluxConnector;

import static spark.Spark.*;

/**
 * Created by brodo on 08.07.15.
 */
public class Main {
    public static void main(String[] args) {
        DataAdapter influxDb = new InfluxConnector();
        influxDb.openDB();

        get("/hello", (req, res) -> "Hello World");
        post("/kml", new KMLRoute(influxDb));
        get("/testFake", new TestClusterRoute());
    }

}

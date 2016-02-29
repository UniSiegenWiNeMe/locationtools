package de.unisiegen.sensortools;

import de.unisiegen.sensortools.db.DataAdapter;
import de.unisiegen.sensortools.db.InfluxConnector;

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
        post("/cluster", new ClusterRoute());
        post("/testFake", new TestClusterRoute(influxDb));
        post("/testTime", new TestTimeRoute(influxDb));
        get("/testPatterns", new TestEventPatternRoute());
    }

}

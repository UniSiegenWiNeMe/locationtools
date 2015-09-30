package de.unisiegen.sensortools;

import de.unisiegen.sensortools.db.DataAdapter;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.HashMap;

/**
 * Created by Nico on 30.09.15.
 */
public class ClusterRoute implements Route {

    public ClusterRoute() {

    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        String clusterBool = request.queryParams("cluster");
        System.out.println("Cluster? " + clusterBool);

        String normalizedBool = request.queryParams("normalized");
        System.out.println("Normalized? " + normalizedBool);

        String normalizedMethod = request.queryParams("normalizedMethod");
        System.out.println("Norm. Method? " + normalizedMethod);

        return "Cluster";
    }
}

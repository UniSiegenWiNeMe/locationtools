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
        boolean clusterBool = Boolean.valueOf(request.queryParams("cluster"));
        boolean normalizedBool = Boolean.valueOf(request.queryParams("normalized"));
        int normalizedMethod = Integer.valueOf(request.queryParams("normalizedMethod"));

        String bufferedReaders = "";

        if(normalizedBool && clusterBool) {
            switch(normalizedMethod) {
            case(0): //Methode Rescaling
                bufferedReaders = ""; //Norm
                bufferedReaders = ""; //Cluster
                bufferedReaders = ""; //Denorm
                break;
            case(1): //Methode Standarized
                bufferedReaders = ""; //Norm
                bufferedReaders = ""; //Cluster
                bufferedReaders = ""; //Denorm
                break;
            default:
                bufferedReaders = ""; //Norm
                bufferedReaders = ""; //Cluster
                bufferedReaders = ""; //Denorm
                break;
            }
        } if(clusterBool) {
            bufferedReaders = ""; //Cluster
        } else {
            bufferedReaders = ""; //Norm
        }

        System.out.println("Cluster? " + clusterBool);
        System.out.println("Normalized? " + normalizedBool);
        System.out.println("Norm. Method? " + normalizedMethod);
        return "Cluster";
    }
}

package de.unisiegen.locationtools;

import de.unisiegen.locationtools.cluster.ClusterManagement;
import de.unisiegen.locationtools.cluster.UserLocation;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.Date;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestClusterRoute implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        Double [][] locations = new Double[10000][2];
        int x = (int) (50.0*1000000.0);
        int y = (int) (8.0*1000000.0);

        for(int i=0;  i<1000000; i++){
            double random = Math.random();
            double random2 = Math.random();

            Location loc = new Location(Location.LocationType.ADDRESS, x+((int)(random*1000000)),y+((int)(random2*1000000)));
            UserLocation uloc = new UserLocation(loc,new Date().getTime(), -1);
        }

        return null;
    }
}

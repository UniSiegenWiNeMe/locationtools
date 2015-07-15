package de.unisiegen.locationtools;

import de.unisiegen.locationtools.cluster.ClusterManagement;
import de.unisiegen.locationtools.cluster.ClusteredLocation;
import de.unisiegen.locationtools.cluster.UserLocation;
import de.unisiegen.locationtools.db.DataAdapter;
import net.sf.javaml.core.Dataset;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestClusterRoute implements Route {
    private ArrayList<UserLocation> ulocs = getFakeLocaction();
    private DataAdapter myAdapter = new DataAdapter() {
        @Override
        public void openDB() {
            ulocs = getFakeLocaction();
        }

        @Override
        public void closeDB() {

        }

        @Override
        public Location saveLocation(String user, String namespace, Location loc, long timeStamp) {
            return null;
        }

        @Override
        public Location saveLocation(String user, String namespace, Location loc) {
            return null;
        }

        @Override
        public void saveLocations(String user, String namespace, Map<Long, Location> locations) {

        }

        @Override
        public ClusteredLocation saveClusterLocation(String user, String namespace, Location loc) {
            return null;
        }

        @Override
        public ClusteredLocation saveClusterLocation(String user, String namespace, Location loc, long timestamp) {
            return null;
        }

        @Override
        public ClusteredLocation saveClusterLocation(String user, String namespace, Location loc, Dataset ds) {
            return null;
        }

        @Override
        public ClusteredLocation updateClusteredLocation(String user, String namespace, ClusteredLocation updatedLoc) {
            return null;
        }

        @Override
        public ClusteredLocation updateClusteredLocation(String user, String namespace, ClusteredLocation updatedLoc, Dataset ds) {
            return null;
        }

        @Override
        public void setClusterIDOfLocations(String user, Dataset ds, String namespace, long id) {

        }

        @Override
        public void clearLocationHistory(String user, long since, String namespace, long until) {

        }

        @Override
        public void clearClusteredLocations(String user, long since, String namespace, long until) {

        }

        @Override
        public List<ClusteredLocation> getAllClusterLocs(String user, String namespace) {
            return null;
        }

        @Override
        public List<UserLocation> getAllHistoryLocs(String user, String namespace,long since, long until, boolean timedescending, boolean onlyUnclustered) {
            return ulocs;
        }

        @Override
        public List<UserLocation> getUnclusteredHistoryLocs(String user, String namespace, long since, long until) {
            return null;
        }

    };

    @Override
    public Object handle(Request request, Response response) throws Exception {
        HashMap<Location,Dataset> clusters = ClusterManagement.clusterLocations(myAdapter,new Date(0), new Date(),null,false);
        String x = "Clusters found: " + clusters.size();
        for(Location loc: clusters.keySet()){
            x+="\n"+loc.lat+ " "+ loc.lon + " Location belonging to cluster:" + clusters.get(loc).size();
        }
        return x;
    }

    private ArrayList<UserLocation> getFakeLocaction(){
        ArrayList<UserLocation> ulocs = new ArrayList<UserLocation>();
        Double [][] locations = new Double[10000][2];
        int x = (int) (50.0*1000000.0);
        int y = (int) (8.0*1000000.0);

        for(int i=0;  i<1000; i++){
            double random = Math.random();
            double random2 = Math.random();

            Location loc = new Location(Location.LocationType.ADDRESS, x+((int)(random*1000)),y+((int)(random2*1000)));
            UserLocation uloc = new UserLocation(loc,new Date().getTime(), -1);
            ulocs.add(uloc);
        }

        return ulocs;
    }
}

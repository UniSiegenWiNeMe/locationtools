package de.unisiegen.locationtools;

import de.unisiegen.locationtools.cluster.ClusterManagement;
import de.unisiegen.locationtools.cluster.ClusteredLocation;
import de.unisiegen.locationtools.cluster.UserLocation;
import de.unisiegen.locationtools.db.DataAdapter;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DenseInstance;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestClusterRoute implements Route {
    private ArrayList<UserLocation> ulocs = getFakeLocaction();
    private DataAdapter myAdapter = new DataAdapter() {
        @Override
        public void openDB() {

        }

        @Override
        public void closeDB() {

        }

        @Override
        public Location saveLocation(Location loc, long timeStamp) {
            return null;
        }

        @Override
        public Location saveLocation(Location loc) {
            return null;
        }

        @Override
        public void saveLocations(Map<Long, Location> locations) {

        }

        @Override
        public ClusteredLocation saveClusterLocation(Location loc) {
            return null;
        }

        @Override
        public ClusteredLocation saveClusterLocation(Location loc, long timestamp) {
            return null;
        }

        @Override
        public ClusteredLocation saveClusterLocation(Location loc, Dataset ds) {
            return null;
        }

        @Override
        public ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc) {
            return null;
        }

        @Override
        public ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc, Dataset ds) {
            return null;
        }

        @Override
        public void setClusterIDOfLocations(Dataset ds, long id) {

        }

        @Override
        public void clearLocationHistory(long since, long until) {

        }

        @Override
        public void clearClusteredLocations(long since, long until) {

        }

        @Override
        public List<ClusteredLocation> getAllClusterLocs() {
            return null;
        }

        @Override
        public List<UserLocation> getAllHistoryLocs(long since, long until, boolean timedescending, boolean onlyUnclustered) {
            return null;
        }

        @Override
        public List<UserLocation> getUnclusteredHistoryLocs(long since, long until) {
            return null;
        }
    };

    @Override
    public Object handle(Request request, Response response) throws Exception {

        return ClusterManagement.clusterLocations(myAdapter,new Date(0), new Date(),null,false);
    }
    private ArrayList<UserLocation> getFakeLocaction(){
        ArrayList<UserLocation> ulocs = new ArrayList<UserLocation>();
        Double [][] locations = new Double[10000][2];
        int x = (int) (50.0*1000000.0);
        int y = (int) (8.0*1000000.0);

        for(int i=0;  i<1000000; i++){
            double random = Math.random();
            double random2 = Math.random();

            Location loc = new Location(Location.LocationType.ADDRESS, x+((int)(random*1000000)),y+((int)(random2*1000000)));
            UserLocation uloc = new UserLocation(loc,new Date().getTime(), -1);
            ulocs.add(uloc);
        }

        return ulocs;
    }
}

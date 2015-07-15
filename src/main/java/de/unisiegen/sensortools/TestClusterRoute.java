package de.unisiegen.sensortools;

import de.unisiegen.sensortools.cluster.ClusterManagement;
import de.unisiegen.sensortools.cluster.distanceMeasures.PowerDistance;
import de.unisiegen.sensortools.cluster.sensors.PowerMeasurement;
import de.unisiegen.sensortools.cluster.sensors.UserLocation;
import de.unisiegen.sensortools.db.DataAdapter;
import net.sf.javaml.clustering.DensityBasedSpatialClustering;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import org.xml.sax.SAXException;
import spark.Request;
import spark.Response;
import spark.Route;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestClusterRoute implements Route {
    private ArrayList<UserLocation> ulocs;
    private DataAdapter myAdapter ;/*= new DataAdapter() {
        @Override
        public void openDB() {

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
*/


    public TestClusterRoute(DataAdapter influxConnector){
        myAdapter = influxConnector;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //ulocs = getFakeLocaction(request);
/*
        HashMap<Location,Dataset> clusters = ClusterManagement.clusterLocations(myAdapter,new Date(0), new Date(),null,false);
        String x = "Clusters found: " + clusters.size();
        for(Location loc: clusters.keySet()){
            x+="\n"+loc.lat+ " "+ loc.lon + " Location belonging to cluster:" + clusters.get(loc).size();
            x+="\nhttp://maps.google.com/?ie=UTF8&hq=&ll="+((double)loc.lat)/1000000 +","+((double)loc.lon)/1000000+"&z=13";
        }
        return x;*/

        List<PowerMeasurement> powerValues = myAdapter.getHistoryConsumption("namespace", "user", "Papier Drucker 3D Raum", 0, 0);

        Dataset data = new DefaultDataset();
        for(int i = 0;i<powerValues.size();i++) {
            data.add(powerValues.get(i));
        }

        Dataset[] resultsCluster;
        DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(10, 5, new PowerDistance());
        resultsCluster = clusterer.cluster(data);
        return resultsCluster;
    }

    private ArrayList<UserLocation> getFakeLocaction(Request request){
        KMLParser parser = new KMLParser();
        HashMap<Long,Location> ulocs = null;
        ArrayList<UserLocation> res = new ArrayList<>();
        try {
            ulocs = parser.parseLocations(request.body());
            //ArrayList<UserLocation> ulocs = new ArrayList<UserLocation>();
            Double [][] locations = new Double[10000][2];
            int x = (int) (50.0*1000000.0);
            int y = (int) (8.0*1000000.0);
            Iterator<Location> it = ulocs.values().iterator();
            while(it.hasNext()){
                Location loc = it.next();
                UserLocation uloc = new UserLocation(loc,new Date().getTime(), -1);
                res.add(uloc);
            }
            return res;
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}

package de.unisiegen.sensortools;

import de.unisiegen.sensortools.cluster.ClusterManagement;
import de.unisiegen.sensortools.cluster.TimeClusterResult;
import de.unisiegen.sensortools.cluster.distanceMeasures.PowerDistance;
import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import de.unisiegen.sensortools.cluster.sensors.PowerMeasurement;
import de.unisiegen.sensortools.cluster.sensors.UserLocation;
import de.unisiegen.sensortools.db.DataAdapter;
import net.sf.javaml.clustering.DensityBasedSpatialClustering;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import org.eclipse.jetty.server.Authentication;
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
public class TestTimeRoute implements Route {
    private ArrayList<UserLocation> ulocs;
    private DataAdapter myAdapter ;



    public TestTimeRoute(DataAdapter influxConnector){
        myAdapter = influxConnector;
    }
    @Override
    public Object handle(Request request, Response response) throws Exception {
        //ulocs = getFakeLocaction(request);

        HashMap<Location,Dataset> clusters = ClusterManagement.clusterLocations(myAdapter,new Date(0), new Date(),null,false);
        String resultString = "Clusters found: " + clusters.size();
        for(Location loc: clusters.keySet()){
            resultString+="\n"+loc.lat+ " "+ loc.lon + " Location belonging to cluster:" + clusters.get(loc).size();
            resultString+="\nhttp://maps.google.com/?ie=UTF8&hq=&ll="+((double)loc.lat)/1000000 +","+((double)loc.lon)/1000000+"&z=13";
        }
        Dataset data = clusters.get(clusters.keySet().iterator().next());
            LinkedList<AbstractMeasurement> list = new LinkedList<AbstractMeasurement>();
            for(Instance instance:data){
                AbstractMeasurement am = (AbstractMeasurement)instance;
                list.add(am);
            }
            resultString+="\n\n";
            List<TimeClusterResult> timeresults = ClusterManagement.clusterTime(list);
            for(TimeClusterResult tcr:timeresults){
                resultString+= "Start at: " + tcr.start.toLocaleString() + "End at: "+ tcr.end.toLocaleString()+ "\n";
            }


        return resultString;
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

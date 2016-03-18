package de.unisiegen.sensortools;

import de.unisiegen.sensortools.cluster.ClusterManagement;
import de.unisiegen.sensortools.cluster.ClusterResult;
import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import de.unisiegen.sensortools.cluster.sensors.PowerMeasurement;
import de.unisiegen.sensortools.cluster.sensors.SHSensorEvent;
import de.unisiegen.sensortools.cluster.sensors.UserLocation;
import de.unisiegen.sensortools.db.DataAdapter;
import de.unisiegen.sensortools.db.InfluxConnector;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestEventEnergyRoute implements Route {
    private ArrayList<UserLocation> ulocs;
    private String url;



    @Override
    public Object handle(Request request, Response response) throws Exception {
        InfluxConnector ic = new InfluxConnector();
        ic.openDB();
        List<ClusterResult> cluster = ClusterManagement.clusterEnergy(ic);
        String resultString="";
        for(ClusterResult tcr:cluster){
            resultString+= "Start at: " + tcr.start.toLocaleString() + "\tEnd at: "+ tcr.end.toLocaleString();
            double min = Double.MAX_VALUE;
            double max = 0;
            double meansum=0;
            for(AbstractMeasurement mesurement:tcr.data){
                PowerMeasurement se = (PowerMeasurement) mesurement;
                min = Math.min(min, Double.valueOf(se.getValue("POWER")));
                max = Math.max(max, Double.valueOf(se.getValue("POWER")));
                meansum+=Double.valueOf(se.getValue("POWER"));
            }
            resultString +="\tMin:"+min+"\tMax:"+max+"\tmean:"+meansum/tcr.data.size()+"\n";
        }
        return resultString;
    }


}

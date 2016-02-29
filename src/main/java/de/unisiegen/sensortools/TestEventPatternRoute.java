package de.unisiegen.sensortools;

import de.unisiegen.sensortools.cluster.ClusterManagement;
import de.unisiegen.sensortools.cluster.ClusterResult;
import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import de.unisiegen.sensortools.cluster.sensors.UserLocation;
import de.unisiegen.sensortools.db.DataAdapter;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;
import org.xml.sax.SAXException;
import spark.Request;
import spark.Response;
import spark.Route;
import weka.core.parser.JFlex.StdOutWriter;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.*;

/**
 * Created by Martin on 08.07.2015.
 */
public class TestEventPatternRoute implements Route {
    private ArrayList<UserLocation> ulocs;
    private String url;



    @Override
    public Object handle(Request request, Response response) throws Exception {
        String url = request.queryParams("source");
        System.out.println(url);
        List<ClusterResult> cluster = ClusterManagement.clusterPatterns(url);
        return cluster;
    }


}

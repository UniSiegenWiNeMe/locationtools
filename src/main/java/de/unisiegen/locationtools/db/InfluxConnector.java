package de.unisiegen.locationtools.db;

import de.unisiegen.locationtools.Location;
import de.unisiegen.locationtools.cluster.ClusteredLocation;
import de.unisiegen.locationtools.cluster.UserLocation;

import net.sf.javaml.core.Dataset;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Created by lars on 08/07/15.
 */
public class InfluxConnector implements DataAdapter {
    private String dbURL="http://141.99.14.50:8086";
    private String dbUser = "root";
    private String dbPassword = "root";
    private InfluxDB influxDB;
    private String dbName = "locations";


    public InfluxConnector(String dbURL, String dbUser, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        String dbName = "locations";
    }

    public InfluxConnector(){
    }

    public void finalize() {
    }


    public void openDB() {
        influxDB = InfluxDBFactory.connect(dbURL, dbUser, dbPassword);
        if( ! influxDB.describeDatabases().contains(dbName)){
            influxDB.createDatabase(dbName);
        }
    }

    public void closeDB() {
        influxDB = null;
    }

    /** Fixme: add user and namespace to */
    @Override
    public void saveLocations(String user,String namespace,Map<Long, Location> locations) {

        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .retentionPolicy("default")
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();

        for (Map.Entry<Long, Location> entry : locations.entrySet())
        {
            Point point1 = Point.measurement("Loci")
                    .time(entry.getKey(), TimeUnit.MILLISECONDS)
                    .field("lat", entry.getValue().lat).field("long", entry.getValue().lon).tag("namespace", namespace).tag("user", user)
                    .build();

            batchPoints.point(point1);
        }



        influxDB.write(batchPoints);

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

    /** Fixme: user and namespace are new. */
    @Override
    public List<ClusteredLocation> getAllClusterLocs(String user, String namespace) {

    Query query = new Query("SELECT * FROM locations WHERE user = '" + user + "' AND namespace = '" + namespace + "'", dbName);
    QueryResult queryresult = influxDB.query(query);
    // queryresult.getResults().get(0).getSeries();
    //queryresult.getResults().stream().map((Function<QueryResult.Result, Void>) System.err::println);
        System.out.println(queryresult);
        return null;
    }

    @Override
    public List<UserLocation> getAllHistoryLocs(String user,String namespace, long since, long until, boolean timedescending, boolean onlyUnclustered) {
        return null;
    }

    @Override
    public List<UserLocation> getUnclusteredHistoryLocs(String user, String namespace, long since, long until) {
        return null;
    }






}

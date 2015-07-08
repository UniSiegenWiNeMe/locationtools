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
import spark.utils.Assert;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lars on 08/07/15.
 */
public class InfluxConnector implements DataAdapter {
    private String dbURL="http://141.99.14.50:8086";
    private String dbUser = "root";
    private String dbPassword = "root";
    private InfluxDB influxDB;
    private String dbName;


    InfluxConnector(String dbURL, String dbUser, String dbPassword) {
        this.dbURL = dbURL;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
        String dbName = "locations";
    }

    InfluxConnector(){
    }

    public void finalize(){
        closeDB();
    }




    public void openDB() {
        influxDB = InfluxDBFactory.connect(dbURL, dbUser, dbPassword);
        if( ! influxDB.describeDatabases().contains(dbName)){
            influxDB.createDatabase(dbName);
        }
    }

    public void closeDB() {}

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
        BatchPoints batchPoints = BatchPoints
                .database(dbName)
                .retentionPolicy("default")
                .consistency(InfluxDB.ConsistencyLevel.ALL)
                .build();

        //loop

        Iterator it = locations.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }

        Point point1 = Point.measurement("Test")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .field("value", 0.66)
                .build();

        batchPoints.point(point1);

        influxDB.write(batchPoints);
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
        InfluxDB influxDB = InfluxDBFactory.connect("http://141.99.14.50:8086", "root", "root");
        String dbName = "locations";

        Query query = new Query("SELECT * FROM locations", dbName);
        QueryResult queryresult = influxDB.query(query);



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
}

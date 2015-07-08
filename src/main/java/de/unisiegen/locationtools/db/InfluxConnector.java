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

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by lars on 08/07/15.
 */
public class InfluxConnector implements DataAdapter {
    String dbURL="http://localhost:8086";

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
}

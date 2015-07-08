package de.unisiegen.locationtools.db;

import de.unisiegen.locationtools.Location;
import de.unisiegen.locationtools.cluster.ClusteredLocation;
import de.unisiegen.locationtools.cluster.UserLocation;
import net.sf.javaml.core.Dataset;

import java.util.List;

/**
 * Created by Martin on 08.07.2015.
 */
public interface DataAdapter {

        void openDB();
        void closeDB();
        Location saveLocation(Location loc, long timeStamp);
        Location saveLocation(Location loc);
        ClusteredLocation saveClusterLocation(Location loc);
        ClusteredLocation saveClusterLocation(Location loc, Dataset ds);
        ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc);
        ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc, Dataset ds);
        void setClusterIDOfLocations(Dataset ds, long id);
        void clearLocationHistory(long since, long until);
        void clearClusteredLocations(long since, long until);
        List<ClusteredLocation> getAllClusterLocs();
        List<UserLocation> getAllHistoryLocs(long since, long until, boolean timedescending, boolean onlyUnclustered);
        List<UserLocation> getUnclusteredHistoryLocs(long since, long until);


}
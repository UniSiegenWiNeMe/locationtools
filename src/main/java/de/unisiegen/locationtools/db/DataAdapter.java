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



        public void openDB();
        public void closeDB();
        public Location saveLocation(Location loc,long timeStamp);
        public Location saveLocation(Location loc );
        public ClusteredLocation saveClusterLocation(Location loc);
        public ClusteredLocation saveClusterLocation(Location loc, Dataset ds);
        public ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc);
        public ClusteredLocation updateClusteredLocation(ClusteredLocation updatedLoc, Dataset ds);
        public void setClusterIDOfLocations(Dataset ds, long id);
        public void clearLocationHistory(long since, long until);
        public void clearClusteredLocations(long since, long until);
        public List<ClusteredLocation> getAllClusterLocs();
        public List<UserLocation> getAllHistoryLocs(long since, long until, boolean timedescending, boolean onlyUnclustered);
        public List<UserLocation> getUnclusteredHistoryLocs(long since, long until);



}

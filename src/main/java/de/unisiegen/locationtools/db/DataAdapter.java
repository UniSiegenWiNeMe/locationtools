package de.unisiegen.locationtools.db;

import de.unisiegen.locationtools.Location;
import de.unisiegen.locationtools.cluster.ClusteredLocation;
import de.unisiegen.locationtools.cluster.UserLocation;
import net.sf.javaml.core.Dataset;

import java.util.List;
import java.util.Map;

/**
 * Created by Martin on 08.07.2015.
 */
public interface DataAdapter {



        public void openDB();
        public void closeDB();
        public Location saveLocation(String user,String namespace, Location loc,long timeStamp);
        public Location saveLocation(String user,String namespace,Location loc );
        public void saveLocations(String user,String namespace,Map<Long, Location> locations);
        public ClusteredLocation saveClusterLocation(String user,String namespace,Location loc);
        public ClusteredLocation saveClusterLocation(String user,String namespace,Location loc, long timestamp);
        public ClusteredLocation saveClusterLocation(String user,String namespace,Location loc, Dataset ds);
        public ClusteredLocation updateClusteredLocation(String user,String namespace,ClusteredLocation updatedLoc);
        public ClusteredLocation updateClusteredLocation(String user,String namespace,ClusteredLocation updatedLoc, Dataset ds);
        public void setClusterIDOfLocations(String user,Dataset ds,String namespace, long id);
        public void clearLocationHistory(String user,long since,String namespace, long until);
        public void clearClusteredLocations(String user,long since,String namespace, long until);
        public List<ClusteredLocation> getAllClusterLocs(String user,String namespace);
        public List<UserLocation> getAllHistoryLocs(String user,String namespace,long since, long until, boolean timedescending, boolean onlyUnclustered);
        public List<UserLocation> getUnclusteredHistoryLocs(String user,String namespace,long since, long until);



}

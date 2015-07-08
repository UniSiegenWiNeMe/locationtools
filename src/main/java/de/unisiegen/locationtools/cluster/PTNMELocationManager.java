package de.unisiegen.locationtools.cluster;

import de.unisiegen.locationtools.Location;
import de.unisiegen.locationtools.LocationUtils;

import java.util.Vector;


public class PTNMELocationManager {

    static boolean useFallBackLocation = false;
    static Location lastLocation = null;
    static long	lastLocationReceivedAt = 0;
    static Vector<LocationListener> listener;

    public static boolean isUseFallBackLocation() {
        return useFallBackLocation;
    }

    public static void setUseFallBackLocation(boolean useFallBackLocation) {
        PTNMELocationManager.useFallBackLocation = useFallBackLocation;
    }

    public static void registerListener(LocationListener x){
        if (listener == null) listener = new Vector<LocationListener>();
        listener.add(x);
    }

    public static void unregisterListener(LocationListener x){
        if (listener != null){
            listener.remove(x);
        }
    }

    /* TODO Location aus Profile clearen
    public static void clearHistory(){
		Utilities.openDBConnection().clearSearchHistory();
	}*/

    public static double computeDistance(Location loc1, Location loc2){
        return computeDistance(((double)(loc1.lat))/1000000,((double)(loc1.lon))/1000000, ((double)(loc2.lat))/1000000,((double)(loc2.lon))/1000000);
    }

    public static double computeDistance(Location loc1, double lat2, double lon2){
        return computeDistance(((double)(loc1.lat))/1000000,((double)(loc1.lon))/1000000, lat2,lon2);
    }

    public static double computeDistance(double lat1,double lon1, double lat2, double lon2){
        return LocationUtils.computeDistance(lat1, lon1, lat2, lon2);
    }


    public static long getLastLocationReceivedAt() {
        return lastLocationReceivedAt;
    }

    public static void setLastLocationReceivedAt(long lastLocationReceivedAt) {
        PTNMELocationManager.lastLocationReceivedAt = lastLocationReceivedAt;
    }


    public static interface LocationListener{
        public void onNewLocationFound(Location x);
    }

}

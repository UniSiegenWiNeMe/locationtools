package de.unisiegen.locationtools.cluster;

import java.util.Date;
import java.util.Vector;

import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.LocationUtils;
import de.schildbach.pte.dto.Location;
import de.schildbach.pte.dto.LocationType;

public class PTNMELocationManager {

	private static LocationService service;
    static boolean useFallBackLocation = false;
    static Location lastLocation = null;
    static long	lastLocationReceivedAt = 0;
    static Vector<LocationListener> listener;
    private static SharedPreferences prefs;
    private static SharedPreferences.Editor editor;


    public static LocationService getService() {
		return service;
	}
	public static void setService(LocationService svc) {
		service = svc;
	}

	public static void startService(){
		Utilities.getContext().startService(new Intent(Utilities.getContext(), LocationService.class));
	}
	
	public static void forceLocationUpdate(boolean enable){
		if(service!=null && enable){
			service.forceLocationUpdateSetting();
		}else{
			if(service!=null) service.setNormalInterval();
		}
	}


    public static Location getPTLocation(double lat, double lng){

        Location loc1;

            loc1 = new Location(LocationType.ADDRESS,(int)(lat*1000000), (int)(lng*1000000));

        return loc1;
    }

    public static Location getLocation(boolean update){
        if(prefs==null){
            prefs= PreferenceManager.getDefaultSharedPreferences(Utilities.getContext());
        }
        if(lastLocation==null){

            lastLocationReceivedAt = prefs.getLong(LocationReceiver.LAST_POISITION_UPDATE, 0);
            lastLocation = createNowLocation(null);

        }
        if(update){
            forceLocationUpdate(true);
        }
        if(lastLocation==null || (lastLocation!=null && (lastLocation.lat ==0 && lastLocation.lon==0))){
            return null;
        }else{
            return lastLocation;
        }



    }

    public static Location getLocation(){
        return getLocation(true);
    }

    public static double[] getLocationLatLng(boolean update){
        Location x;
        if(!update){
            x = getLocation(false);
            if(x !=null)return new double[]{((double)x.lat)/1000000.0,((double)x.lon)/1000000.0};
        }else{
            x = getLocation(true);
            if(x !=null)return new double[]{((double)x.lat)/1000000.0,((double)x.lon)/1000000.0};
        }
        return null;

    }

    public static double[] getLocationLatLng(){
        return getLocationLatLng(true);
    }

    public static void refreshLocation(){
        forceLocationUpdate(true);
    }

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

    public static void clearHistory(){
		Utilities.openDBConnection().clearSearchHistory();
	}

    public static double computeDistance(Location loc1, Location loc2){
        return computeDistance(((double)(loc1.lat))/1000000,((double)(loc1.lon))/1000000, ((double)(loc2.lat))/1000000,((double)(loc2.lon))/1000000);
    }

    public static double computeDistance(Location loc1, double lat2, double lon2){
        return computeDistance(((double)(loc1.lat))/1000000,((double)(loc1.lon))/1000000, lat2,lon2);
    }

    public static double computeDistance(double lat1,double lon1, double lat2, double lon2){
        return LocationUtils.computeDistance(lat1, lon1, lat2, lon2);
    }

    public static double computeDistance(android.location.Location loc1,android.location.Location loc2){
        return computeDistance(loc1.getLatitude(),loc1.getLongitude(),loc2.getLatitude(),loc2.getLongitude());
    }

    public static long getLastLocationReceivedAt() {
        return lastLocationReceivedAt;
    }

    public static void setLastLocationReceivedAt(long lastLocationReceivedAt) {
        PTNMELocationManager.lastLocationReceivedAt = lastLocationReceivedAt;
    }

    public static void notifyListener(Location loc){
		lastLocationReceivedAt = new Date().getTime();
        lastLocation = createNowLocation(loc);
		if(listener !=null){
        	for (LocationListener x: listener){
        		x.onNewLocationFound(lastLocation);
        	}
        }
	}
    public static interface LocationListener{
        public void onNewLocationFound(Location x);
    }
    private static Location createNowLocation(Location loc){
        String time= (new Date().getTime()-lastLocationReceivedAt)/(1000l*60l)<10 ? ("\n"+(new Date().getTime()-lastLocationReceivedAt)/(1000l*60l)+" min"):(">10 min");
        if(loc ==null){
            return new Location(LocationType.ADDRESS,null, prefs.getInt(LocationReceiver.LAST_LAT, 0), prefs.getInt(LocationReceiver.LAST_LON, 0),"Hier", "Hier"+time);
        }
        return new Location(LocationType.ADDRESS,null, loc.lat, loc.lon,"Hier", "Hier"+time);

    }
}

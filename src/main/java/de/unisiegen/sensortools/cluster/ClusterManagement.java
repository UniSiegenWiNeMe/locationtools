package de.unisiegen.sensortools.cluster;




import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import de.unisiegen.sensortools.Location;
import de.unisiegen.sensortools.cluster.distanceMeasures.LocationDistanceMeasure;
import de.unisiegen.sensortools.cluster.distanceMeasures.PowerDistance;
import de.unisiegen.sensortools.cluster.distanceMeasures.SHEventDistanceMeasure;
import de.unisiegen.sensortools.cluster.distanceMeasures.TimeDistanceMeasure;
import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import de.unisiegen.sensortools.cluster.sensors.PowerMeasurement;
import de.unisiegen.sensortools.cluster.sensors.SHSensorEvent;
import de.unisiegen.sensortools.cluster.sensors.UserLocation;
import de.unisiegen.sensortools.db.DataAdapter;
import net.sf.javaml.clustering.DensityBasedSpatialClustering;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.Instance;
import org.json.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.*;
import java.net.URL;

/**
 * Created by Martin on 05.03.2015.
 */
public class ClusterManagement {

    private static Dataset[] resultsCluster;
    private static HashMap<Long, HashMap<Long, Double>> probresult = null;

    public static HashMap<Location, Dataset> clusterLocations(DataAdapter adapter, Date since, Date until, Integer max, boolean onlyUnClustered) {
        HashMap<Location, Dataset> clusterCenters = new HashMap<Location, Dataset>();


        List<UserLocation> locs= adapter.getAllHistoryLocs("lala","martinloc",since.getTime(),until.getTime(),true,onlyUnClustered);
        System.out.println( "Number of Locations available for clustering:" + locs.size());
        Dataset data = new DefaultDataset();
        if (max != null) {
            int allLocs = locs.size();
            int every = (allLocs > max) ? allLocs / max : 1;
            int i = 0;
            for (UserLocation loc : locs) {
                if ((i++) % every == 0) {
                    data.add(loc);
                }
            }
        } else {
            for (UserLocation loc : locs) {
                data.add(loc);
            }
        }

        System.out.println( "" + data.size() + " Locations initialized for clustering");
        LocationDistanceMeasure ldm = new LocationDistanceMeasure(0.03,25);
        int minLocs = Math.max(10, (int) (data.size() * ldm.minLocsFactor));
        if (onlyUnClustered) {
            minLocs *= 3;
        }

        System.out.println("" + minLocs + " Locations required for clustering");
        DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(ldm.distance4Clustering, minLocs,ldm);
        System.out.println("Starting Clustering");

        resultsCluster = clusterer.cluster(data);


       System.out.println( "Finished Clustering");
        for (Dataset y : resultsCluster) {
            double sumLat = 0.0;
            double sumLon = 0.0;
            Iterator<Instance> it = y.listIterator();
            while (it.hasNext()) {
                Instance i = it.next();
                if (i instanceof UserLocation) {
                    UserLocation uloc = (UserLocation) i;
                    sumLat += ((double) uloc.getLoc().lat) / 1000000.0;
                    sumLon += ((double) uloc.getLoc().lon) / 1000000.0;
                }


            }
            int lat = (int) ((sumLat / (double) y.size()) * 1000000.0);
            int lon = (int) ((sumLon / (double) y.size()) * 1000000.0);
            clusterCenters.put(new Location(Location.LocationType.ADDRESS, lat, lon), y);
        }

        return clusterCenters;
    }

    public static HashMap<Location, Dataset> clusterLocations(DataAdapter adapter, Date since, Date until, Integer max) {
        return clusterLocations(adapter, since, until, max, false);
    }

    public static List<ClusterResult> clusterTime(Collection<AbstractMeasurement> data){
        LinkedList<ClusterResult> result = new LinkedList<ClusterResult>();
        DefaultDataset dds = new DefaultDataset();

            for(AbstractMeasurement instance: data){
                dds.add(instance);
            }
            TimeDistanceMeasure tdm = new TimeDistanceMeasure(TimeDistanceMeasure.TimeRepetitionInterval.HOUR_OF_DAY, (30.0/60.0), 0.1);
            int minLocs = Math.max(10, (int) (dds.size() * tdm.minClusterSize));
            DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(tdm.maxTimeDifference,minLocs,tdm);
            Dataset[] resultData = clusterer.cluster(dds);


            for(int i =0; i<resultData.length; i++){
                Dataset set = resultData[i];
                Iterator it = set.iterator();
                long maxTime=0;
                long minTime= Long.MAX_VALUE;
                LinkedList<AbstractMeasurement> resultList = new LinkedList<AbstractMeasurement>();
                while(it.hasNext()){
                    AbstractMeasurement absMes = (AbstractMeasurement) it.next();
                    maxTime = Math.max(absMes.getStart(), maxTime);
                    minTime = Math.min(absMes.getStart(), minTime);
                    resultList.add(absMes);
               }
            result.add(new ClusterResult(new Date(minTime), new Date(maxTime), resultList));
            }

        return result;
    }

    public static List<ClusterResult> clusterPatterns(String asd){
        JSONArray requestResult= null;
        HttpURLConnection connection = null;
        try {
            //Create connection

            URL url = new URL(asd);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            System.out.println(connection.getResponseCode());


            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            requestResult = new JSONArray(response.toString()) ;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
        DefaultDataset dds = new DefaultDataset();
        ArrayList<SHSensorEvent> events = new ArrayList<SHSensorEvent>();
        for(int i =0; i<requestResult.length(); i++){
            SHSensorEvent event =SHSensorEvent.fromJSON(requestResult.getJSONObject(i).toString());
            dds.add(event);
            events.add(event);
        }

        LinkedList<ClusterResult> result = new LinkedList<ClusterResult>();

        SHEventDistanceMeasure shedm = new SHEventDistanceMeasure(events);
        DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(SHEventDistanceMeasure.INTER_DAY_PATTERNS,shedm.getSuggestedRepetitionCriteria(SHEventDistanceMeasure.INTER_DAY_PATTERNS),shedm);
        Dataset[] resultData = clusterer.cluster(dds);


        for(int i =0; i<resultData.length; i++){
            Dataset set = resultData[i];
            Iterator it = set.iterator();
            long maxTime=0;
            long minTime= Long.MAX_VALUE;
            LinkedList<AbstractMeasurement> resultList = new LinkedList<AbstractMeasurement>();
            while(it.hasNext()){
                AbstractMeasurement absMes = (AbstractMeasurement) it.next();
                maxTime = Math.max(absMes.getStart(), maxTime);
                minTime = Math.min(absMes.getStart(), minTime);
                resultList.add(absMes);
            }
            result.add(new ClusterResult(new Date(minTime), new Date(maxTime), resultList));
        }

        return result;
    }

    public static List<ClusterResult> clusterEnergy(DataAdapter dataAdapter){
        ArrayList<ClusterResult> results = new ArrayList<ClusterResult>();
        List<PowerMeasurement> data = dataAdapter.getHistoryConsumption("","","",0l,0l);
        Dataset dataSet = new DefaultDataset();
        for(PowerMeasurement pm:data){
            dataSet.add(pm);
        }
        DensityBasedSpatialClustering clusterer = new DensityBasedSpatialClustering(10,5,new PowerDistance(new PowerDistance.StatusUpdater() {
            @Override
            public void onUpdate(int measurementCount) {
                System.out.println("Measerument " + measurementCount +" with Dataset Size "+dataSet.size() + "("+(measurementCount/(dataSet.size()*dataSet.size()))+"%)");
            }
        }));

        Dataset[] result =  clusterer.cluster(dataSet);
        for(int i=0; i<result.length; i++){
            long start = Long.MAX_VALUE;
            long end = 0l;

            ArrayList<AbstractMeasurement> measurements = new ArrayList<AbstractMeasurement>();
            for(int j=0; j<result[i].size(); j++){
                PowerMeasurement pmCurrent = (PowerMeasurement)result[i].get(j);
                start = Math.min(start, pmCurrent.getStart());
                end = Math.max(end,pmCurrent.getStart());

                measurements.add(pmCurrent);
            }
            results.add(new ClusterResult(new Date(start),new Date(end),measurements));
        }
    return results;
    }

    public static List<ClusteredLocation> getClusteredLocationsFromCache() {
        //TODO: get Clusters from Cache
        //return Utilities.openDBConnection().getAllClusterLocs();
        return null;
    }
    public static List<ClusteredLocation> getClusteredLocationWithIDs(Collection<Long> ids){
        List<ClusteredLocation> res = getClusteredLocationsFromCache();
        Iterator<ClusteredLocation> it = res.iterator();
        while(it.hasNext()){
            ClusteredLocation cloc = it.next();
            if(!ids.contains(cloc.getId())){
                it.remove();
            }
        }
    return res;
    }

    public static List<ClusteredLocation> getCloseByClusteredLocationsFromCache(double maxDistanceInMeter, Location currentLoc) {
        List<ClusteredLocation> all = getClusteredLocationsFromCache();
        Iterator<ClusteredLocation> it = all.iterator();
        while (it.hasNext()) {
            ClusteredLocation cl = it.next();


            if (currentLoc != null) {
                double distance = PTNMELocationManager.computeDistance(cl.getLoc(),currentLoc);
                if (distance > maxDistanceInMeter) {
                    it.remove();
                }
            }

        }
        return all;

    }

    public static ClusteredLocation getClosestClusteredLocationsFromCache() {
        //TODO: Alle ClusteredLocations holen
        List<ClusteredLocation> all = new ArrayList<ClusteredLocation>();
        ClusteredLocation res = null;
        //TODO: Location als Parameter annehmen
        double[] locations = new double[]{0.0,0.0};
        if (locations != null) {
            double x = locations[0];
            double y = locations[1];
            double mindistance = Double.MAX_VALUE;
            Iterator<ClusteredLocation> it = all.iterator();
            while (it.hasNext()) {
                ClusteredLocation cl = it.next();
                double distance = PTNMELocationManager.computeDistance(cl.getLoc(), x, y);
                if (distance < mindistance) {
                    res = cl;
                    mindistance = distance;
                }
            }
            return res;
        } else {
            return null;
        }

    }

    public static void addNewClusteredLocations(Map<Location, Dataset> locs, ClusterMetaData.ClusterType type) {


        for (Location loc : locs.keySet()) {
            ClusteredLocation toAdd = null;
            List<ClusteredLocation> oldClusteredLocs = getClusteredLocationsFromCache();
            for (ClusteredLocation oldLoc : oldClusteredLocs) {
                double distance = PTNMELocationManager.computeDistance(loc, oldLoc.getLoc());
                if (distance < (2*LocationDistanceMeasure.DEFAULT_DISTANCE_4_CLUSTERING)) {
                    toAdd = oldLoc;
                    break;
                }
            }
            if (toAdd == null) {
               // Utilities.openDBConnection().saveClusterLocation(loc, locs.get(loc), type);
            } else {
               // Log.d("PTEnabler", "Updating Clustered Location: ID " + toAdd.getId() + " Last Clustered: " + new Date(toAdd.getDate()).toLocaleString());
                int lat = (loc.lat + toAdd.getLoc().lat) / 2;
                int lon = (loc.lon + toAdd.getLoc().lon) / 2;
                Location upatedLL;
                if (toAdd.getLoc().place != null) {
                    upatedLL = new Location(Location.LocationType.ADDRESS, toAdd.getLoc().id, lat, lon, toAdd.getLoc().place, toAdd.getLoc().name);
                } else {
                    upatedLL = new Location(Location.LocationType.ADDRESS, lat, lon);
                }

                toAdd.setDate(new Date().getTime());
                toAdd.setLoc(upatedLL);
                toAdd.setCount(toAdd.getCount() + 1);
               // Utilities.openDBConnection().updateClusteredLocation(toAdd, locs.get(loc));

            }
        }



    }

    public static void addNewClusteredLocations(List<ClusteredLocation> clocs) {

        /*
        for (ClusteredLocation loc : clocs) {

            Utilities.openDBConnection().updateClusteredLocation(loc, null);

        }
        */
    }

    public static void clearClusters() {
        clearClusters(14);
    }

    public static void clearClusters(int olderThanXdays) {
        /*
        Utilities.openDBConnection().clearClusteredLocations(olderThanXdays);
        */
    }

    public static Set<PropableNextLocationResult> getProbableDestinations(long cid, boolean includeCurrent) {
        List<ClusteredLocation> clocations = getClusteredLocationsFromCache();
        if (probresult == null) {
            HashMap<Integer, ClusteredLocation> probLoc = new HashMap<Integer, ClusteredLocation>();
            TreeSet<UserLocation> ulocs = new TreeSet<UserLocation>();
            // TODO: ALle Locations holen, die für Übergangswahrscheinlihckeit relevant
            //ulocs.addAll(Utilities.openDBConnection().getAllHistoryLocs(0, new Date().getTime(), false));

            // All IDs of currently available cluster
            Vector<Long> cids = new Vector<Long>();
            for (ClusteredLocation cl : clocations) {
                cids.add(cl.getId());
            }
            HashMap<Long, HashMap<Long, Integer>> sums = new HashMap<Long, HashMap<Long, Integer>>();
            Iterator<UserLocation> it = ulocs.iterator();
            long lastparentCluster = -1;
            int tempCount = 0;
            while (it.hasNext()) {
                UserLocation current = it.next();
                if (cids.contains(current.getParentCluster())) {
                    if (lastparentCluster == current.getParentCluster() || current.getParentCluster() == 0) {
                        // Iteration durch einen Cluster oder durch noise

                        //tempCount auf 0, um sicherzustellen, dass das vorbeilaufen an einem Cluster nicht gezählt wird.
                        tempCount = 0;
                        continue;
                    } else {
                        // Eintritt in neuen Cluster der von altem Abweicht
                        if (lastparentCluster == -1) {
                            // Eintritt in ersten Cluster
                            // Setzen und weiter
                            lastparentCluster = current.getParentCluster();
                            continue;
                        }


                        if (tempCount < 5) {
                        // Vorbeilaufen an Clustern ausschließen
                        // Vergleichswert heißt, dass 5 Locations im selben Cluster gefunden werden müssen mit jeweils 30 Sekunden differenz
                            tempCount++;
                            continue;
                        }
                        // Clusterwechsel (am Ende von Noise oder direkt)
                        HashMap<Long, Integer> c1Scores = sums.get(lastparentCluster);
                        if (c1Scores != null) {
                            Integer c1c2score = c1Scores.get(current.getParentCluster());
                            if (c1c2score == null) {
                                c1Scores.put(current.getParentCluster(), 1);
                            } else {
                                c1Scores.put(current.getParentCluster(), (c1c2score.intValue() + 1));
                            }
                        } else {
                            c1Scores = new HashMap<Long, Integer>();
                            c1Scores.put(current.getParentCluster(), 1);
                            sums.put(lastparentCluster, c1Scores);
                        }

                        tempCount = 0;
                        lastparentCluster = current.getParentCluster();
                    }
                }
            }
            probresult = calculateProbabilityForDestinations(sums);

        }
        HashMap<Long, Double> currentCluster = probresult.get(cid);
        if (currentCluster != null && currentCluster.size() != 0) {
            TreeSet<PropableNextLocationResult> results = new TreeSet<PropableNextLocationResult>();
            for (ClusteredLocation cloc : clocations) {
                if (currentCluster.keySet().contains(cloc.getId())) {

                        results.add(new PropableNextLocationResult(currentCluster.get(cloc.getId()), cloc));
                }
            }
            return results;
        }

        return null;

    }

    public static Set<PropableNextLocationResult> getProbableDestinations(long cid) {
        return getProbableDestinations(cid, false);
    }

    public static Set<PropableNextLocationResult> getPropableLocationForDate(Date x, boolean includeCurPosCluster) {
        // TODO: ALle Locations holen, die für Übergangswahrscheinlihckeit relevant
        List<UserLocation> all = new ArrayList<UserLocation>();//Utilities.openDBConnection().getAllHistoryLocs(0, new Date().getTime());
        Calendar toLookFor = Calendar.getInstance();
        toLookFor.setTimeInMillis(x.getTime());
        Calendar loccal = Calendar.getInstance();
        HashMap<Long, Double> props = new HashMap<Long, Double>();
        HashMap<Long, Integer> hourOfDaytoUse = new HashMap<Long, Integer>();
        for (UserLocation loc : all) {
            loccal.setTimeInMillis(loc.getDate());
            if (loc.getParentCluster() != 0
                    && loccal.get(Calendar.HOUR_OF_DAY) == toLookFor.get(Calendar.HOUR_OF_DAY)) {


                if (hourOfDaytoUse.get(loc.getParentCluster()) != null) {
                    if (loccal.get(Calendar.DAY_OF_WEEK) == toLookFor.get(Calendar.DAY_OF_WEEK)) {
                        hourOfDaytoUse.put(loc.getParentCluster(), hourOfDaytoUse.get(loc.getParentCluster()) + 4);
                    } else {
                        hourOfDaytoUse.put(loc.getParentCluster(), hourOfDaytoUse.get(loc.getParentCluster()) + 1);
                    }

                } else {
                    if (loccal.get(Calendar.DAY_OF_WEEK) == toLookFor.get(Calendar.DAY_OF_WEEK)) {
                        hourOfDaytoUse.put(loc.getParentCluster(), 4);
                    } else {
                        hourOfDaytoUse.put(loc.getParentCluster(), 1);
                    }

                }


            }
        }
        double sum = 0;
        for (int count : hourOfDaytoUse.values()) {
            sum += count;
        }

        for (long cid : hourOfDaytoUse.keySet()) {
            props.put(cid, ((double) hourOfDaytoUse.get(cid)) / sum);
        }
        TreeSet<PropableNextLocationResult> res = new TreeSet<PropableNextLocationResult>();
        for (ClusteredLocation cloc : getClusteredLocationsFromCache()) {
            if (props.keySet().contains(cloc.getId())) {

                    res.add(new PropableNextLocationResult(props.get(cloc.getId()), cloc));

            }
        }

        return res;
    }

    private static HashMap<Long, HashMap<Long, Double>> calculateProbabilityForDestinations(HashMap<Long, HashMap<Long, Integer>> inputs) {
        HashMap<Long, HashMap<Long, Double>> res = new HashMap<Long, HashMap<Long, Double>>();
        for (long c1 : inputs.keySet()) {
            HashMap<Long, Integer> c1scores = inputs.get(c1);
            double sum = 0;
            for (int count : c1scores.values()) {
                sum += count;
            }
            for (long c1c2 : c1scores.keySet()) {
                HashMap<Long, Double> c1c2prob = res.get(c1);
                if (c1c2prob == null) c1c2prob = new HashMap<Long, Double>();
                if (sum != 0) c1c2prob.put(c1c2, ((double) c1scores.get(c1c2)) / sum);
                res.put(c1, c1c2prob);
            }
        }

        return res;


    }
    public static String exportClusters(){

        List<ClusteredLocation> clusters = getClusteredLocationsFromCache();
        Gson gson = new Gson();
        String test = gson.toJson(clusters);
        return test;

    }

}

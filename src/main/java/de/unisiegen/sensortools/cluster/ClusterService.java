package de.unisiegen.sensortools.cluster;

public class ClusterService {

	public final String LAST_CLUSTERED = "LAST_CLUSTERED";
	private double mintransitionprob = 0.05;
    private int timeinFuture= 30;
   /*
    private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	*/
	public ClusterService() {
		//super("TMClusteringService");
		
		
	}

	/*
	protected void onHandleIntent(Intent intent) {
		prefs= PreferenceManager.getDefaultSharedPreferences(this);
		long now = new Date().getTime();
		
			
			Utilities.openDBConnection().clearLocationHistory(14);
			Date start = new Date((new Date().getTime()-(1000l*3600l*24l)));
			Map<Location, Dataset> clusteredLocs = ClusterManagement.clusterLocations(start, new Date(), 1000);
            //Map<Location, Dataset> clusteredLocs = Utilities.clusterLocations(new Date(0) , new Date(), 10000);
			Log.d("PTEnabler ClusteringService","Done with yesterday! "+ clusteredLocs.size()+ "Locations calculated" );
            ClusterManagement.addNewClusteredLocations(clusteredLocs, ClusterMetaData.ClusterType.DAILY_CLUSTER);
			if(prefs.getBoolean("INCLUDE_NOISE", false)){
                Log.d("PTEnabler ClusteringService","Checking remaining noise! ");
                clusteredLocs.clear();
                clusteredLocs.putAll(ClusterManagement.clusterLocations(new Date(0), new Date(), Integer.MAX_VALUE, true));
                ClusterManagement.addNewClusteredLocations(clusteredLocs, ClusterMetaData.ClusterType.NOISE_CLUSTER);
            }

			editor = prefs.edit();
			editor.putLong(LAST_CLUSTERED, now);
			editor.commit();
			Log.d("PTEnabler ClusteringService","Done! "+ clusteredLocs.size()+ "Locations calculated in total" );

			ClusterManagement.clearClusters();
			List<ClusteredLocation> clocs = ClusterManagement.getClusteredLocationsFromCache();
			ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    if(activeNetworkInfo != null && activeNetworkInfo.isConnected()){
			    
		    	for (ClusteredLocation caller:clocs){
			    	if(caller.getLoc().place==null){

                        Log.d("PTEnabler ClusteringService","Reverse Geocoding Cluster "+ caller.getId());
                        double shortenX = (double)caller.getLoc().lat / 1000000;
                        double shortenY = (double)caller.getLoc().lon / 1000000;

                        JSONObject currAdr=null;
						try {


							currAdr = Utilities.getJSONObjectFromUrl("http://nominatim.openstreetmap.org/reverse?format=json&limit=5&lat="+shortenX+"1&lon="+shortenY+"1&addressdetails=1").getJSONObject("address");

						} catch (JSONException e) {
							e.printStackTrace();
						}
						if(currAdr !=null){
							String place = currAdr.optString("road")+" "+currAdr.optString("house_number")+", "+currAdr.optString("postcode")+" "+(currAdr.optString("city"));
							Log.d("PTEnabler ClusteringService","Adress: "+ place);
							Location loc = new Location(caller.getLoc().type, caller.getLoc().id, caller.getLoc().lat, caller.getLoc().lon, place, place);
							caller.setLoc(loc);

						}else{
							Log.d("PTEnabler ClusteringService","Could not retrieve address");
						}


                        Location loc = new Location(caller.getLoc().type, caller.getLoc().id, caller.getLoc().lat, caller.getLoc().lon, place, place);
                        caller.setLoc(loc);
                    }
                    caller.getMeta().nextCIDs = caller.getProableNextCID(mintransitionprob,timeinFuture);

			    }
		    	ClusterManagement.addNewClusteredLocations(clocs);
		    }




	}
*/
}

package de.unisiegen.locationtools.cluster;
import java.util.Comparator;

import java.util.List;

import de.ms.ptenabler.util.Utilities;
import de.schildbach.pte.LocationUtils;
import de.schildbach.pte.dto.Location;

public class LocationComparator implements  Comparator<Location>{
	double lat;
	double lng;
	public LocationComparator(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public int compare(Location lhs, Location rhs) {
		List<Location> history = Utilities.getDataAdapter().getAllLocs();
		if(history.contains(lhs) && !history.contains(rhs)){
			return -1;
		}
		if(!history.contains(lhs) && history.contains(rhs)){
			return 1;
		}
		double distance = LocationUtils.computeDistance(lat, lng, ((double)lhs.lat/100000.0), ((double)lhs.lon/1000000.0)) - LocationUtils.computeDistance(lat, lng, ((double)rhs.lat/1000000.0), ((double)rhs.lon/100000.0));
		if(distance >0){
			return -1;
		}else{
			return 1;
		}
	}
	

}

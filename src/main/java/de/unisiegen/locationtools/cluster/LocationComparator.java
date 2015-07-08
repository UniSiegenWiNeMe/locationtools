package de.unisiegen.locationtools.cluster;
import de.unisiegen.locationtools.Location;
import de.unisiegen.locationtools.LocationUtils;

import java.util.Comparator;

import java.util.List;


public class LocationComparator implements  Comparator<Location>{
	double lat;
	double lng;
	public LocationComparator(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public int compare(Location lhs, Location rhs) {
		/* TODO Datenbankzugriff
		List<Location> history = Utilities.getDataAdapter().getAllLocs();

		if(history.contains(lhs) && !history.contains(rhs)){
			return -1;
		}
		if(!history.contains(lhs) && history.contains(rhs)){
			return 1;
		}
		double distance = LocationUtils.computeDistance(lat, lng, ((double) lhs.lat / 100000.0), ((double) lhs.lon / 1000000.0)) - LocationUtils.computeDistance(lat, lng, ((double)rhs.lat/1000000.0), ((double)rhs.lon/100000.0));
		if(distance >0){
			return -1;
		}else{
			return 1;
		}
	}
	

}

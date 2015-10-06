package de.unisiegen.sensortools.cluster;

import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;

import java.util.Date;
import java.util.List;

/**
 * Created by Martin on 30.09.2015.
 */
public class TimeClusterResult {
    public Date start;
    public Date end;
    public List<AbstractMeasurement>data;

    public TimeClusterResult(Date start, Date end, List<AbstractMeasurement> data){
        this.start = start;
        this.end = end;
        this.data = data;
    }
}

package de.unisiegen.sensortools.cluster.distanceMeasures;

import de.unisiegen.sensortools.cluster.sensors.PowerMeasurement;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

/**
 * Created by Martin Stein on 10.03.2016.
 */
public class PowerDistance implements DistanceMeasure {

    //DistanceMeasure
    StatusUpdater updater;
    int counter =0;

    public PowerDistance(){
    this.updater = null;
    }
    public PowerDistance(StatusUpdater update){
    this.updater = update;
    }

    public double measure(Instance instance, Instance instance1) {
        PowerMeasurement i1 = (PowerMeasurement) instance;
        PowerMeasurement i2 = (PowerMeasurement) instance1;
        double a =Double.valueOf(i1.getValue("POWER"));
        double b =Double.valueOf(i2.getValue("POWER"));
        double powerDistance= Math.abs(a-b);
        long timeDistance = (Math.abs(i1.getStart()-i2.getStart()) / 5000l);
        if(updater!=null)updater.onUpdate(counter++);
        return Math.sqrt((powerDistance*powerDistance)+(timeDistance*timeDistance));

    }

    @Override
    public boolean compare(double v, double v1) {
        return v<v1;
    }

    @Override
    public double getMinValue() {
        return 0;
    }

    @Override
    public double getMaxValue() {
        return Double.MAX_VALUE;
    }

public interface StatusUpdater{
    public void onUpdate(int measurementCount);

}

}


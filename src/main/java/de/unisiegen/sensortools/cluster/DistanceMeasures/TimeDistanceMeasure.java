package de.unisiegen.sensortools.cluster.distanceMeasures;

import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

/**
 * Created by Martin on 15.07.2015.
 */
public class TimeDistanceMeasure implements DistanceMeasure {

    @Override
    public double measure(Instance instance, Instance instance1) {
       if(instance instanceof AbstractMeasurement && instance1 instanceof AbstractMeasurement){
           AbstractMeasurement measurement1 = (AbstractMeasurement) instance;
           AbstractMeasurement measurement2 =(AbstractMeasurement) instance1;



       }
        return 0;
    }

    @Override
    public boolean compare(double v, double v1) {
        return false;
    }

    @Override
    public double getMinValue() {
        return 0;
    }

    @Override
    public double getMaxValue() {
        return 0;
    }
}

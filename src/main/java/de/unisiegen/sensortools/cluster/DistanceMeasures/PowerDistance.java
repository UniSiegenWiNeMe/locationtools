package de.unisiegen.sensortools.cluster.DistanceMeasures;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

/**
 * Created by Matthias Betz on 15.07.2015.
 */
public class PowerDistance implements DistanceMeasure {

    //DistanceMeasure
    @Override
    public double measure(Instance instance, Instance instance1) {
       return instance.get(0) - instance1.get(0);
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
        return 3300;
    }
}

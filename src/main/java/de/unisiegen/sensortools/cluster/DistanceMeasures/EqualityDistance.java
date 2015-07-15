package de.unisiegen.sensortools.cluster.DistanceMeasures;

import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

/** A simple Yes/No Metric, suitable for all Measurements
 * Created by lars on 15/07/15.
 */
public class EqualityDistance implements DistanceMeasure {

    @Override
    public double measure(Instance instance, Instance instance1) {
        if (instance.equals(instance1))
            return 1;
        else
            return 0;
    }

    @Override
    public boolean compare(double v, double v1) {
        return v == v1;
    }

    @Override
    public double getMinValue() {
        return 0;
    }

    @Override
    public double getMaxValue() {
        return 1;
    }
}

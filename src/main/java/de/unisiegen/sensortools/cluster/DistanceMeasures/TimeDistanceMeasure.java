package de.unisiegen.sensortools.cluster.distanceMeasures;

import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;

/**
 * Created by Martin on 15.07.2015.
 */
public class TimeDistanceMeasure implements DistanceMeasure {

    private TimeRepetitionInterval interval;
    private DateTimeFieldType intervalType;
    private double maxValue;
    private double maxTimeDifference;
    private int minClusterSize;


    public enum TimeRepetitionInterval{
        DAYS_OF_YEAR,
        DAY_OF_MONTH,
        HOUR_OF_DAY,
        DAY_OF_WEEK

    }
    public TimeDistanceMeasure(TimeRepetitionInterval type, double maxTimeDifference, int minClusterSize ){
        this.interval = type;
        this.maxTimeDifference = maxTimeDifference;
        this.minClusterSize = minClusterSize;
    }
    @Override
    public double measure(Instance instance, Instance instance1) {
       if(instance instanceof AbstractMeasurement && instance1 instanceof AbstractMeasurement){
           AbstractMeasurement measurement1 = (AbstractMeasurement) instance;
           AbstractMeasurement measurement2 =(AbstractMeasurement) instance1;
           DateTime dt1 = new DateTime(measurement1.getStart());
           DateTime dt2 = new DateTime(measurement2.getStart());





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
        switch (interval){
            case HOUR_OF_DAY: return 24;
            case DAYS_OF_YEAR: return 366;
            case DAY_OF_MONTH: return 31;
            case DAY_OF_WEEK: return 7;
        }
        return 0;
    }
}

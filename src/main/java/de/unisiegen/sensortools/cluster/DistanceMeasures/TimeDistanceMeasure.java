package de.unisiegen.sensortools.cluster.distanceMeasures;

import de.unisiegen.sensortools.cluster.sensors.AbstractMeasurement;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import org.joda.time.*;

/**
 * Created by Martin on 15.07.2015.
 */
public class TimeDistanceMeasure implements DistanceMeasure {

    private TimeRepetitionInterval interval;
    private DateTimeFieldType intervalType;
    private double maxValue;
    public double maxTimeDifference;
    public double minClusterSize;


    public enum TimeRepetitionInterval{
        DAYS_OF_YEAR,
        DAY_OF_MONTH,
        HOUR_OF_DAY,
        DAY_OF_WEEK

    }
    public TimeDistanceMeasure(TimeRepetitionInterval type, double maxTimeDifference, double minClusterSizeFactor ){
        this.interval = type;
        this.maxTimeDifference = maxTimeDifference;
        this.minClusterSize = minClusterSize;
    }
    @Override
    public double measure(Instance instance, Instance instance1) {
        double distance = Double.MAX_VALUE;
        if(instance instanceof AbstractMeasurement && instance1 instanceof AbstractMeasurement){
           AbstractMeasurement measurement1 = (AbstractMeasurement) instance;
           AbstractMeasurement measurement2 =(AbstractMeasurement) instance1;

           DateTime dt1 = new DateTime(measurement1.getStart());
           DateTime dt2 = new DateTime(measurement2.getStart());

           switch(this.interval){
               case DAY_OF_MONTH: {
                    return daysOfMonthDistance(dt1,dt2);
               }
               case DAYS_OF_YEAR:{
                    return dayOfYearDistance(dt1,dt2);
               }
               case HOUR_OF_DAY:{
                    return hourOfDayDistance(dt1,dt2);
               }
               case DAY_OF_WEEK:{
                    return dayOfWeekDistance(dt1,dt2);
               }
               default:{
                throw new ArithmeticException("Missing Interval Type Attribute. Please provide the interval type when constructing TimeDistanceMeasure");

               }
           }





       }else{
        throw new IllegalArgumentException("The provided values must implement the de.unisiegen.sensortools.AbstractMeasurement interface ");
        }

    }

    private double dayOfYearDistance(DateTime dt1,DateTime dt2 ){
        Interval interval = new Interval(dt1,dt2);
        double distance = ((double)(    interval.toDurationMillis()% //Distance between the two Dates in Millis
                                        (1000l*3600*24l*365l)))/    //Days of a Year to take into Account Yearly re-occurance
                                        (1000*3600*24);   // To get the number of Days as distance measure
        return distance;
    }
    private double daysOfMonthDistance(DateTime dt1,DateTime dt2){
        int day1= dt1.getDayOfMonth();
        int day2 = dt2.getDayOfMonth();
        LocalTime time1= new LocalTime(dt1.getHourOfDay(),dt1.getMinuteOfHour(),dt1.getSecondOfMinute());
        LocalTime time2= new LocalTime(dt2.getHourOfDay(),dt2.getMinuteOfHour(),dt2.getSecondOfMinute());
        double timeShare = (double)(Minutes.minutesBetween(time1,time2).getMinutes()) / (60.0*24.0);
        return timeShare+Math.abs((day1-day2));

    }
    private double hourOfDayDistance(DateTime dt1,DateTime dt2){
        LocalTime time1= new LocalTime(dt1.getHourOfDay(),dt1.getMinuteOfHour(),dt1.getSecondOfMinute());
        LocalTime time2= new LocalTime(dt2.getHourOfDay(),dt2.getMinuteOfHour(),dt2.getSecondOfMinute());
        return ((double)Minutes.minutesBetween(time1,time2).getMinutes())/60.0;
    }
    private double dayOfWeekDistance(DateTime dt1,DateTime dt2){
        LocalTime time1= new LocalTime(dt1.getHourOfDay(),dt1.getMinuteOfHour(),dt1.getSecondOfMinute());
        LocalTime time2= new LocalTime(dt2.getHourOfDay(),dt2.getMinuteOfHour(),dt2.getSecondOfMinute());
        return ((double)Minutes.minutesBetween(time1,time2).getMinutes())/60.0;
    }


    @Override
    public boolean compare(double v, double v1) {
        return  v<v1;
    }

    @Override
    public double getMinValue() {
        return 0;
    }

    @Override
    public double getMaxValue() {
        switch (interval){
            case HOUR_OF_DAY: return 24;
            case DAYS_OF_YEAR: return 364;
            case DAY_OF_MONTH: return 31;
            case DAY_OF_WEEK: return 7;
        }
        return 0;
    }
}

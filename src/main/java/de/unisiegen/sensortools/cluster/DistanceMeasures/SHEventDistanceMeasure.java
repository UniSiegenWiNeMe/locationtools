package de.unisiegen.sensortools.cluster.distanceMeasures;

import de.unisiegen.sensortools.cluster.sensors.SHSensorEvent;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;

import java.util.*;

/**
 * Created by Martin on 27.02.2016.
 */
public class SHEventDistanceMeasure implements DistanceMeasure {
    public static long SHORT_DURATION_PATTERNS_DENSITTY_CRITERIA = 1000l*60l*20l;
    public static long LONG_DURATION_PATTERNS_DENSITTY_CRITERIA = 1000l*60l*120l;
    public final static int INTER_DAY_PATTERNS = 1;
    public final static int INTRA_DAY_PATTERNS = 2;

    private TreeMap<String,ArrayList<Long>> referenceValues;
    private long firstEventTime = Long.MAX_VALUE;
    private long lastEventTime = 0l;
    private Comparator<Long> comparator = new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o1.compareTo(o2);
        }
    };



    public SHEventDistanceMeasure(List<SHSensorEvent> eventsToCompare){

        referenceValues = new TreeMap<String, ArrayList<Long>>();
        for(SHSensorEvent event:eventsToCompare){
            firstEventTime = Math.min(firstEventTime,event.getStart());
            lastEventTime = Math.max(lastEventTime,event.getStart());
            ArrayList<Long> valuesForType = referenceValues.getOrDefault(event.getSUID(), new ArrayList<Long>());
            valuesForType.add(event.getStart());
            referenceValues.put(event.getSUID(),valuesForType);
        }
        for(String type:referenceValues.keySet()){
            Collections.sort(referenceValues.get(type),comparator);
        }

    }

    public int getSuggestedRepetitionCriteria(int patternType){
        switch (patternType){

            case INTER_DAY_PATTERNS: {
                return (int)(((lastEventTime-firstEventTime)/(1000l*60l*60l*24l))*0.03);
            }
            case INTRA_DAY_PATTERNS:{
                return (int)(((lastEventTime-firstEventTime)/(1000l*60l*60l*24l))*0.1);
            }
            default: return (int)(((lastEventTime-firstEventTime)/(1000l*60l*60l*24l))*0.03);

        }

    }
    @Override
    public double measure(Instance instance, Instance instance1) throws IllegalArgumentException{
        if(instance instanceof SHSensorEvent &&instance1 instanceof SHSensorEvent ){
            SHSensorEvent event1 = (SHSensorEvent) instance;
            SHSensorEvent event2 = (SHSensorEvent) instance1;
            return getClosestValueForType(referenceValues.get(event2.getSUID()), event1.getStart());

        }else{
            throw new IllegalArgumentException("Instance needs to be SHSensorEvent");
        }
    }
    @Override
    public boolean compare(double v, double v1) {
            if(v>=v1){
                return true;
            }
            return false;
    }


    @Override
    public double getMinValue() {
        return 0;
    }

    @Override
    public double getMaxValue() {
        return new Date().getTime()*1.5;
    }

   private long getClosestValueForType(List<Long>relevantVal, long val){
       if(relevantVal.size()==0){
           return Long.MAX_VALUE;
       }
       if(relevantVal.size()==1){
            return  relevantVal.get(0);
       }
       if(relevantVal.contains(val)) return val;
       if(val>=relevantVal.get(relevantVal.size()-1)) return relevantVal.get(relevantVal.size() - 1);
       if(val<=relevantVal.get(0)) return relevantVal.get(0);


       List<Long> upperList = relevantVal.subList((relevantVal.size()/2),relevantVal.size());
       List<Long> lowerList = relevantVal.subList(0,(relevantVal.size()/2));
       if(upperList.size()==0 || lowerList.size()==0){
           System.out.println("OOOOOO");
       }
       long upperValue =getClosestValueForType(upperList,val);
       long lowerValue =getClosestValueForType(lowerList,val);
       long upperDist = Math.abs(upperValue - val);
       long lowerDist = Math.abs(lowerValue-val);
       if(upperDist<=lowerDist){
           return upperValue;
       }else{
           return lowerValue;
       }
       }



}


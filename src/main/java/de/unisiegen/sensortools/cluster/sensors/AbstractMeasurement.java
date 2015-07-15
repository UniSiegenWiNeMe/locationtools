package de.unisiegen.sensortools.cluster.sensors;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Martin on 15.07.2015.
 */
public abstract class AbstractMeasurement extends DenseInstance{
    public  String name;
    public  Map<String, String> tags;
    public  long start;
    public  long end;

    public AbstractMeasurement() {
        super(new double[]{});

    }

    public String getName(){
        return name;
    };
    public void setName(String name){
        this.name = name;
    }
    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }
    public long getStart(){
        return start;
    };
    public long getEnd(){
        return end;
    };
    public abstract Collection<Object> getValues();
    public abstract Collection<Object> setValues(Collection<Object> values);
    public abstract Map<String, String> getTags();
    public abstract void setTags(Map<String, String> tags);

}

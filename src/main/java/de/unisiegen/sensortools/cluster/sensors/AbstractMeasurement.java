package de.unisiegen.sensortools.cluster.sensors;

import net.sf.javaml.core.AbstractInstance;
import net.sf.javaml.core.DenseInstance;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Martin on 15.07.2015.
 */
public abstract class AbstractMeasurement extends DenseInstance{
    public  String name;
    public  Map<String, String> values;
    public  long start;
    public  long end;

    public AbstractMeasurement() {
        super(new double[]{});
        values = new HashMap<>();
    }
    public AbstractMeasurement(double[] values){
        super(values);
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

    /** Default getter, just insert key that stored the value.
     *
     * @param key
     * @return
     */
    public String getValue(String key) {
        return values.get(key);
    }

    /** Default setter of key-value pair, Just add water.
     *
     * @param key
     * @param value
     */
    public void setValue(String key, String value) {
        values.put(key, value);
    }

    /** Default deletion function for internal store. Returns the contained value.
     * Todo: Add exception for key unknown.
     *
     * @param key
     * @return
     */
    public String removeValue(String key){
        String val = values.get(key);
        values.remove(key);
        return val;
    }



}

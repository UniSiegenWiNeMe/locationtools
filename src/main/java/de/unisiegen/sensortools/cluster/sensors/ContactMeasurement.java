package de.unisiegen.sensortools.cluster.sensors;

import de.unisiegen.sensortools.cluster.DistanceMeasures.EqualityDistance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.distance.DistanceMeasure;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.*;

/**
 * Measure Contact Events (i.e. meeting people).
 * A measurement consists of a time interval (start, end)
 * and a single identifier of the other entity (String)
 * Created by lars on 15/07/15.
 */
public class ContactMeasurement extends AbstractMeasurement implements Comparable {

    static int last_id = 0;

    final static String contactIDkey = "contact_ID";

    /** unique identifier of this contact measurement */
    private int measure_id;

    public ContactMeasurement(long ts, long te, String id) {
        super();
        measure_id = last_id + 1;
        last_id++;
        setStart(ts);
        setEnd(te);
        setValue(contactIDkey, id);
    }

    @Override
    public Collection<Object> getValues() {
        return new ArrayList<>(this.tags.values());
    }

    @Override
    public Collection<Object> setValues(Collection<Object> values) {
        throw new NotImplementedException();
    }


    @Override
    public Map<String, String> getTags() {
        return new HashMap<String,String>(this.tags);
    }

    @Override
    public void setTags(Map<String, String> tags) {
        Iterator<String> keys = tags.keySet().iterator();
        while(keys.hasNext()) {
            String k = keys.next();
            this.setValue(k,tags.get(k));
        }
    }

    /** Equality if start, end and all fields are equal.
     *
     * @param other
     * @return
     */
    public boolean equals(ContactMeasurement other) {
        if (! getTags().keySet().equals(other.getTags().keySet()))
            return false;
        for (Iterator<String> keys = getTags().keySet().iterator();
            keys.hasNext();  ) {
            String k = keys.next();
            if (!this.getValue(k).equals(other.getValue(k)))
                return false;
        }
        return getStart() == other.getStart()
                && getEnd() == other.getEnd();
    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object other) {
        DistanceMeasure distance = new EqualityDistance();
        return (int) distance.measure(this, (Instance) other);
    }
}

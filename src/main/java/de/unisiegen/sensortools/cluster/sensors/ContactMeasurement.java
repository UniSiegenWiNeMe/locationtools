package de.unisiegen.sensortools.cluster.sensors;

import java.util.Collection;
import java.util.Map;

/**
 * Measure Contact Events (i.e. meeting people).
 * A measurement consists of a time interval (start, end)
 * and a single identifier of the other entity (String)
 * Created by lars on 15/07/15.
 */
public class ContactMeasurement extends AbstractMeasurement implements Comparable {

    static int last_id = 0;

    final static String contactIDkey "contact_ID"

    /** unique identifier of this contact measurement */
    int measure_id;

    synchronized public ContactMeasurement(long ts, long te, String id) {
        super();
        measure_id = last_id + 1;
        setStart(ts);
        setEnd(te);
        setValue(contactIDkey, id);
    }

    @Override
    public Collection<Object> getValues() {

        return null;
    }

    @Override
    public Collection<Object> setValues(Collection<Object> values) {
        return null;
    }


    @Override
    public Map<String, String> getTags() {
        return null;
    }

    @Override
    public void setTags(Map<String, String> tags) {

    }

    /**
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

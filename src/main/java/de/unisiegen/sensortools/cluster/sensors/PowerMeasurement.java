package de.unisiegen.sensortools.cluster.sensors;

import java.util.Collection;
import java.util.Map;

/**
 * Created by Matthias Betz on 15.07.2015.
 */
public class PowerMeasurement extends AbstractMeasurement implements Comparable {

    public PowerMeasurement() {
        super();
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

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}

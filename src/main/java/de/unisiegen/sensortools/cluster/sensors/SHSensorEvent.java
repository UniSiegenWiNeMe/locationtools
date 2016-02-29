package de.unisiegen.sensortools.cluster.sensors;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONObject;


import java.util.*;

/**
 * Created by Martin on 27.02.2016.
 */
public class SHSensorEvent extends AbstractMeasurement {
    private String SUID;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getSUID() {
        return SUID;
    }

    public void setSUID(String SUID) {
        this.SUID = SUID;
    }

    private TreeMap<Long, String> values;
    private String UUID;



    public SHSensorEvent(String SUID,String UUID, String value, long timestamp, String readableName){

        this.setName(readableName);
        this.setStart(timestamp);
        this.SUID = SUID;
        this.values = new TreeMap<Long, String>();
        values.put(timestamp,value);
        this.UUID = UUID;
    };
    public static SHSensorEvent fromJSON(String json){
        System.out.println(json);
        JSONObject jsonEvent = new JSONObject(json);
        DateTimeFormatter dtf = DateTimeFormat.forPattern ("YYYY-MM-dd HH:mm:ss.S");
        DateTime dt = DateTime.parse(jsonEvent.optString("time"), dtf);
        return new SHSensorEvent(
                jsonEvent.optString("sensor_id"),
                jsonEvent.optString("user_id"),
                jsonEvent.optString("value"),
                dt.getMillis(),
                jsonEvent.optString("sensor")
        );
    }

    @Override
    public String getValue(String key) {
        return values.get(key);
    }
    public String getValue(){
        if(values.isEmpty())
        {
            return null;
        }
        Iterator<Long> first =values.keySet().iterator();

        return values.get(first.next());
    }
}

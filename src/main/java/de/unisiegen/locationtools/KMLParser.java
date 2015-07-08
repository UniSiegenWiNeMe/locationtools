package de.unisiegen.locationtools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;


/**
 * Created by Martin on 24.06.2015.
 */
public class KMLParser extends DefaultHandler {
    Boolean currentElement = false;
    String currentValue = "";
    public HashMap<Long, Location> items = null;
    private long currentdate = 0;


    // Called when tag starts
    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {

        if (localName.equals("Track")) {
            items = new HashMap<Long, Location>();
        }
        if (localName.equals("when") || localName.equals("coord")) {
            currentValue = "";
        }

    }

    // Called when tag closing
    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {

        currentElement = false;

        /** set value */
        if (localName.equalsIgnoreCase("when")) {
            DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
            DateTime date = parser.parseDateTime(currentValue);
            currentdate = date.getMillis();
        } else if (localName.equalsIgnoreCase("coord")) {
            String[] coords = currentValue.split(" ");
            if (coords.length != 2) {
                return;
            }
            Location loc = new Location(Location.LocationType.ADDRESS, (int) (Double.valueOf(coords[1]) * 1000000.0), (int) (Double.valueOf(coords[0]) * 1000000.0));
            items.put(currentdate, loc);
        }
    }

    // Called to get tag characters
    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        currentValue = currentValue + new String(ch, start, length);
    }
}

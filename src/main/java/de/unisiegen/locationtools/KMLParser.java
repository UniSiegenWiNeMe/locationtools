package de.unisiegen.locationtools;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;


/**
 * Created by Martin on 24.06.2015.
 */
public class KMLParser {

    public  HashMap<Long,Location> parseLocations(String xml) throws ParserConfigurationException,SAXException,FileNotFoundException,IOException {

        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        SaxHandler myXMLHandler = new SaxHandler();
        xr.setContentHandler(myXMLHandler);
        xr.parse(new InputSource(new StringReader(xml)));
        System.out.println("Imported " + myXMLHandler.items.size() + " Locations");
        return myXMLHandler.items;
    }

    private class SaxHandler extends DefaultHandler {
        Boolean currentElement = false;
        String currentValue = "";
        public HashMap<Long, Location> items = new HashMap<Long, Location>();
        private long currentdate = 0;
        // Called when tag starts
        @Override
        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            if (isRelevantTagName(localName) || isRelevantTagName(qName)) {
                currentValue = "";
            }
        }


        private boolean isRelevantTagName(String localName) {
            return isWhenTag(localName) || isCoordTag(localName);
        }

        private boolean isCoordTag(String tagName) {
            return tagName.equalsIgnoreCase("coord") || tagName.equalsIgnoreCase("gx:coord");
        }

        private boolean isWhenTag(String tagName) {
            return tagName.equalsIgnoreCase("when");
        }

        // Called when tag closing
        @Override
        public void endElement(String uri, String localName, String qName)
                throws SAXException {

            currentElement = false;

            /** set value */
            if (isWhenTag(localName) || isWhenTag(qName)) {
                DateTimeFormatter parser = ISODateTimeFormat.dateTimeParser();
                DateTime date = parser.parseDateTime(currentValue);
                currentdate = date.getMillis();
            }
            if (isCoordTag(localName) || isCoordTag(qName)) {
                String[] coords = currentValue.split(" ");
                if (coords.length < 2) {
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
}

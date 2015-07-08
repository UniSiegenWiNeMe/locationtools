package de.unisiegen.locationtools.db

/**
 * Created by lars on 09/07/15.
 */
class InfluxConnectorTest extends groovy.util.GroovyTestCase {
InfluxConnector db;

    @org.junit.Test
    public void testGetAllClusterLocs() throws Exception {


    }

    @org.junit.Before
    public void setUp() throws Exception {
        db = new InfluxConnector();
        db.openDB();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        db.closeDB();

    }
}

package de.unisiegen.locationtools;

import static spark.Spark.*;

/**
 * Created by brodo on 08.07.15.
 */
public class Main {
    public static void main(String[] args) {
        get("/hello", (req, res) -> "Hello World");
    }
}

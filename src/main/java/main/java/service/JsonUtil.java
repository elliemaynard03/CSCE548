package main.java.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Utility class for JSON serialization/deserialization.
 * Uses Gson library configured with pretty printing.
 */
public class JsonUtil {

    // Static Gson instance accessible everywhere
    public static final Gson gson =
            new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
}

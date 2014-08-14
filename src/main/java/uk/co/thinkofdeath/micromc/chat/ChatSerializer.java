package uk.co.thinkofdeath.micromc.chat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatSerializer {

    private static final Gson gson = attach(new GsonBuilder())
            .create();

    public static String toString(Component component) {
        return gson.toJson(component, Component.class);
    }

    public static Component fromString(String str) {
        return gson.fromJson(str, Component.class);
    }

    public static GsonBuilder attach(GsonBuilder builder) {
        builder.registerTypeAdapter(Component.class, new ComponentSerializer());
        return builder;
    }
}

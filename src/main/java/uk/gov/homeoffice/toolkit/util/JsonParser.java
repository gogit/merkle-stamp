package uk.gov.homeoffice.toolkit.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class JsonParser<T> {

    public String asPrettyString(T type) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(type);
    }

    public String asPrettyStringJsonObject(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(gson.toJson(json, JsonObject.class));
    }

    public T asType(String json, Class<T> claz) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(json, claz);
    }

}

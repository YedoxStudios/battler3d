package io.yedox.imagine3d.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;

public class EntityData {
    private Gson gson;
    private HashMap<String, Object> entityDataMap;

    public EntityData() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        entityDataMap = new HashMap<>();
    }

    public void putKey(String key, Object value) {
        entityDataMap.put(key, value);
    }

    public <T> T getKey(String key) {
        return (T) entityDataMap.get(key);
    }

    public String toJson() {
        return gson.toJson(entityDataMap);
    }

    public void fromJson(String json) {
        entityDataMap = new Gson().fromJson(json, new TypeToken<HashMap<String, Object>>() {}.getType());
    }

    public void printEntityData() {
        entityDataMap.forEach((key, value) -> System.out.println(key + " " + value));
    }
}

package com.yzk18.commons;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;

public class FileJsonConverter  implements JsonSerializer<File>, JsonDeserializer<File> {

    public FileJsonConverter() {
    }

    public JsonElement serialize(File src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    public File deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new File(json.getAsString());
    }
}

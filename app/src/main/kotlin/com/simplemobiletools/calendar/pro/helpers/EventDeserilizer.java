package com.simplemobiletools.calendar.pro.helpers;


import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.simplemobiletools.calendar.pro.models.Event;

import java.lang.reflect.Type;

public  class EventDeserilizer implements JsonDeserializer<Event> {


    @Override
    public Event deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Event event = new Gson().fromJson(json, Event.class);
        JsonObject jsonObject = json.getAsJsonObject();

        if (jsonObject.has("repetition_exceptions")) {
            JsonElement elem = jsonObject.get("repetition_exceptions");
            if (elem != null && !elem.isJsonNull()) {
                String valuesString = elem.getAsString();
                if (!TextUtils.isEmpty(valuesString)){
                     final Converters __converters = new Converters();
                     event.setRepetitionExceptions(__converters.jsonToStringList(valuesString));
                }
            }
            JsonElement elemid = jsonObject.get("id");
            if (elemid != null && !elemid.isJsonNull()) {
                String valuesString = elemid.getAsString();
                if (!TextUtils.isEmpty(valuesString)){
                   event.setId(null);
                }
            }
        }
        return event ;
    }
}

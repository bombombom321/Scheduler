package com.simplemobiletools.calendar.pro.helpers;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.simplemobiletools.calendar.pro.R;
import com.simplemobiletools.calendar.pro.net.AsyncHttpTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Global {

    public static Context mod = null;

    public static Map getJsonObject(String content){
        try {

            Map<String, Object> mp = new HashMap<String, Object>();

            JSONObject obj = new JSONObject(content);

            mp.putAll(jsonToMap(obj, mp));
            //map.put(mp.get(0).toString(), mp.get(1).toString());

            return mp;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Map jsonToMap(JSONObject json, Map retMap) throws JSONException {
//		Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            }

            else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }




}

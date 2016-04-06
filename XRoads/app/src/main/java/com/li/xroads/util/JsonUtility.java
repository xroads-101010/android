package com.li.xroads.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by nagnath_p on 3/9/2016.
 */
public class JsonUtility {

    public <T> T jsonToObject(String json, Class<T> type){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        T object = gson.fromJson(json, type);
        return object;
    }


 /*   public <T> T jsonArrayToObjectList(JSONArray jsonArr, Class<T> type) throws ClassNotFoundException {

        Type listType = new TypeToken<ArrayList<?>>(){}.getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        //T object = gson.fromJson(json, type);
        return null;
    }
*/
}

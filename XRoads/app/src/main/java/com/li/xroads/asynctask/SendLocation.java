package com.li.xroads.asynctask;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.li.xroads.helper.HTTPResponse;
import com.li.xroads.helper.RestClient;
import com.li.xroads.util.Constant;
import com.li.xroads.util.PropertiesReader;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sony on 28-02-2016.
 */
public class SendLocation extends AsyncTask<String, Void, HTTPResponse> {

    private static final String TAG = "SendLocation";
    @Override
    protected HTTPResponse doInBackground(String... params) {
        JSONObject locJson = new JSONObject();
        try {

            Log.e(TAG, "In doInBackground");
            locJson.put(Constant.TRIP_ID, params[0]);
            locJson.put(Constant.USER_ID, params[1]);
            locJson.put(Constant.Latitude, params[2]);
            locJson.put(Constant.Longitude, params[3]);
            Log.e(TAG, "In doInBackground ::" + locJson);

        } catch (JSONException ex) {
            Log.i(TAG, "In doInBackground :: EX: " + ex.getMessage());
        }
        RestClient client = new RestClient();
        return client.doPost(null, locJson);
    }

    @Override
    protected void onPostExecute(HTTPResponse result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

}

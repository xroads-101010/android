package com.li.xroads.activity;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.li.xroads.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.li.xroads.fragment.LocationFragment;
import com.li.xroads.helper.HTTPResponse;
import com.li.xroads.helper.RestClient;
import com.li.xroads.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TrackingMapsActivity extends AppCompatActivity implements LocationFragment.OnFragmentInteractionListener, OnMapReadyCallback {
    private static int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;


    private GoogleMap googleMap;
    private String userId = "user1";
    HashMap<String, Marker> markerMap = null;
    Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking_maps);

        LocationFragment locationFragment = LocationFragment.newInstance("user1", "trip1");
        getFragmentManager().beginTransaction().add(android.R.id.content, locationFragment).commit();
        if (!checkGooglePlayServices()) {
            finish();
        }
        MapFragment mapFragment = ((MapFragment) getFragmentManager().findFragmentById(R.id.map));
        mapFragment.getMapAsync(this);

        callAsynchronousTask();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        initializeMap();
    }


    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void callAsynchronousTask() {
        timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            final Handler handler = new Handler();
            int count = 1;

            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            ReceiveLocation receiveLocation = new ReceiveLocation();
                            receiveLocation.doInBackground();
                            RestClient client = new RestClient();
                            HashMap<String, String> queryParam = new HashMap<>();
                            queryParam.put("tripId", "trp1");
                            //client.doGet(null, queryParam);
                            JSONArray locations = new JSONArray();
                            JSONObject j1 =new JSONObject();
                            j1.put(Constant.Latitude, "28.61");
                            j1.put(Constant.Longitude, "77.2099");
                            j1.put(Constant.USER_ID, "user2");
                            locations.put(j1);

                            JSONObject j2 =new JSONObject();
                            j2.put(Constant.Latitude, "7.000");
                            j2.put(Constant.Longitude, "81.0000");
                            j2.put(Constant.USER_ID, "user3");
                            locations.put(j2);
                            JSONObject j3 =new JSONObject();
                            j3.put(Constant.Latitude, "24.000");
                            j3.put(Constant.Longitude, "45.000");
                            j3.put(Constant.USER_ID, "user4");
                            locations.put(j3);

                            markLocations(locations);

                            count++;
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(doAsynchronousTask, 0, 50000); //execute in every 50000 ms
    }

    private void initializeMap() {
        googleMap.setMyLocationEnabled(true);
        markerMap = new HashMap<String, Marker>();
        googleMap.clear();
    }

    private void clearMap() {
        googleMap.setMyLocationEnabled(true);
        markerMap = null;
        googleMap.clear();
    }

    public void markLocations(JSONArray locations) {
        if (googleMap != null && locations != null) {
            for (int i = 0; i < locations.length(); i++) {
                try {
                    Double lat = Double.valueOf(locations.getJSONObject(i).getString(Constant.Latitude));
                    Double lang = Double.valueOf(locations.getJSONObject(i).getString(Constant.Longitude));
                    String userId = locations.getJSONObject(i).getString(Constant.USER_ID);
                    LatLng loc = new LatLng(lat, lang);
                    populateMarker(userId, loc);
                } catch (JSONException ex) {
                }

            }
            zoomOnMap();
        }
    }

    private Marker populateMarker(String userId, LatLng loc) {
        Marker marker;
        if (markerMap.containsKey(userId)) {
            marker = markerMap.get(userId);
            marker.setPosition(loc);
        } else {
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(loc)
                    .title(userId)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        }
        Toast.makeText(this, "Populated", Toast.LENGTH_SHORT).show();
        markerMap.put(userId, marker);
        return marker;
    }

    protected void createSelfMarker(double latitude, double longitude, String title) {
        LatLng loc = new LatLng(latitude, longitude);
        populateMarker(userId, loc);
        zoomOnMap();
    }

    private void zoomOnMap() {
        LatLngBounds.Builder cameraZoom = new LatLngBounds.Builder();
        for (String key : markerMap.keySet()) {
            cameraZoom.include(markerMap.get(key).getPosition());
        }
        final CameraUpdate cu =CameraUpdateFactory.newLatLngBounds(cameraZoom.build(), 50);
        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                googleMap.animateCamera(cu);

            }
        });
    }

    private boolean checkGooglePlayServices() {
        int checkGooglePlayServices = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
            GooglePlayServicesUtil.getErrorDialog(checkGooglePlayServices,
                    this, REQUEST_CODE_RECOVER_PLAY_SERVICES).show();

            return false;
        }
        return true;

    }

    @Override
    public void onFragmentInteraction(Location location) {
        createSelfMarker(location.getLatitude(), location.getLongitude(), userId);
    }

    private class ReceiveLocation {

        private static final String TAG = "ReceiveLocation";

        protected JSONObject doInBackground(String... params) {
            JSONObject responseJson= new JSONObject();
            try {
                RestClient client = new RestClient();
                HashMap<String, String> queryParam = new HashMap<>();
                queryParam.put("tripId", "trp1");
                HTTPResponse result = client.doGet(null, queryParam);
                if (result.getStatus() == HttpURLConnection.HTTP_OK) {
                    responseJson = new JSONObject(result.getResponse());
                }

            }catch(JSONException ex){

            }catch(Exception ex){

            }
            return responseJson;
        }

        protected void onPostExecute(String result) {
            try {
                if (result != null) {
                    JSONObject response = new JSONObject(result);
                    JSONArray locations = new JSONArray();
                    locations.put(new JSONObject());
                    locations.put(new JSONObject());
                    locations.put(new JSONObject());
                    locations.put(new JSONObject());

                    markLocations(locations);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void onPreExecute() {
        }

        protected void onProgressUpdate(Void... values) {
        }

    }


}

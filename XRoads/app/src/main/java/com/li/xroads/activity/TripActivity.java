package com.li.xroads.activity;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.li.xroads.R;
import com.li.xroads.adapter.TripAdapter;
import com.li.xroads.domain.Trip;
import com.li.xroads.domain.User;
import com.li.xroads.fragment.TripFragment;
import com.li.xroads.helper.HTTPResponse;
import com.li.xroads.helper.RestClient;
import com.li.xroads.util.Constant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;


public class TripActivity extends AppCompatActivity implements ListView.OnItemClickListener {

    private ActionBar actionBar;
    private HashMap<Integer, JSONObject> tripsMap = new HashMap<Integer, JSONObject>();
    private ArrayList<Trip> tripsList = null;
    private TripAdapter adapter = null;
    private int userId;
    private ListView tripListView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        userId = getIntent().getExtras().getInt(Constant.USER_ID);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        tripListView = (ListView) findViewById(R.id.tripList);
        tripListView.setOnItemClickListener(this);
        loadTripList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTrip();
            }
        });
    }

    private void loadTripList() {
        tripsList = new ArrayList<Trip>();
        TripListAsync tripListAsync = new TripListAsync();
        tripListAsync.execute(String.valueOf(userId));
        adapter = new TripAdapter(TripActivity.this, tripsList);
        tripListView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_refresh:
                loadTripList();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void createTrip() {
        Intent intent = new Intent(TripActivity.this, TripCreateActivity.class);
        intent.setAction(Constant.ACTION_CREATE);
        intent.putExtra(Constant.USER_ID, userId);
        startActivityForResult(intent, Constant.Trip.CREATE_TRIP_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == Constant.Trip.CREATE_TRIP_REQUEST) {
            if (intent != null) {
                Trip trip = intent.getParcelableExtra(Constant.Trip.TRIP_PARCEL_URI);
                if (trip != null) {
                    tripsList.add(trip);
                    adapter.notifyDataSetChanged();
                }
            }
        } else if (requestCode == Constant.Trip.UPDATE_TRIP_REQUEST) {

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        final Trip item = (Trip) parent.getItemAtPosition(position);
        Intent intent = new Intent(TripActivity.this, TripCreateActivity.class);
        intent.setAction(Constant.ACTION_UPDATE);
        intent.putExtra(Constant.Trip.TRIP_PARCEL_URI, item);
        startActivityForResult(intent, Constant.Trip.UPDATE_TRIP_REQUEST);
    }

    public class TripListAsync extends AsyncTask<String, Void, HTTPResponse> {
        private ProgressDialog pDialog;
        private static final String TAG = "TripListAsync";

        @Override
        protected HTTPResponse doInBackground(String... params) {

            Log.e(TAG, "In doInBackground");
            String userId = params[0];
            RestClient client = new RestClient();
            HashMap<String, String> queryParam = new HashMap<String, String>();
            queryParam.put("userId", userId);
            return client.doGet("trip/all", queryParam);
        }

        @Override
        protected void onPostExecute(HTTPResponse result) {
            pDialog.dismiss();
            try {
                if (result.getStatus() == HttpURLConnection.HTTP_OK) {
                    JSONObject jsonResp = result.getResponseAsJson();
                    if (jsonResp != null) {
                        JSONArray trips = jsonResp.getJSONArray(Constant.TRIPS_KEY);
                        Type listType = new TypeToken<ArrayList<Trip>>() {
                        }.getType();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        ArrayList<Trip> tripsForUser = gsonBuilder.create().fromJson(trips.toString(), listType);

                        tripsList.addAll(tripsForUser);
                        adapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException ex) {

            }
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(TripActivity.this);
            pDialog.setTitle("Trips");
            pDialog.setMessage("Fetching trips ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

    }


    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }
}

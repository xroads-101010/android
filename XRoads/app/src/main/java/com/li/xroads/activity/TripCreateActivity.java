package com.li.xroads.activity;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.model.LatLng;
import com.li.xroads.R;
import com.li.xroads.domain.Trip;
import com.li.xroads.domain.TripMember;
import com.li.xroads.domain.User;
import com.li.xroads.fragment.DatePickerFragment;
import com.li.xroads.fragment.TripFragment;
import com.li.xroads.helper.HTTPResponse;
import com.li.xroads.helper.RestClient;
import com.li.xroads.util.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;


public class TripCreateActivity extends AppCompatActivity implements View.OnClickListener, DatePickerFragment.DatePickerDialogListener
        {
    private static final String TAG = "TripCreateActivity";

    ActionBar actionBar;
    EditText nameEdit;
    EditText startDateEdit;
    EditText endDateEdit;
    Button saveButton;
    Button startDateButton;
    Button endDateButton;
    Place startPlace;
    Place endPlace;
    Trip trip = null;
    private String action;
    private int userId;

    private GregorianCalendar startDate;
    private GregorianCalendar endDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_create);
        action = getIntent().getAction();
        userId = getIntent().getExtras().getInt(Constant.USER_ID);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        nameEdit = (EditText) findViewById(R.id.tripCreateActNameEdit);
        startDateEdit = (EditText) findViewById(R.id.tripCreateActStartDateEdit);
        endDateEdit = (EditText) findViewById(R.id.tripCreateActEndDateEdit);
        saveButton = (Button) findViewById(R.id.tripCreateSaveButton);
        saveButton.setOnClickListener(this);
        startDateButton = (Button) findViewById(R.id.startDateButton);
        startDateButton.setOnClickListener(this);
        endDateButton = (Button) findViewById(R.id.endDateButton);
        endDateButton.setOnClickListener(this);


        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, R.array.city_arr);
        PlaceAutocompleteFragment autoCompleteStartPointFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.tripCreateActStartPointFragment);

        autoCompleteStartPointFragment.setHint("Select Start Point");
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_CITIES)
                .build();
        autoCompleteStartPointFragment.setFilter(typeFilter);
        autoCompleteStartPointFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                startPlace = place;
            }
            @Override
            public void onError(Status status) {
            }
        });

        PlaceAutocompleteFragment autoCompleteEndPointFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.tripCreateActEndPointFragment);
        autoCompleteEndPointFragment.setHint("Select End Point");
        autoCompleteEndPointFragment.setFilter(typeFilter);
        autoCompleteEndPointFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                endPlace = place;
            }
            @Override
            public void onError(Status status) {
            }
        });


        if(Constant.ACTION_UPDATE.equals(action)){
            trip = getIntent().getParcelableExtra(Constant.Trip.TRIP_PARCEL_URI);
            nameEdit.setText(trip.getName());
            startDateEdit.setText(String.valueOf(trip.getStartDate()));
            endDateEdit.setText(String.valueOf(trip.getEndDate()));
            autoCompleteStartPointFragment.setText(trip.getStartLocation());
            autoCompleteEndPointFragment.setText(trip.getDestination());
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tripCreateSaveButton:
                registerTrip();
                break;
            case R.id.startDateButton:
                showDatePickerDialog(v);
            case R.id.endDateButton:
                showDatePickerDialog(v);
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_trip_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = DatePickerFragment.newInstance(view);
        newFragment.show(getFragmentManager(), "DatePicker");
    }

    @Override
    public void onFragmentInteraction(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
        int tagValue = (int) view.getTag();
        switch (tagValue) {
            case R.id.startDateButton:
                startDate = new GregorianCalendar(selectedYear,selectedMonth,selectedDay);
                startDateEdit.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
                break;
            case R.id.endDateButton:
                endDate = new GregorianCalendar(selectedYear,selectedMonth,selectedDay);
                endDateEdit.setText(selectedDay + " / " + (selectedMonth + 1) + " / "
                        + selectedYear);
                break;

        }
    }

    private void registerTrip() {
        if(trip == null) {
            trip = new Trip();
        }
        trip.setId(0);
        trip.setName(nameEdit.getText().toString());
        trip.setStartLocation(String.valueOf(startPlace.getName()));
        LatLng startLatLng = startPlace.getLatLng();
        trip.setStartLocationLat(String.valueOf(startLatLng.latitude));
        trip.setStartLocationLong(String.valueOf(startLatLng.longitude));
        trip.setDestination(String.valueOf(endPlace.getName()));
        LatLng endLatLng = endPlace.getLatLng();
        trip.setDestinationLat(String.valueOf(endLatLng.latitude));
        trip.setDestinationLong(String.valueOf(endLatLng.longitude));
        trip.setChampionUserId(userId);
        trip.setStartDate(startDate.getTimeInMillis());
        trip.setEndDate(endDate.getTimeInMillis());
        trip.setTripMemberList(new ArrayList<TripMember>());
        //startIntent(null);
        //trip.setStartDate(startDateEdit.getText().toString());
        RegisterTripAsync registerUserAsync = new RegisterTripAsync();
        registerUserAsync.execute(trip);
    }

    private void startIntent(HTTPResponse result) {
       if (result.getStatus() == HttpURLConnection.HTTP_OK || result.getStatus() == HttpURLConnection.HTTP_CREATED) {
        Intent intent = new Intent();
        intent.putExtra(Constant.Trip.TRIP_PARCEL_URI, trip);
        setResult(Constant.Trip.CREATE_TRIP_REQUEST, intent);
        finish();
        }
        Toast.makeText(this, result.getResponse(), Toast.LENGTH_LONG).show();
    }

    public class RegisterTripAsync extends AsyncTask<Trip, Void, HTTPResponse> {
        private ProgressDialog pDialog;
        private static final String TAG = "RegisterTripAsync";

        @Override
        protected HTTPResponse doInBackground(Trip... params) {
            JSONObject tripJson = new JSONObject();
            try {

                Log.e(TAG, "In doInBackground");
                Trip trip = params[0];
                tripJson = trip.asJson();
                Log.e(TAG, "In doInBackground ::" + tripJson);

            } catch (JSONException ex) {
                Log.i(TAG, "In doInBackground :: EX: " + ex.getMessage());
            }
            RestClient client = new RestClient();
            /*String endPoint= "trip"
            if(trip.getId()!= 0 ){

            }*/
            return client.doPost("trip", tripJson);
        }

        @Override
        protected void onPostExecute(HTTPResponse result) {
            pDialog.dismiss();
            startIntent(result);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(TripCreateActivity.this);
            pDialog.setTitle("Trip Registration");
            pDialog.setMessage("Creating trip ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

    }
}

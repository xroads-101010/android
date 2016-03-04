package com.li.xroads.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.li.xroads.R;
import com.li.xroads.domain.User;
import com.li.xroads.helper.HTTPResponse;
import com.li.xroads.helper.RestClient;
import com.li.xroads.listener.TextValidator;
import com.li.xroads.util.Constant;
import com.li.xroads.util.NetworkConnectionUtility;
import com.li.xroads.util.Validation;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class RegisterActivity extends Activity implements View.OnClickListener {

    Button registerButton;
    EditText emailEdit;
    EditText mobileEdit;
    EditText nameEdit;
    EditText passwordEdit;
    TextView loginScreenLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (!NetworkConnectionUtility.isInternetConnected(this)) {
            Toast.makeText(this, "Internet is not connected, please check connection", Toast.LENGTH_LONG).show();
            return;
        }
        loginScreenLink = (TextView) findViewById(R.id.regiActLinkToLogin);
        loginScreenLink.setOnClickListener(this);
        registerView();
    }

    private void registerView() {
        registerButton = (Button) findViewById(R.id.regiActRegisterButton);
        emailEdit = (EditText) findViewById(R.id.regiActEmailEdit);
        emailEdit.addTextChangedListener(new TextValidator(emailEdit) {
            @Override
            public void validate(EditText textView, String text) {
                Validation.isEmailAddress(textView, false);
            }
        });
        mobileEdit = (EditText) findViewById(R.id.regiActMobileEdit);
        mobileEdit.addTextChangedListener(new TextValidator(mobileEdit) {
            @Override
            public void validate(EditText textView, String text) {
                Validation.isPhoneNumber(textView, true);
            }
        });
        nameEdit = (EditText) findViewById(R.id.regiActNameEdit);
        nameEdit.addTextChangedListener(new TextValidator(nameEdit) {
            @Override
            public void validate(EditText textView, String text) {
                Validation.hasText(textView);
            }
        });
        passwordEdit = (EditText) findViewById(R.id.regiActPasswordEdit);
        passwordEdit.addTextChangedListener(new TextValidator(passwordEdit) {
            @Override
            public void validate(EditText textView, String text) {
                Validation.hasText(textView);
            }
        });
        registerButton.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.regiActRegisterButton:
                if (checkValidation()) {
                    registerUser();
                } else {
                    Toast.makeText(RegisterActivity.this, "Form contains error", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.regiActLinkToLogin:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void startIntent(HTTPResponse result) {
        if (result.getStatus() == HttpURLConnection.HTTP_OK || result.getStatus() == HttpURLConnection.HTTP_CREATED) {
            Intent intent = intent = new Intent(this, OtpActivity.class);
            startActivity(intent);
        }
        Toast.makeText(this, result.getResponse(), Toast.LENGTH_LONG).show();
    }

    private boolean checkValidation() {
        boolean ret = true;

        if (!Validation.hasText(nameEdit)) ret = false;
        if (!Validation.isEmailAddress(emailEdit, false)) ret = false;
        if (!Validation.isPhoneNumber(mobileEdit, true)) ret = false;
        if (!Validation.hasText(passwordEdit)) ret = false;

        return ret;
    }

    private void registerUser() {
        User user = new User();
        user.setEmail(emailEdit.getText().toString());
        user.setUserMobile(mobileEdit.getText().toString());
        user.setUserName(nameEdit.getText().toString());
        user.setPassword(passwordEdit.getText().toString());
        RegisterUserAsync registerUserAsync = new RegisterUserAsync();
        registerUserAsync.execute(user);
    }


    public class RegisterUserAsync extends AsyncTask<User, Void, HTTPResponse> {
        private ProgressDialog pDialog;
        private static final String TAG = "RegisterUserAsync";

        @Override
        protected HTTPResponse doInBackground(User... params) {
            JSONObject userJson = new JSONObject();
            try {

                Log.e(TAG, "In doInBackground");
                User user = params[0];
                userJson.put(Constant.EMAIL_KEY, user.getEmail());
                userJson.put(Constant.USER_MOBILE_KEY, user.getUserMobile());
                userJson.put(Constant.USER_NAME_KEY, user.getUserName());
                userJson.put(Constant.PASSWORD_KEY, user.getPassword());
                userJson.put(Constant.IS_REGISTERED_KEY, Boolean.TRUE);
                Log.e(TAG, "In doInBackground ::" + userJson);

            } catch (JSONException ex) {
                Log.i(TAG, "In doInBackground :: EX: " + ex.getMessage());
            }
            RestClient client = new RestClient();
            return client.doPost("user", userJson);
        }

        @Override
        protected void onPostExecute(HTTPResponse result) {
            finish();
            startIntent(result);
        }

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(RegisterActivity.this);
            pDialog.setTitle("User Registration");
            pDialog.setMessage("Registering user ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            pDialog.setMessage("Loading User Space");
            pDialog.setTitle("Getting Data");
        }

    }

}

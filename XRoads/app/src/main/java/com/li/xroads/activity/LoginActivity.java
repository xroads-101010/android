package com.li.xroads.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.li.xroads.R;
import com.li.xroads.fragment.InternetCheckDialogFragment;
import com.li.xroads.util.Constant;
import com.li.xroads.util.NetworkConnectionUtility;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener ,InternetCheckDialogFragment.InternetCheckDialogListener{

    Button loginButton;
    TextView registerScreenLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login);
        hideSoftKeyboard();
        if (!NetworkConnectionUtility.isInternetConnected(this)) {
            DialogFragment dialog = new InternetCheckDialogFragment();
            dialog.show(getFragmentManager(), "InternetCheckFragment");
            return;
        }

        loginButton = (Button) findViewById(R.id.loginActLoginButton);
        loginButton.setOnClickListener(this);
        registerScreenLink = (TextView) findViewById(R.id.loginActLinkToRegister);
        registerScreenLink.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.loginActLoginButton:
                intent = new Intent(this, TripActivity.class);
                intent.putExtra(Constant.USER_ID, 3);
                break;
            case R.id.loginActLinkToRegister:
                intent = new Intent(this, RegisterActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }


    @Override
    public void onDialogOkClick(DialogFragment dialog) {
        return;
    }

    public void hideSoftKeyboard() {
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
        );
    }



}

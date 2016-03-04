package com.li.xroads.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.li.xroads.R;

public class LoginActivity extends Activity implements View.OnClickListener {

    Button loginButton;
    TextView registerScreenLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                intent = new Intent(this, TrackingMapsActivity.class);
                break;
            case R.id.loginActLinkToRegister:
                intent = new Intent(this, RegisterActivity.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }
}

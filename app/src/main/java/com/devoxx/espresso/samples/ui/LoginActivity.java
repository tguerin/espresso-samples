package com.devoxx.espresso.samples.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.crashlytics.android.Crashlytics;
import com.devoxx.espresso.samples.R;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;


public class LoginActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.inject(this);
        Fabric.with(this, new Crashlytics());
    }

    @OnClick(R.id.login_btn)
    public void onLoginBtnClicked() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}

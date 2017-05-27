package com.hacktm.kidlocation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    public static final String HACK_TM_KID_LOCATOR = "hackTmKidLocator";
    public static final String TAG = "HACK_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subscribeToTopics();
    }

    private void subscribeToTopics() {
        FirebaseMessaging.getInstance().subscribeToTopic(HACK_TM_KID_LOCATOR);
        Log.d(TAG, "Subscribed to topic:" + HACK_TM_KID_LOCATOR);
    }
}

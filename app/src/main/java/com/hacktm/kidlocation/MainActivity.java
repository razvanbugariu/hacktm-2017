package com.hacktm.kidlocation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    public static final String HACK_TM_KID_LOCATOR = "hackTmKidLocator";
    public static final String TAG = "HACK_TAG";

    private EditText xText;
    private EditText yText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        subscribeToTopics();
    }

    private void initComponents() {
        xText = (EditText) findViewById(R.id.xEditText);
        yText = (EditText) findViewById(R.id.yEditText);
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCoordinates();
            }
        });
    }

    private void saveCoordinates() {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
        editor.putString("xCoord", xText.getText().toString());
        editor.putString("yCoord", yText.getText().toString());
        editor.commit();

        if(!PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("xCoord", "10").equals("10")) {
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
        }
    }

    private void subscribeToTopics() {
        FirebaseMessaging.getInstance().subscribeToTopic(HACK_TM_KID_LOCATOR);
        Log.d(TAG, "Subscribed to topic:" + HACK_TM_KID_LOCATOR);
    }
}

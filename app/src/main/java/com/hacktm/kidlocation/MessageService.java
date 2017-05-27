package com.hacktm.kidlocation;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by rbu on 5/27/17.
 */

public class MessageService extends FirebaseMessagingService {

    private static final String TAG = "HACK_TAG";
    private int TAKE_PHOTO_CODE = 0;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "MESSAGE RECEIVED");
        goToPhotoActivity();
    }

    private void goToPhotoActivity() {
        Intent intent = new Intent(getApplicationContext(), PhotoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}

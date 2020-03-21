package com.cpur.notifications;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.HashMap;
import java.util.Map;

public class CpurFirebaseInstanceIDService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);
    }


    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        String uid = FirebaseAuth.getInstance().getUid();
        if (uid != null) {
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            DatabaseReference databaseReference = mDatabase.child("users").child(uid);
            Map<String, Object> stringObjectMap = new HashMap<>();
            stringObjectMap.put("notificationToken", token);
            databaseReference.updateChildren(stringObjectMap);
        }

    }

}

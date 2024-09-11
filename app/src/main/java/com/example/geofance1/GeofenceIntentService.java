package com.example.geofance1;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

public class GeofenceIntentService extends IntentService {

    public GeofenceIntentService(){
        super("Hello");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if(geofencingEvent.hasError()){
            Toast.makeText(this, "There is some error", Toast.LENGTH_SHORT).show();
            return ;
        }
        int geofenceTransition = geofencingEvent.getGeofenceTransition();
        if(geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL){
            Toast.makeText(this, "Dwell", Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.geofance1;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private FusedLocationProviderClient fusedLocationProviderClient ;
//    private GeofencingClient geofencingClient ;
//    private PendingIntent pendingIntent ;
    private final int minimum_radious = 100 ; 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        if(ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);
            geofencingClient.addGeofences(createGeoFancingRequest() , createGeofencePendingIntent())
                    .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failure" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
        }

//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
//        geofencingClient = LocationServices.getGeofencingClient(this);



//        public Geofence createGeofence(double latitude; double longitude ; float radius; String geofenceId;) {
//            return new Geofence.Builder().setRequestId(geofenceId)
//                    .setCircularRegion(latitude, longitude, radius)
//                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
//                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
//                    .build();
//        }

//        private GeofencingRequest createGeofencingRequest(Geofence geofence) {
//            return new GeofencingRequest.Builder()
//                    .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
//                    .addGeofence(geofence)
//                    .build();
//        }

    }

    private List<Geofence> createGeoFancingListt() {
        List<Geofence> geofenceList = new ArrayList<>();
        geofenceList.add(new Geofence.Builder()
                .setRequestId("Kb GeoFenceLocation")
                .setCircularRegion(
                        47.6062 , // latitude
                        122.3321 , // longitude
                        minimum_radious
                )
                .setLoiteringDelay(5000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL)
                .build()
        );

        return  geofenceList;
    }

    private GeofencingRequest createGeoFancingRequest() {
       GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
               builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL);
               builder.addGeofences(createGeoFancingListt());

               return  builder.build();


    }

    private PendingIntent createGeofencePendingIntent() {
        Intent intent = new Intent(this , GeofenceIntentService.class);
        return PendingIntent.getService(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        GeofencingClient geofencingClient = LocationServices.getGeofencingClient(this);
        geofencingClient.removeGeofences(createGeofencePendingIntent())
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Stop Success", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Stop Failure", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    }

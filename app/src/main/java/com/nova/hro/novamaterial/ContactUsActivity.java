package com.nova.hro.novamaterial;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ContactUsActivity extends AppCompatActivity implements OnMapReadyCallback {
    SupportMapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng latLng = new LatLng(19.18683476, 72.97420353);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        googleMap.moveCamera(cameraUpdate);
        googleMap.addMarker(new MarkerOptions().position(latLng).title("Nova Head Office").snippet("007 Siddharth Darshan, Dada Patil Wadi\n" +
                "Dada Patil Marg.\n" +
                "Near Thane West station\n" +
                "THANE. 400 601\n" +
                "IndiaTel: +9122 4123 8052"));
    }

    public void launchWebsite(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://novahro.com")));
    }

    public void contactCall(View v) {
        //startActivity(new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:+9168886888")));
    }

    public void contactEmail(View v) {
    }

}
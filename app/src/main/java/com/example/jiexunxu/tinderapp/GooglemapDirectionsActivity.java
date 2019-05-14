package com.example.jiexunxu.tinderapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by Jiexun Xu on 3/29/2018.
 *
 * Unused right now
 */

class GooglemapDirectionsActivity extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private GoogleMap map;

    private ArrayList<YelpPlace> places;
    private int selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_googlemap_directions);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        final Button backButton=findViewById(R.id.googleMapBackButton);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GooglemapDirectionsActivity.this, SlideShowActivity.class);
                GooglemapDirectionsActivity.this.startActivity(intent);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(null);
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        Bundle args=getIntent().getBundleExtra("bundle");
        places=(ArrayList<YelpPlace>)args.getSerializable("places");
        selectedPlace=getIntent().getIntExtra("selectedPlace", -1);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMinZoomPreference(15);
        YelpPlace place=places.get(selectedPlace);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(place.lat, place.lng)));
        map.animateCamera( CameraUpdateFactory.zoomTo( 17.0f ) );
        map.addMarker(new MarkerOptions().title(place.name).position(new LatLng(place.lat, place.lng)));
    }
}

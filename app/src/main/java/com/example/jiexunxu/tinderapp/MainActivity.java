package com.example.jiexunxu.tinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageButton;

public class MainActivity extends AppCompatActivity implements LocationListener{
    // GPS location related variables
    private Location lastKnownLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SettingsParams settings=new SettingsParams();
        settings.readSettingsFromFile(this.getApplicationContext());
        initUI();
        initMainActivityButtons();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }

    private void initUI(){
        setTitle("Pick a category");
        ConstraintLayout layout=findViewById(R.id.mainLayout);
        final Button customSearchButton=findViewById(R.id.mainPageCustomzieSearchButton);
        AppOptions opts=AppOptions.getUIOptions(SettingsParams.themeID);
        layout.setBackgroundColor(getApplicationContext().getResources().getColor(opts.backgroundColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(opts.primaryColor)));
        customSearchButton.setBackgroundResource(opts.buttonStyle);
    }

    private void initMainActivityButtons(){
        final GifImageButton restaurantGifButton=findViewById(R.id.restaurantGif);
        final GifImageButton shoppingGifButton=findViewById(R.id.shoppingGif);
        final GifImageButton hotelsGifButton=findViewById(R.id.hotelsGif);
        final GifImageButton fitnessGifButton=findViewById(R.id.fitnessGif);
        final GifImageButton entertainmentGifButton=findViewById(R.id.entertainmentGif);
        final GifImageButton beautyGifButton=findViewById(R.id.beautyGif);
        final GifImageButton nightlifeGifButton=findViewById(R.id.nightlifeGif);
        final GifImageButton petsGifButton=findViewById(R.id.petsGif);
        final GifImageButton foodDeliveryButton=findViewById(R.id.foodDeliveryGif);
        final Button customSearchButton=findViewById(R.id.mainPageCustomzieSearchButton);

        customSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
                MainActivity.this.startActivity(intent);
            }
        });

        restaurantGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("food,restaurants");
            }
        });

        shoppingGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("shopping");
            }
        });

        hotelsGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("hotelstravel");
            }
        });

        fitnessGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("active");
            }
        });

        entertainmentGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("arts");
            }
        });

        beautyGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("beautysvc,health");
            }
        });

        nightlifeGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("nightlife");
            }
        });

        petsGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("pets");
            }
        });

        foodDeliveryButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("fooddelivery");
            }
        });

        /*
        final GifImageButton servicesGifButton=findViewById(R.id.entertainmentGif);
        servicesGifButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startSearch("homeservices,localservices,professional,publicservicesgovt");
            }
        });
        */
    }

    private void startSearch(String category){
        SettingsParams settings=new SettingsParams();
        settings.readSettingsFromFile(this.getApplicationContext());
        YelpFusionParams params=settings.settingsToYelpParams();
        if(category.equals("fooddelivery"))
            params.mustHaveFoodDelivery=true;
        else
            params.setCategories(category);
        if(!params.getParams().containsKey("location")){
            try {
                getDeviceLocation();
                params.setLatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
            }catch(Exception ex){
                ErrorActivity.start(MainActivity.this, "GPS service unavailable. Please specify a search address in advanced search to search around that area.");
            }
        }
        Intent intent=new Intent(MainActivity.this, WaitSearchActivity.class);
        intent.putExtra("params", params);
        MainActivity.this.startActivity(intent);
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            String location_context = Context.LOCATION_SERVICE;
            LocationManager lm = (LocationManager) getSystemService(location_context);
            List<String> providers = lm.getProviders(true);
            for (String provider : providers) {
                lm.requestLocationUpdates(provider, 1000, 0, new LocationListener() {
                    public void onLocationChanged(Location location) {
                        lastKnownLocation=location;
                    }

                    public void onProviderDisabled(String provider) {}

                    public void onProviderEnabled(String provider) {}

                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                });
                Location l=lm.getLastKnownLocation(provider);
                if (lastKnownLocation == null || (l!=null && l.getAccuracy() < lastKnownLocation.getAccuracy())) {
                    lastKnownLocation = l;
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onLocationChanged(Location loc) {
        lastKnownLocation = loc;
        if(SettingsParams.debugMode) {
            String Text = "My current location is: " + "Latitud = "
                    + loc.getLatitude() + "Longitud = " + loc.getLongitude();
            Log.d("loc", "onLocationChanged" + Text);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}
}

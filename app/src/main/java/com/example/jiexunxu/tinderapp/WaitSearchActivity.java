package com.example.jiexunxu.tinderapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.yelp.fusion.client.connection.YelpFusionApi;
import com.yelp.fusion.client.connection.YelpFusionApiFactory;
import com.yelp.fusion.client.models.Business;
import com.yelp.fusion.client.models.Category;
import com.yelp.fusion.client.models.SearchResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import retrofit2.Call;

/**
 * Created by Jiexun Xu on 3/28/2018.
 */

public class WaitSearchActivity extends AppCompatActivity {
    // Yelp fusion API Key
    private final String YelpFusionAPIKey="8JjOowcYfC9pTdTSAecKpdk6rCAV1lDY-N01QtuDw_K33ML6o-DUmXErJuZujMoYcMdLFp7VK41ajShMXjNhdzgjaZIkdR2qAAToD68zo6my62RssH0sa3d9BT2sWnYx";
    YelpFusionAPIQuery placeSearch;

    // Used for storing current places.
    ArrayList<YelpPlace> places;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_for_search);
        initUI();
        initButtons();
    }

    // Disable back button on the phone
    @Override
    public void onBackPressed() {
        cancelSearch();
    }

    @Override
    protected void onStart(){
        super.onStart();
        YelpFusionParams params=(YelpFusionParams)getIntent().getSerializableExtra("params");
        doYelpSearch(params);
    }

    private void doYelpSearch(YelpFusionParams params){
        try {
            placeSearch = new YelpFusionAPIQuery(YelpFusionAPIKey, getApplicationContext());
            placeSearch.execute(params);
        }catch(Exception ex){
            if(SettingsParams.debugMode)
                Log.d("Error", "==========FATAL ERROR: Unable to call yelp API=============");
            ErrorActivity.start(this, "Unable to call remote yelp API for search. Either there's no internet, the search quota has been full, or something else has gong wrong...");
        }
    }

    private void initUI(){
        setTitle("Searching...");
        ConstraintLayout layout=findViewById(R.id.waitSearchLayout);
        AppOptions opts=AppOptions.getUIOptions(SettingsParams.themeID);
        layout.setBackgroundColor(getApplicationContext().getResources().getColor(opts.backgroundColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(opts.primaryColor)));
        final Button startOverButton=findViewById(R.id.startOverButton);
        startOverButton.setBackgroundResource(opts.buttonStyle);
     //   startOverButton.setCompoundDrawables(getApplicationContext().getResources().getDrawable( opts.buttonStyle), null, null, null);
    }

    private void initButtons(){
        final Button startOverButton=findViewById(R.id.startOverButton);
        final pl.droidsonroids.gif.GifTextView gif=findViewById(R.id.waitSearchGifAnimation);
        startOverButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                cancelSearch();
            }
        });
    }

    private void cancelSearch(){
        placeSearch.cancelTask();
        Intent intent=new Intent(WaitSearchActivity.this, MainActivity.class);
        WaitSearchActivity.this.startActivity(intent);
    }
}

class YelpFusionAPIQuery extends AsyncTask<YelpFusionParams, Void, ArrayList<YelpPlace>> {
    private ArrayList<YelpPlace> places;
    private YelpFusionApi yelpAPI;

    private boolean taskCanceled;

    // Used to fire up the slideshowactivity on postExecute
    private Context context;

    YelpFusionAPIQuery(String APIKey, Context context){
        this.context=context;
        try {
            yelpAPI = new YelpFusionApiFactory().createAPI(APIKey);
        }catch(IOException ex){
            Log.d("Error", "Unable to create yelp fusion API");
        }
    }

    ArrayList<YelpPlace> getPlaces(){ return places; }

    void cancelTask(){
        taskCanceled=true;
    }

    @Override
    protected ArrayList<YelpPlace> doInBackground(YelpFusionParams... params) {
        YelpFusionAPI_BusinessSearch(params[0]);
        YelpPlace.sortKey=params[0].sortKey;
        return places;
    }

    @Override
    protected void onPostExecute(ArrayList<YelpPlace> result){
        super.onPostExecute(result);
        if(!taskCanceled) {
            if(result!=null && result.size()>0) {
                if(YelpPlace.sortKey>0)
                    Collections.sort(result);
                if(SettingsParams.debugMode)
                    for (int i = 0; i < places.size(); i++)
                        Log.d("place", places.get(i).toString());
                Intent intent = new Intent(context, SlideShowActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("places", result);
                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }else{
                Intent intent=new Intent(context, NoPlacesFoundActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
    }

    private void YelpFusionAPI_BusinessSearch(YelpFusionParams params){
        try {
            Call<SearchResponse> call;
            if(params.mustHaveFoodDelivery)
                call=yelpAPI.getTransactionSearch("delivery", params.getParams());
            else
                call=yelpAPI.getBusinessSearch(params.getParams());
            if(SettingsParams.debugMode)
                Log.d("Msg", "finish call");
            SearchResponse response = call.execute().body();
            ArrayList<Business> businesses=response.getBusinesses();
            places=new ArrayList<YelpPlace>(businesses.size());
            for(int i=0;i<businesses.size();i++){
                Business business=businesses.get(i);
                YelpPlace place=new YelpPlace();
                place.formattedAddress=business.getLocation().getAddress1()+business.getLocation().getAddress2()+business.getLocation().getAddress3()
                        +"\n"+business.getLocation().getCity()+" "+business.getLocation().getState();
                place.lat=business.getCoordinates().getLatitude();
                place.lng=business.getCoordinates().getLongitude();
                place.rating=business.getRating();
                place.reviewCount=business.getReviewCount();
                place.name=business.getName();
                ArrayList<Category> categories=business.getCategories();
                place.categories=new String[categories.size()][2];
                for(int j=0;j<categories.size();j++) {
                    place.categories[j][0] = categories.get(j).getAlias();
                    place.categories[j][1] = categories.get(j).getTitle();
                }
                place.imageURL=business.getImageUrl();
                place.URL=business.getUrl();
                place.setPrice(business.getPrice());
                if(params.mustHaveFoodDelivery) {
                    place.phone = business.getPhone();
                    place.displayPhone=place.phone;
                    place.setDistance(params.latitude, params.longitude, place.lat, place.lng);
                }else {
                    place.displayPhone = business.getDisplayPhone();
                    place.phone=business.getPhone();
                    if(!params.getParams().containsKey("location"))
                        place.distance=business.getDistance();
                    else
                        place.setDistance(params.latitude, params.longitude, place.lat, place.lng);
                }

                places.add(place);
            }
        }catch(Exception ex){
            if(SettingsParams.debugMode)
                Log.d("Error", "==========FATAL ERROR: Unable to call yelp API=============");
            ErrorActivity.start(context, "Unable to call remote yelp API for search. Either there's no internet, the search quota has been full, or something else has gong wrong...");
        }
    }
}

package com.example.jiexunxu.tinderapp;

import android.location.Location;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Constructs the google api query text and place filter based on user choices
 *
 * Created by Jiexun Xu on 3/14/2018.
 */

class YelpFusionParams implements Serializable{
    private HashMap<String, String> params;
    int sortKey;
    boolean mustHaveFoodDelivery;
    double latitude;
    double longitude;

    HashMap<String, String> getParams() { return params; }

    void setDefaultParams(){
        params=new HashMap<String, String>();
        params.put("latitude", "40.748838");
        params.put("longitude", "-73.985644");
        params.put("radius", "8000");
        params.put("sort_by", "best_match");
        params.put("limit", "25");
        params.put("price", "1,2,3,4");
        params.put("open_now", "false");
    }

    void setLatLng(double lat, double lng){
        latitude=lat;
        longitude=lng;
        if(!params.containsKey("location")) {
            params.put("latitude", Double.toString(lat));
            params.put("longitude", Double.toString(lng));
        }
    }
/*
    void setSortingMethod(int sortBy){
        switch(sortBy){
            case 1:params.put("sort_by", "best_match");break;
            case 2:params.put("sort_by", "distance");break;
            case 3:params.put("sort_by", "rating");break;
            case 4:params.put("sort_by", "review_count");break;
            default:params.put("sort_by", "best_match");break;
        }

    }
*/
    void setMaxResults(int maxResults){ params.put("limit", Integer.toString(maxResults)); }

    void setRadius(int radius){
        params.put("radius", Integer.toString(radius));
    }

    // Supply additional keywords, such as "seafood, asian food" etc to the search
    void setKeywordSearch(String keyword){
        if(keyword!=null && keyword.length()>0)
            params.put("term", keyword);
        else
            params.remove("term");
    }

    // If this method is called, the default search location in params will be overwritten
    void setLocationSearch(String address){
        if(address!=null && address.length()>0) {
            params.remove("latitude");
            params.remove("longitude");
            params.put("location", address);
        }else{
            params.remove("location");
            params.put("latitude", "40.748838");
            params.put("longitude", "-73.985644");
        }
    }

    // Input is a variable number of categories string. This method will concatenate them together
    void setCategories(String... categories){
        String cat="";
        for(int i=0;i<categories.length;i++)
            cat+=categories[i]+",";
        cat=cat.substring(0, cat.length()-1);
        params.put("categories", cat);
    }

    // Input are 4 booleans that corresponds to the four prices $, $$, $$$, $$$$. pi==true means the
    // i-th price will be considered
    void setPrice(boolean p1, boolean p2, boolean p3, boolean p4){
        String cat="";
        if(p1)
            cat+="1,";
        if(p2)
            cat+="2,";
        if(p3)
            cat+="3,";
        if(p4)
            cat+="4,";
        if(!p1 && !p2 && !p3 && !p4)
            cat="1,2,3,4,";
        cat=cat.substring(0, cat.length()-1);
        params.put("price", cat);
    }

    void setMustOpenNow(boolean mustOpenNow){
        if(mustOpenNow)
            params.put("open_now", "true");
        else
            params.put("open_now", "false");
    }
}

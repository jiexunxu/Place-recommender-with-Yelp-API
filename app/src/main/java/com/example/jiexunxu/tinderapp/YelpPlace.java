package com.example.jiexunxu.tinderapp;

import android.location.Location;

import java.io.Serializable;

/**
 * Stores all the place details parsed from the json file
 *
 * Created by Jiexun Xu on 3/13/2018.
 */

class YelpPlace implements Serializable, Comparable<YelpPlace> {
    /* An integer indicating which of the following fields will be used as a sorting field for YelpPlace objects
       sortKey=1 uses price (low to high)
       sortKey=2 uses price (high to low)
       sortKey=3 uses distance
       sortKey=4 uses rating (high to low)
       sortKey=5 uses reviewCount
     */
    static int sortKey;

    String formattedAddress;
    double lat;
    double lng;
    String imageURL;
    String URL;
    String phone;
    String displayPhone;
    String name;
    boolean openNow;
    String openingHours;
    String placeID;
    String price;
    double rating;
    int reviewCount;
    double distance; // in meters
    String[][] categories;
    String priceDescription;

    @Override
    public String toString(){
        return "{ Address="+formattedAddress+", \nlocation=["+lat+", "+
                lng+"], \nicon="+imageURL+", \nphone="+phone+", \nname="+name+
                ", \nopenNow="+openNow+", \nopeningHours"+openingHours+", \nplaceID="+
                placeID+", \nprice="+price+", \nrating="+rating+",\nreviewCount="+reviewCount+"\ndistance="+distance;
    }

    @Override
    public int compareTo(YelpPlace other){
        switch(sortKey){
            case 1:
                if(this.price.length()>other.price.length())
                    return 1;
                else if(this.price.length()<other.price.length())
                    return -1;
                return 0;
            case 2:
                if(this.price.length()<other.price.length())
                    return 1;
                else if(this.price.length()>other.price.length())
                    return -1;
                return 0;
            case 3:
                if(this.distance>other.distance)
                    return 1;
                else if(this.distance<other.distance)
                    return -1;
                return 0;
            case 4:
                if(this.rating<other.rating)
                    return 1;
                else if(this.rating>other.rating)
                    return -1;
                return 0;
            case 5:
                if(this.reviewCount<other.reviewCount)
                    return 1;
                else if(this.reviewCount>other.reviewCount)
                    return -1;
                return 0;
            default:
                return 0;
        }
    }

    void setPrice(String price) {
        this.price=price;
        if (price.length()==1)
            priceDescription=" (<$10)";
        else if (price.length()==2)
            priceDescription=" ($10-$30)";
        else if (price.length()==3)
            priceDescription=" ($30-$60)";
        else if (price.length()==4)
            priceDescription=" (>$60)";
    }

    void setDistance(double lat1, double lng1, double lat2, double lng2){
        Location loc1=new Location("");
        loc1.setLatitude(lat1);
        loc1.setLongitude(lng1);
        Location loc2=new Location("");
        loc2.setLatitude(lat1);
        loc2.setLongitude(lng2);
        double dist=loc1.distanceTo(loc2);
        if(dist<10000)
            distance=dist*1.5;
        else if(dist<810000)
            distance=dist*(1.5-(dist-10000)*0.5/800000);
        else
            distance=dist;
    }

    double distance2miles(){
        return Math.round(distance/1609.34*100.0)/100.0;
    }
}

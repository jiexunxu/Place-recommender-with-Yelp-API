package com.example.jiexunxu.tinderapp;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.eftimoff.viewpagertransformers.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageButton;

/**
 * Created by Jiexun Xu on 3/28/2018.
 */

public class SlideShowActivity extends AppCompatActivity {
    private ArrayList<YelpPlace> places;
    private SlideShowAdapter adapter;
    private int selectedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        Bundle args=getIntent().getBundleExtra("bundle");
        places=(ArrayList<YelpPlace>)args.getSerializable("places");
        initUI();
        initButtons();
    }

    @Override
    protected void onStart(){
        super.onStart();
        initSlideShow();
    }

    // Disable back button on the phone
    @Override
    public void onBackPressed() {
        backToMainMenu();
    }

    private void initUI(){
        setTitle("Results");
        ConstraintLayout layout=findViewById(R.id.slideshowLayout);
        AppOptions opts=AppOptions.getUIOptions(SettingsParams.themeID);
        layout.setBackgroundColor(getApplicationContext().getResources().getColor(opts.backgroundColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(opts.primaryColor)));
    }

    private void initButtons(){
        final GifImageButton callButton=findViewById(R.id.placePhoneCall);
        final GifImageButton directionsButton=findViewById(R.id.placeDirections);
        final GifImageButton websiteButton=findViewById(R.id.placeWebsite);
        final GifImageButton backButton=findViewById(R.id.placeGoBackToSearch);

        callButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:"+places.get(selectedPlace).phone));
                try {
                    if (ContextCompat.checkSelfPermission(SlideShowActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SlideShowActivity.this, new String[]{Manifest.permission.CALL_PHONE},1);
                    }else {
                        startActivity(intent);
                    }
                }catch(Exception ex){
                    ErrorActivity.start(getApplicationContext(), "Unable to make a phone call.");
                }
            }
        });

        directionsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    String googleMapStr = "http://maps.google.com/maps?daddr=" + places.get(selectedPlace).lat + "," + places.get(selectedPlace).lng;
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(googleMapStr));
                    startActivity(intent);
                }catch(Exception ex){
                    ErrorActivity.start(getApplicationContext(), "Unable to launch google map directions app.");
                }
            }
        });

        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(places.get(selectedPlace).URL));
                    startActivity(intent);
                }catch(Exception ex){
                    ErrorActivity.start(getApplicationContext(), "Unable to launch webpage.");
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                backToMainMenu();
            }
        });

        ViewPager pager=findViewById(R.id.viewpager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                YelpPlace place=places.get(position);
                selectedPlace=position;
                setTitle(place.name);
                String tags="";
                for(int i=0;i<place.categories.length;i++)
                    tags+=place.categories[i][1]+", ";
                tags=tags.substring(0, tags.length()-2);
                ((TextView)findViewById(R.id.placeTagsText)).setText("Tag(s): "+tags);
                if(place.phone==null || place.phone.length()<5)
                    ((TextView)findViewById(R.id.placePhoneNumberText)).setText("Phone: Unknown");
                else
                    ((TextView)findViewById(R.id.placePhoneNumberText)).setText("Phone: "+place.displayPhone);
                ((TextView)findViewById(R.id.placeDistanceText)).setText("Distance: "+place.distance2miles()+" miles");
                ((TextView)findViewById(R.id.placePriceText)).setText("Price: "+place.price+place.priceDescription);
                ((TextView)findViewById(R.id.placeReviewText)).setText("Review("+place.reviewCount+"): ");
                ((RatingBar)findViewById(R.id.placeRatingBar)).setRating((float)place.rating);
            }

            @Override
            public void onPageSelected(int position) { }

            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    private void initSlideShow(){
        String[] imageURLs=new String[places.size()];
        for(int i=0;i<places.size();i++)
            imageURLs[i]=places.get(i).imageURL;

        adapter=new SlideShowAdapter(this, imageURLs);
        ViewPager pager=findViewById(R.id.viewpager);
        pager.setAdapter(adapter);
        switch((new java.util.Random()).nextInt(9)){
            case 0:pager.setPageTransformer(true, new CubeOutTransformer());break;
            case 1:pager.setPageTransformer(true, new BackgroundToForegroundTransformer());break;
            case 2:pager.setPageTransformer(true, new DepthPageTransformer());break;
            case 3:pager.setPageTransformer(true, new DrawFromBackTransformer());break;
            case 4:pager.setPageTransformer(true, new FlipHorizontalTransformer());break;
            case 5:pager.setPageTransformer(true, new ForegroundToBackgroundTransformer());break;
            case 6:pager.setPageTransformer(true, new RotateDownTransformer());break;
            case 7:pager.setPageTransformer(true, new RotateUpTransformer());break;
            case 8:pager.setPageTransformer(true, new TabletTransformer());break;
            case 9:pager.setPageTransformer(true, new ZoomOutSlideTransformer());break;
            default:pager.setPageTransformer(true, new DefaultTransformer());break;
        }
    }

    private void backToMainMenu(){
        Intent intent=new Intent(SlideShowActivity.this, MainActivity.class);
        SlideShowActivity.this.startActivity(intent);
    }
}

class SlideShowAdapter extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;

    String[] imageURLs;

    public SlideShowAdapter(Context context, String[] imageURLs) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.imageURLs=imageURLs;
    }

    @Override
    public int getCount() {
        return imageURLs.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.slideshow_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        if(imageURLs[position].equals("")){
            Picasso.get().load(R.drawable.slideshow_no_image).fit().centerCrop().into(imageView);
        }else {
            try {
                Picasso.get().load(imageURLs[position]).placeholder(R.drawable.slideshow_loading).fit().centerInside().into(imageView);//
            } catch (Exception ex) {
                if(SettingsParams.debugMode)
                    Log.d("Error", "Unable to load image=" + imageURLs[position]);
                Picasso.get().load(R.drawable.slideshow_no_image).fit().centerCrop().into(imageView);
            }
        }
     //   Glide.with(mContext).load(imageURLs[position]).apply((new RequestOptions()).centerCrop()).into(imageView);
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
      //  container.removeView((LinearLayout) object);
        View view = (View)object;
        ((ViewPager) container).removeView(view);
        view = null;
    }
}

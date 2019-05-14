package com.example.jiexunxu.tinderapp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Jiexun Xu on 4/3/2018.
 */

public class NoPlacesFoundActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_places_found);
        initUI();
        initButtons();
    }

    private void initUI(){
        setTitle("No Results Found :(");
        ConstraintLayout layout=findViewById(R.id.noPlacesFoundLayout);
        final Button backToMainButton=findViewById(R.id.noResultsBackButton);
        AppOptions opts=AppOptions.getUIOptions(SettingsParams.themeID);
        layout.setBackgroundColor(getApplicationContext().getResources().getColor(opts.backgroundColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(opts.primaryColor)));
        backToMainButton.setBackgroundResource(opts.buttonStyle);
    }

    private void initButtons(){
        final Button backToMainButton=findViewById(R.id.noResultsBackButton);
        backToMainButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent(NoPlacesFoundActivity.this, MainActivity.class);
                NoPlacesFoundActivity.this.startActivity(intent);
            }
        });
    }
}

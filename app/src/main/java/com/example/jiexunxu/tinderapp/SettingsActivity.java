package com.example.jiexunxu.tinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;

/**
 * Created by Jiexun Xu on 3/30/2018.
 */

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_settings);
        initUI();
        initButtons();
    }

    @Override
    protected void onStart(){
        super.onStart();
        SettingsParams settings=new SettingsParams();
        settings.readSettingsFromFile(this.getApplicationContext());
        setSettingValuesToUI(settings);
    }

    @Override
    public void onBackPressed() {
        saveAndBack();
    }

    private void initUI(){
        setTitle("Custom Search");
        ConstraintLayout layout=findViewById(R.id.settingsLayout);
        final EditText keywordsEditText=findViewById(R.id.keywordSearchTextbox);
        final EditText addressEditText=findViewById(R.id.addressTextbox);
        final Spinner maxResultsSpinner=findViewById(R.id.maxResultsSpinner);
        final Spinner sortBySpinner=findViewById(R.id.sortBySpinner);
        final Spinner searchRangeSpinner=findViewById(R.id.searchRangeSpinner);
        final Spinner themeSpinner=findViewById(R.id.colorThemeSpinner);
        final Button saveButton=findViewById(R.id.saveAndBackButton);
        final Button resetDefaultsButton=findViewById(R.id.resetDefaultButton);
        final CheckBox price1CheckBox=findViewById(R.id.price1CheckBox);
        final CheckBox price2CheckBox=findViewById(R.id.price2CheckBox);
        final CheckBox price3CheckBox=findViewById(R.id.price3CheckBox);
        final CheckBox price4CheckBox=findViewById(R.id.price4CheckBox);
        final CheckBox openNowCheckBox=findViewById(R.id.openNowCheckBox);
        AppOptions opts=AppOptions.getUIOptions(SettingsParams.themeID);
        layout.setBackgroundColor(getApplicationContext().getResources().getColor(opts.backgroundColor));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getApplicationContext().getResources().getColor(opts.primaryColor)));
        keywordsEditText.setBackgroundResource(opts.editTextStyle);
        addressEditText.setBackgroundResource(opts.editTextStyle);
        maxResultsSpinner.setBackgroundResource(opts.spinnerStyle);
        maxResultsSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle);
        sortBySpinner.setBackgroundResource(opts.spinnerStyle);
        sortBySpinner.setPopupBackgroundResource(opts.spinnerPopupStyle);
        searchRangeSpinner.setBackgroundResource(opts.spinnerStyle);
        searchRangeSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle);
        themeSpinner.setBackgroundResource(opts.spinnerStyle);
        themeSpinner.setPopupBackgroundResource(opts.spinnerPopupStyle);
        saveButton.setBackgroundResource(opts.buttonStyle);
        resetDefaultsButton.setBackgroundResource(opts.buttonStyle);
        CompoundButtonCompat.setButtonTintList(price1CheckBox, ColorStateList.valueOf(getResources().getColor(opts.primaryColorDark)));
        CompoundButtonCompat.setButtonTintList(price2CheckBox, ColorStateList.valueOf(getResources().getColor(opts.primaryColorDark)));
        CompoundButtonCompat.setButtonTintList(price3CheckBox, ColorStateList.valueOf(getResources().getColor(opts.primaryColorDark)));
        CompoundButtonCompat.setButtonTintList(price4CheckBox, ColorStateList.valueOf(getResources().getColor(opts.primaryColorDark)));
        CompoundButtonCompat.setButtonTintList(openNowCheckBox, ColorStateList.valueOf(getResources().getColor(opts.primaryColorDark)));
    }

    private void initButtons(){
        final Spinner maxResultsSpinner=findViewById(R.id.maxResultsSpinner);
        final String[] maxResultsItems={"1", "5", "10", "20", "50"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, maxResultsItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        maxResultsSpinner.setAdapter(adapter);
        final Spinner sortBySpinner=findViewById(R.id.sortBySpinner);
        String[] sortByItems={"Best Match", "Price (low to high)", "Price (high to low)", "Distance", "Rating", "Review Count"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sortByItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortBySpinner.setAdapter(adapter);
        final Spinner searchRangeSpinner=findViewById(R.id.searchRangeSpinner);
        final String[] searchRangeItems={"1", "5", "10", "25"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchRangeItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchRangeSpinner.setAdapter(adapter);
        final Button saveButton=findViewById(R.id.saveAndBackButton);
        final Button resetDefaultsButton=findViewById(R.id.resetDefaultButton);
        final Spinner themeSpinner=findViewById(R.id.colorThemeSpinner);
        final String[] themeSpinnerItems={"Default", "Ever Green", "Deep Blue", "Crimson Red"};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, themeSpinnerItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        themeSpinner.setAdapter(adapter);

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                saveAndBack();
            }
        });

        resetDefaultsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                SettingsParams settings=new SettingsParams();
                settings.getDefaultSettings();
                setSettingValuesToUI(settings);
            }
        });
    }

    private void saveAndBack(){
        SettingsParams settings=UIToSettingsParams();
        settings.writeSettingsToFile(this.getApplicationContext());
        Intent intent=new Intent(SettingsActivity.this, MainActivity.class);
        SettingsActivity.this.startActivity(intent);
    }

    private SettingsParams UIToSettingsParams(){
        final EditText keywordsText=findViewById(R.id.keywordSearchTextbox);
        final EditText addressText=findViewById(R.id.addressTextbox);
        final Spinner maxResultsSpinner=findViewById(R.id.maxResultsSpinner);
        final Spinner sortBySpinner=findViewById(R.id.sortBySpinner);
        final Spinner searchRangeSpinner=findViewById(R.id.searchRangeSpinner);
        final CheckBox price1CheckBox=findViewById(R.id.price1CheckBox);
        final CheckBox price2CheckBox=findViewById(R.id.price2CheckBox);
        final CheckBox price3CheckBox=findViewById(R.id.price3CheckBox);
        final CheckBox price4CheckBox=findViewById(R.id.price4CheckBox);
        final CheckBox mustBeOpenCheckBox=findViewById(R.id.openNowCheckBox);
        final Spinner themeSpinner=findViewById(R.id.colorThemeSpinner);

        SettingsParams settings=new SettingsParams();
        settings.keywords=keywordsText.getText().toString();
        settings.address=addressText.getText().toString();
        switch(maxResultsSpinner.getSelectedItemPosition()){
            case 0:settings.maxResults=1;break;
            case 1:settings.maxResults=5;break;
            case 2:settings.maxResults=10;break;
            case 3:settings.maxResults=20;break;
            case 4:settings.maxResults=50;break;
            default:settings.maxResults=20;break;
        }
        switch(sortBySpinner.getSelectedItemPosition()){
            case 0:settings.sortingMethod=0;break;
            case 1:settings.sortingMethod=1;break;
            case 2:settings.sortingMethod=2;break;
            case 3:settings.sortingMethod=3;break;
            case 4:settings.sortingMethod=4;break;
            case 5:settings.sortingMethod=5;break;
            default:settings.sortingMethod=0;break;
        }
        switch(searchRangeSpinner.getSelectedItemPosition()){
            case 0:settings.searchRange=1600;break;
            case 1:settings.searchRange=8000;break;
            case 2:settings.searchRange=16000;break;
            case 3:settings.searchRange=40000;break;
            default:settings.searchRange=8000;break;
        }
        settings.prices[0]=price1CheckBox.isChecked();
        settings.prices[1]=price2CheckBox.isChecked();
        settings.prices[2]=price3CheckBox.isChecked();
        settings.prices[3]=price4CheckBox.isChecked();
        settings.mustBeOpenNow=mustBeOpenCheckBox.isChecked();
        switch(themeSpinner.getSelectedItemPosition()){
            case 0:SettingsParams.themeID=0;break;
            case 1:SettingsParams.themeID=1;break;
            case 2:SettingsParams.themeID=2;break;
            case 3:SettingsParams.themeID=3;break;
            default:SettingsParams.themeID=0;break;
        }
        return settings;
    }

    private void setSettingValuesToUI(SettingsParams settings){
        final EditText keywordsText=findViewById(R.id.keywordSearchTextbox);
        final EditText addressText=findViewById(R.id.addressTextbox);
        final Spinner maxResultsSpinner=findViewById(R.id.maxResultsSpinner);
        final Spinner sortBySpinner=findViewById(R.id.sortBySpinner);
        final Spinner searchRangeSpinner=findViewById(R.id.searchRangeSpinner);
        final CheckBox price1CheckBox=findViewById(R.id.price1CheckBox);
        final CheckBox price2CheckBox=findViewById(R.id.price2CheckBox);
        final CheckBox price3CheckBox=findViewById(R.id.price3CheckBox);
        final CheckBox price4CheckBox=findViewById(R.id.price4CheckBox);
        final CheckBox mustBeOpenCheckBox=findViewById(R.id.openNowCheckBox);
        final Spinner themeSpinner=findViewById(R.id.colorThemeSpinner);

        keywordsText.setText(settings.keywords);
        addressText.setText(settings.address);
        switch(settings.maxResults){
            case 1:maxResultsSpinner.setSelection(0);break;
            case 5:maxResultsSpinner.setSelection(1);break;
            case 10:maxResultsSpinner.setSelection(2);break;
            case 20:maxResultsSpinner.setSelection(3);break;
            case 50:maxResultsSpinner.setSelection(4);break;
            default:maxResultsSpinner.setSelection(3);break;
        }
        switch(settings.sortingMethod){
            case 0:sortBySpinner.setSelection(0);break;
            case 1:sortBySpinner.setSelection(1);break;
            case 2:sortBySpinner.setSelection(2);break;
            case 3:sortBySpinner.setSelection(3);break;
            case 4:sortBySpinner.setSelection(4);break;
            case 5:sortBySpinner.setSelection(5);break;
            default:sortBySpinner.setSelection(0);break;
        }
        switch(settings.searchRange){
            case 1600:searchRangeSpinner.setSelection(0);break;
            case 8000:searchRangeSpinner.setSelection(1);break;
            case 16000:searchRangeSpinner.setSelection(2);break;
            case 40000:searchRangeSpinner.setSelection(3);break;
            default:searchRangeSpinner.setSelection(1);break;
        }
        price1CheckBox.setChecked(settings.prices[0]);
        price2CheckBox.setChecked(settings.prices[1]);
        price3CheckBox.setChecked(settings.prices[2]);
        price4CheckBox.setChecked(settings.prices[3]);
        mustBeOpenCheckBox.setChecked(settings.mustBeOpenNow);
        switch(SettingsParams.themeID){
            case 0:themeSpinner.setSelection(0);break;
            case 1:themeSpinner.setSelection(1);break;
            case 2:themeSpinner.setSelection(2);break;
            case 3:themeSpinner.setSelection(3);break;
            default:themeSpinner.setSelection(0);break;
        }
    }
}
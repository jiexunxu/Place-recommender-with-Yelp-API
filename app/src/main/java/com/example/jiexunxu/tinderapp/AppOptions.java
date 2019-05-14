package com.example.jiexunxu.tinderapp;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jiexun Xu on 4/6/2018.
 */

class AppOptions{
    int primaryColor;
    int primaryColorDark;
    int backgroundColor;
    int buttonStyle;
    int editTextStyle;
    int spinnerStyle;
    int spinnerPopupStyle;

    AppOptions(){
        primaryColor=R.color.defaultPrimary;
        primaryColorDark=R.color.defaultPrimaryDark;
        backgroundColor=R.color.defaultBackground;
        buttonStyle=R.drawable.default_rounded_button;
        editTextStyle=R.drawable.default_edittext_style;
        spinnerStyle=R.drawable.default_rounded_spinner;
        spinnerPopupStyle=R.drawable.default_spinner_popup;
    }

    static AppOptions getUIOptions(int themeID){
        AppOptions ret=new AppOptions();
        switch(themeID){
            case 1:
                ret.primaryColor=R.color.greenPrimary;
                ret.primaryColorDark=R.color.greenPrimaryDark;
                ret.backgroundColor=R.color.greenBackground;
                ret.buttonStyle=R.drawable.green_rounded_button;
                ret.editTextStyle=R.drawable.green_edittext_style;
                ret.spinnerStyle=R.drawable.green_rounded_spinner;
                ret.spinnerPopupStyle=R.drawable.green_spinner_popup;
                break;
            case 2:
                ret.primaryColor=R.color.bluePrimary;
                ret.primaryColorDark=R.color.bluePrimaryDark;
                ret.backgroundColor=R.color.blueBackground;
                ret.buttonStyle=R.drawable.blue_rounded_button;
                ret.editTextStyle=R.drawable.blue_edittext_style;
                ret.spinnerStyle=R.drawable.blue_rounded_spinner;
                ret.spinnerPopupStyle=R.drawable.blue_spinner_popup;
                break;
            case 3:
                ret.primaryColor=R.color.redPrimary;
                ret.primaryColorDark=R.color.redPrimaryDark;
                ret.backgroundColor=R.color.redBackground;
                ret.buttonStyle=R.drawable.red_rounded_button;
                ret.editTextStyle=R.drawable.red_edittext_style;
                ret.spinnerStyle=R.drawable.red_rounded_spinner;
                ret.spinnerPopupStyle=R.drawable.red_spinner_popup;
                break;
            default:
                break;
        }
        return ret;
    }
}

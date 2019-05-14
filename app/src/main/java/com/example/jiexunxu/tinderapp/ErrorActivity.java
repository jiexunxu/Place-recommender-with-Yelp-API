package com.example.jiexunxu.tinderapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ErrorActivity extends AppCompatActivity {
    static void start(Context context, String msg){
        Intent intent=new Intent(context, ErrorActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("message", msg);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);
        initUI();
    }

    @Override
    public void onBackPressed() {
        goBackToMain();
    }

    private void initUI(){
        setTitle("Oops...");
        String msg=getIntent().getStringExtra("message");
        final TextView errorText=findViewById(R.id.errorText);
        errorText.setText(msg);
        final Button backToMainButton=findViewById(R.id.errorBackButton);
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBackToMain();
            }
        });
    }

    private void goBackToMain(){
        Intent intent=new Intent(ErrorActivity.this, MainActivity.class);
        ErrorActivity.this.startActivity(intent);
    }
}

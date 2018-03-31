package com.getfreedash;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FontAwsome extends AppCompatActivity {
    Typeface typeface ;
    TextView textViewContact, textViewBattery, textViewBank, textViewBirthday, textViewCab, textViewCamera ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_awsome);

        typeface = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");

        textViewContact = (TextView)findViewById(R.id.contact_us_icon);
        textViewBattery = (TextView)findViewById(R.id.fa_battery);
        textViewBank = (TextView)findViewById(R.id.fa_bank_icon);
        textViewBirthday = (TextView)findViewById(R.id.fa_birthday);
        textViewCab = (TextView)findViewById(R.id.fa_cab_icon);
        textViewCamera = (TextView)findViewById(R.id.fa_camera_icon);


        textViewContact.setTypeface(typeface);
        textViewBattery.setTypeface(typeface);
        textViewBank.setTypeface(typeface);
        textViewBirthday.setTypeface(typeface);
        textViewCab.setTypeface(typeface);
        textViewCamera.setTypeface(typeface);

        textViewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewContact.startAnimation(xmlAnimationSample);

            }
        });
        textViewBattery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewBattery.startAnimation(xmlAnimationSample);

            }
        });
        textViewBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewBank.startAnimation(xmlAnimationSample);

            }
        });
        textViewBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewBirthday.startAnimation(xmlAnimationSample);

            }
        });
        textViewCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewCab.startAnimation(xmlAnimationSample);

            }
        });
        textViewCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                textViewCamera.startAnimation(xmlAnimationSample);

            }
        });
    }
}

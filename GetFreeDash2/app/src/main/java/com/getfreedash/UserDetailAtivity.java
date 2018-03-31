package com.getfreedash;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailAtivity extends AppCompatActivity {
    private  TextView tv_name, tv_email, tv_imei;
    private CircleImageView user_picture;
    private TelephonyManager tm;
    private String str_IMEINumber,fname, lname, email,pic;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_ativity);
        initView();
        toolbar();

    }
    private void toolbar(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("USER DETAIL");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void initView() {
        tm=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        str_IMEINumber=tm.getDeviceId();
        user_picture = (CircleImageView) findViewById(R.id.profilePic);
        Intent intent = getIntent();

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_imei = (TextView) findViewById(R.id.tv_imei);

        if (intent.getStringExtra("from").equals("signUp"))
        {
            fname = intent.getStringExtra("fname");
            lname = intent.getStringExtra("lname");
            email = intent.getStringExtra("email");
        tv_name.setText(fname +" "+lname);
        tv_email.setText(email);
        tv_imei.setText("IMEI NUMBER : " +str_IMEINumber);
        }

        if (intent.getStringExtra("from").equals("facebook"))
        {
            pic = intent.getStringExtra("pic");
            fname = intent.getStringExtra("name");

            email = intent.getStringExtra("email");
            user_picture.setVisibility(View.VISIBLE);
            Picasso.with(UserDetailAtivity.this).load(pic)
                    .into(user_picture);
            tv_name.setText(fname);
            tv_email.setText(email);
            tv_imei.setText("IMEI NUMBER : " +str_IMEINumber);
        }
    }

}

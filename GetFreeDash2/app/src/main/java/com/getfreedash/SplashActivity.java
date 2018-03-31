package com.getfreedash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.getfreedash.Authentication.LoginActivity;
import com.getfreedash.Authentication.RegisterMobileActivity;

public class SplashActivity extends AppCompatActivity {
    private SharedPreferences prefrence;
    private SharedPreferences.Editor editor;


    String token;
    public  static String accessToken;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Intent intent = getIntent();
        if (intent.getData() != null) {

            // Uri data = getIntent().getData();
            // OR USE THIS
           // accessToken  = getIntent().getDataString();
            String data = getIntent().getDataString();
            accessToken= data.substring(data.lastIndexOf("code=") + 5);
           /* token =data.substring(index);
            token.replace("code="," ");*/

           // accessToken=token;
           // accessToken = accessToken.replace("https://getfreedash.com/verifye.php?code=", "");
           // Toast.makeText(this, "" + accessToken, Toast.LENGTH_SHORT).show();
            Intent in = new Intent(SplashActivity.this, LoginActivity.class);

            intent.putExtra("accessToken", accessToken);
            startActivity(in);
            finish();
        } else {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }, 4000);
        }

        Log.e(">>>", String.valueOf(intent.getData()));



  /*  private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.bounce);
        a.reset();
       // TextView tv = (TextView) findViewById(R.id.tv_dash);
        tv.clearAnimation();
        tv.startAnimation(a);
    }*/
   /* private void StartAnimations() {

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        TextView tv_get = (TextView) findViewById(R.id.tv_get);
        LinearLayout layout= (LinearLayout) findViewById(R.id.getfree);
        layout.clearAnimation();
        layout.startAnimation(anim);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, 4000);
        *//*Thread myThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(),ListActivity.class);
                    startActivity(intent);
                    finish();
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();*//*
    }*/

    }
}

package com.getfreedash.Authentication;

import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.getfreedash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SocialLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_fnmae,et_lname,et_email;
    private  TextView tv_login;
    private Toolbar toolbar;
    private RequestQueue queue;
    private String android_Deviceid,android_MacAddress,android_IMEINumber;
    private TelephonyManager tm;
    String response2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_login);
        toolbar();
        initView();
    }

    private void initView()
    {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        android_IMEINumber = tm.getDeviceId();
        android_Deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_MacAddress = wInfo.getMacAddress();
        Log.e("access", AccessToken.getCurrentAccessToken().getToken());
        queue= Volley.newRequestQueue(this);
        et_fnmae= (EditText) findViewById(R.id.et_fnmae);
        et_lname= (EditText) findViewById(R.id.et_lname);
        et_email= (EditText) findViewById(R.id.et_email);
        tv_login= (TextView) findViewById(R.id.tv_login);

        tv_login.setOnClickListener(this);

    }
    private void toolbar(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("SOCIAL LOGIN");
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
    @Override
    public void onClick(View view)
    {
        String str_lname,str_fname,str_email;
        str_fname=et_fnmae.getText().toString().trim();
        str_lname=et_lname.getText().toString().trim();

        str_email=et_email.getText().toString();

        if (str_fname.isEmpty()){
            et_fnmae.setError("Please enter first name");
        }else if (str_lname.isEmpty()){
            et_fnmae.setError("Please enter last name");
        }else if (str_email.isEmpty()) {
            et_email.setError("Email is empty");

        }else if (!Patterns.EMAIL_ADDRESS.matcher(str_email).matches()) {
            et_email.setError("Email is not valid");

        }else {
            login();
        }



    }

    private void login()
    {
        String url="http://api.getfreedash.com/socialLogin";
        StringRequest request=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONObject object=new JSONObject(response);

                    response2=object.getString("status");
                    //alrtDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", et_fnmae.getText().toString());
                params.put("lname", et_lname.getText().toString());
                params.put("email", et_email.getText().toString());
                params.put("accessToken", AccessToken.getCurrentAccessToken().getToken());

                params.put("android_id", android_Deviceid);
                params.put("imei", android_IMEINumber);
                params.put("mac", android_MacAddress);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);


    }

    private void alrtDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(response2);
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                    }
                });

        /*alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });*/

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}

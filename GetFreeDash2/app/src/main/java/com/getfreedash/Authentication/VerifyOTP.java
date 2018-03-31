package com.getfreedash.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.getfreedash.DashBoardActivity;
import com.getfreedash.R;
import com.sinch.verification.Verification;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyOTP extends AppCompatActivity implements View.OnClickListener {
    private EditText et_otp;
    private TextView tv_submit;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    private SharedPreferences prefrence;
    private SharedPreferences.Editor editor;
    private String str_email,str_password;
    private String android_Deviceid, android_MacAddress, android_IMEINumber;
    private TelephonyManager tm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        initView();
        toolbar();
    }

    private void toolbar() {
        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);


        editor = prefrence.edit();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        android_IMEINumber = tm.getDeviceId();
        android_Deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_MacAddress = wInfo.getMacAddress();


        str_password = prefrence.getString("password", null);
        str_email = prefrence.getString("email", null);//email
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("VERIFY OTP");
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
        requestQueue = Volley.newRequestQueue(this);
        et_otp = (EditText) findViewById(R.id.et_otp);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        login_dash();
        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        String otp = et_otp.getText().toString();
        if (otp.isEmpty()) {
            et_otp.setError("Please Enter OTP");
        } else {
            verify();
        }
    }
    public void verify()
    {
        final ProgressDialog mDialog = new ProgressDialog(VerifyOTP.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        String Url = "http://api.getfreedash.com/verifyOtp";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            if (status.contains("Otp Verified")) {
                                //   {"status":"Otp Verified","userid":92004}

                                String userid = mJsonObject.getString("userid");
                                startActivity(new Intent(VerifyOTP.this,DashBoardActivity.class));
                                finish();
                                //Toast.makeText(VerifyOTP.this, "User_id= " + userid, Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(VerifyOTP.this,status, Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(VerifyOTP.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(VerifyOTP.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(VerifyOTP.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(VerifyOTP.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(VerifyOTP.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(VerifyOTP.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",str_email );//str_email
                params.put("otp", et_otp.getText().toString());


                return params;
            }

           /* //@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                return headers;
            }*/
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void login_dash() {

        String Url = "http://api.getfreedash.com/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            if (status.equals("Successfully login")) {
                                SharedPreferences.Editor editor = prefrence.edit();
                                String id = mJsonObject.getString("id");
                                String fname = mJsonObject.getString("fname");
                                String lname = mJsonObject.getString("lname");
                                String email = mJsonObject.getString("email");
                                editor.putString("login_fname", fname);
                                editor.putString("login_lname", lname);
                                editor.putString("login_email", email);
                                editor.putString("id", id);
                                editor.commit();
                                startActivity(new Intent(VerifyOTP.this, DashBoardActivity.class));
                                finish();
                            } else if (status.equals("Mobile not registered")) {
                                Toast.makeText(VerifyOTP.this,status, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(VerifyOTP.this, RegisterMobileActivity.class));

                            } else {
                                Toast.makeText(VerifyOTP.this, status, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {
                            Toast.makeText(VerifyOTP.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(VerifyOTP.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(VerifyOTP.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(VerifyOTP.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(VerifyOTP.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(VerifyOTP.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", str_email);

                params.put("password",str_password);

                params.put("android_id", android_Deviceid);
                params.put("imei", android_IMEINumber);
                params.put("mac", android_MacAddress);
                return params;
            }

           /* //@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();

                return headers;
            }*/
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

}

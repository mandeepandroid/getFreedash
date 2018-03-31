package com.getfreedash;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TelephonyManager tm;
    private JSONObject response2, profile_pic_data, profile_pic_url;
    private ImageView user_picture;
    private TextView user_name;
    private TextView user_email, tv_imei;

    private Toolbar toolbar;
    private EditText et_fname, et_lname, et_email, et_password, et_repassword;
    private TextView tv_submit;
    private RequestQueue requestQueue;
    private LinearLayout ll_facebook;

    private SharedPreferences prefrence;
    private String android_Deviceid,android_MacAddress,android_IMEINumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        android_IMEINumber = tm.getDeviceId();
        android_Deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_MacAddress = wInfo.getMacAddress();
        Log.e(">>>", android_Deviceid);
        Log.e(">>>", android_IMEINumber);
        Log.e(">>>", android_MacAddress);
        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(this);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_fname = (EditText) findViewById(R.id.et_fnmae);
                et_lname = (EditText) findViewById(R.id.et_lname);
                et_email = (EditText) findViewById(R.id.et_email);
                et_password = (EditText) findViewById(R.id.et_password);
                et_repassword = (EditText) findViewById(R.id.et_repassword);

                String email = et_email.getText().toString();
                String lname = et_lname.getText().toString();
                String password = et_password.getText().toString();
                String repassword = et_repassword.getText().toString();
                et_fname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_fname.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_lname.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_lname.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_email.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_email.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_password.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_repassword.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_repassword.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

                if (et_fname.getText().toString().isEmpty()) {
                    et_fname.setError("Please enter first name");

                } else if (lname.isEmpty()) {
                    et_lname.setError("Please enter last name");

                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    et_email.setError("Email is not valid");

                } else if (password.isEmpty()) {
                    et_password.setError("Please enter some password");

                } else if (repassword.isEmpty()) {
                    et_repassword.setError("Please  Re-enter password");

                } else if (!password.equals(repassword)) {
                    et_repassword.setError("Password not match");

                } else {
                    SharedPreferences.Editor editor = prefrence.edit();

                    editor.putString("fname", et_fname.getText().toString());
                    editor.putString("lname", et_lname.getText().toString());
                    editor.putString("email", et_email.getText().toString());
                    editor.putString("password", et_password.getText().toString());
                    editor.commit();
                    signup();

                }

            }
        });


        toolbar();

        getKeyHash();
    }

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("SIGN UP");
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


    private void getKeyHash() {
//87B6cA2GGPnMu0+g7LkixHIWVDo=
        // 87B6cA2GGPnMu0+g7LkixHIWVDo=
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
    }

    public void signup() {
        final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        String Url = "http://api.getfreedash.com/register";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            if (status.equals("Verify Your Email")) {

                                alrtDialog();

                                Toast.makeText(MainActivity.this, "Registered succesfully..", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(MainActivity.this, status, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(MainActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(MainActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(MainActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(MainActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(MainActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", et_fname.getText().toString());
                params.put("lname", et_lname.getText().toString());
                params.put("eid", et_email.getText().toString());
                params.put("pass1", et_password.getText().toString());
                params.put("pass2", et_repassword.getText().toString());
                params.put("android_id",android_Deviceid);
                params.put("imei",android_IMEINumber);
                params.put("mac",android_MacAddress);
                return params;
            }

           @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
             /*  headers.put("Accept","application/json");
               headers.put("Content-Type","application/json");*/
                return headers;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    private void alrtDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("KINDLY CHECK YOUR EMAIL AND CLICK ON VERIFICATION LINK. ");
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

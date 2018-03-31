package com.getfreedash.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.balysv.materialripple.MaterialRippleLayout;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.getfreedash.DashBoardActivity;
import com.getfreedash.MainActivity;
import com.getfreedash.R;
import com.getfreedash.SplashActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText et_name, et_password;
    private TextView tv_login, tv_forgetPass;
    private RequestQueue requestQueue;
    private Toolbar toolbar;
    TextView tv_createAccount;

    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private JSONObject response2;
    private String response1;
    private String android_Deviceid, android_MacAddress, android_IMEINumber;
    private TelephonyManager tm;
    String firstName, lastName, email;
    private SharedPreferences prefrence;
    private SharedPreferences.Editor editor;
    Intent intent;
    private  String acccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);

       // Intent intent2 = getIntent();


        try {

                acccessToken = SplashActivity.accessToken;

        }catch (Exception e){

        }

        initView();
        toolbar();
        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);
        if (!prefrence.getString("id", "").equals("")) {
            Intent intent = new Intent(this, DashBoardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);
        editor = prefrence.edit();
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        android_IMEINumber = tm.getDeviceId();
        android_Deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_MacAddress = wInfo.getMacAddress();

        Log.e(">>>"+"IMEINumber"+"",android_IMEINumber);
        Log.e(">>>"+"Deviceid"+"",android_Deviceid);
        Log.e(">>>"+"MacAddress"+"",android_MacAddress);

        requestQueue = Volley.newRequestQueue(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_createAccount = (TextView) findViewById(R.id.tv_createAccount);
        tv_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));

            }
        });
        tv_forgetPass = (TextView) findViewById(R.id.tv_forgetPass);
        tv_forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));

            }
        });
        tv_login.setOnClickListener(this);
    }
         /*callbackManager = CallbackManager.Factory.create();
       loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {


            getUserDetails(loginResult);


        }

        @Override
        public void onCancel() {
            // App code
        }

        @Override
        public void onError(FacebookException exception) {
            // App code
        }
    });
}

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject json_object,GraphResponse response) {
                        String jsondata = json_object.toString();

                      *//* Intent intent=new Intent(MainActivity.this,UserDetailAtivity.class);
                        intent.putExtra("from","facebook");*//*
                        try {
                            response2 = new JSONObject(jsondata);

                            String name = response2.get("name").toString();

                            firstName = name.substring(0, name.indexOf(" "));
                            lastName = name.substring(name.lastIndexOf(' '));
                            email = response2.get("email").toString();

                            Log.e(">>>", firstName);
                            Log.e(">>>", lastName);


                            editor.putString("fb_email",email);
                            editor.commit();
                            Log.e(">>>", response2.get("email").toString());
                            social_login();
                            ;
                           *//* profile_pic_data = new JSONObject(response2.get("picture").toString());
                            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
*//*
                            *//*String email=response2.get("email").toString();
                            String name=response2.get("name").toString();
                            profile_pic_data = new JSONObject(response2.get("picture").toString());
                            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));
                            String pic=profile_pic_url.getString("url");
                            intent.putExtra("email",email);
                            intent.putExtra("name",name);
                            intent.putExtra("pic",pic);

                            startActivity(intent);*//*
                           *//* Picasso.with(MainActivity.this).load(profile_pic_url.getString("url"))
                                    .into(user_picture);*//*

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120)");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }*/

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("LOGIN");
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
        try {
            vrify_email();
        }catch (Exception e){

        }

    }

    @Override
    public void onClick(View view) {

        login();
    }

    private void login() {


        Animation xmlAnimationSample;
        xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bounch);
        tv_login.startAnimation(xmlAnimationSample);
        String str_name, str_pass;
        str_name = et_name.getText().toString();
        str_pass = et_password.getText().toString();
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                et_name.setError(null);
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
        if (str_name.isEmpty()) {
            et_name.setError("Please enter user name");

        } else if (str_pass.isEmpty()) {
            et_password.setError("Please enter password");

        } else {
            login_dash();
        }


    }

    private void login_dash() {
        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        String Url = "http://api.getfreedash.com/login";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (!LoginActivity.this.isFinishing() && mDialog != null) {
                            mDialog.dismiss();
                        }
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
                                startActivity(new Intent(LoginActivity.this, DashBoardActivity.class));
                                finish();
                            } else if (status.equals("Mobile not registered")) {
                                Toast.makeText(LoginActivity.this,status, Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(LoginActivity.this, RegisterMobileActivity.class));

                            } else {
                                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();

                            }


                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(LoginActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(LoginActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(LoginActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(LoginActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(LoginActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", et_name.getText().toString());

                params.put("password", et_password.getText().toString());

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

    private void social_login()
    {
        String url = "http://api.getfreedash.com/socialLogin";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    response1 = object.getString("status");
                    // alrtDialog();
                    startActivity(new Intent(LoginActivity.this, RegisterMobileActivity.class));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("fname", firstName);
                params.put("lname", lastName);
                params.put("email", email);
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
        requestQueue.add(request);


    }

    private void vrify_email() {
        String url = " http://api.getfreedash.com/verifyEmail";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    response1 = object.getString("status");
                    // alrtDialog();
                    if (response1.contains("Invalid Code")){
                        alrtDialog();
                    }else
                        {
                        startActivity(new Intent(LoginActivity.this,RegisterMobileActivity.class));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }

        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("code", acccessToken);

                return params;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);


    }

    private void alrtDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage(response1);
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

package com.getfreedash.Authentication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.getfreedash.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterMobileActivity extends AppCompatActivity {
    EditText et_recode, et_wlletid, et_country, et_mobile;
    TextView tv_submit;
    private String str_email;
    Toolbar toolbar;
    private RequestQueue requestQueue;
    private SharedPreferences prefrence;
    SharedPreferences.Editor editor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_mobile);
        initView();
        toolbar();
       /* Intent intent = getIntent();
        str_email = intent.getStringExtra("email");*/
    }

    private void toolbar() {

        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);
        editor = prefrence.edit();

        str_email =prefrence.getString("email", null);
        requestQueue = Volley.newRequestQueue(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("REGISTER MOBILE");
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

    private void initView()
    {
        et_recode = (EditText) findViewById(R.id.et_recode);
        et_wlletid = (EditText) findViewById(R.id.et_wlletid);
        et_country = (EditText) findViewById(R.id.et_country);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        tv_submit = (TextView) findViewById(R.id.tv_submit);

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_recode, str_wallet, str_country, str_mobile;

                str_recode = et_recode.getText().toString();
                str_wallet = et_wlletid.getText().toString();
                str_country = et_country.getText().toString();
                str_mobile = et_mobile.getText().toString();
                et_recode.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_recode.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_wlletid.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_wlletid.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_country.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_country.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                et_mobile.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        et_mobile.setError(null);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });

               /* if (str_recode.isEmpty()) {
                  //  et_recode.setError("Plesae enter first name");

                } */
                if (str_wallet.isEmpty()) {
                    et_wlletid.setError("Plesae enter wallet id");

                } else if (str_country.isEmpty()) {
                    et_country.setError("Please enter country code");

                } else if (str_mobile.isEmpty()) {
                    et_mobile.setError("Please  mobile number");

                } else {
                    hitApi();

                }
            }
        });
    }

    public void hitApi() {
        final ProgressDialog mDialog = new ProgressDialog(RegisterMobileActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        String url = " http://api.getfreedash.com/registerMobile";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);

                            String status = mJsonObject.getString("status");
                            if (status.contains("Otp Sent")) {

                                startActivity(new Intent(RegisterMobileActivity.this, VerifyOTP.class));
                                finish();
                            }else {
                                Toast.makeText(RegisterMobileActivity.this,status,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(RegisterMobileActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(RegisterMobileActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(RegisterMobileActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(RegisterMobileActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(RegisterMobileActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(RegisterMobileActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email",str_email);//str_email
                params.put("refcode", et_recode.getText().toString());
                params.put("walletid", et_wlletid.getText().toString());
                params.put("country_code", et_country.getText().toString());
                params.put("mobile", et_mobile.getText().toString());
                editor.putString("phone",et_mobile.getText().toString());
                editor.commit();
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
//7811
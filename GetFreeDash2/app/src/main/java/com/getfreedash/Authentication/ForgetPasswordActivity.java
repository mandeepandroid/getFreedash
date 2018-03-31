package com.getfreedash.Authentication;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
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

import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv_submit;
    private EditText et_email;
    private Toolbar toolbar;
    private RequestQueue requestQueue;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initView();
        toolbar();
    }

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("FORGET PASSWORD");
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
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_email = (EditText) findViewById(R.id.et_email);

        tv_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String email = et_email.getText().toString();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError("Email is not valid");

        } else if (email.isEmpty()){
            et_email.setError("Email is empty");
        }  else{
            forgetpassword();
        }

    }

    public void forgetpassword() {
        final ProgressDialog mDialog = new ProgressDialog(ForgetPasswordActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        //http://api.getfreedash.com/forgetPassword
        String Url = "http://api.getfreedash.com/forgetPassword";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            Toast.makeText(ForgetPasswordActivity.this, status, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e)
                        {
                            mDialog.dismiss();
                            Toast.makeText(ForgetPasswordActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(ForgetPasswordActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(ForgetPasswordActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(ForgetPasswordActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(ForgetPasswordActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(ForgetPasswordActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", et_email.getText().toString());

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

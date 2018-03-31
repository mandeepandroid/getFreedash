package com.getfreedash;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
import com.getfreedash.Adapter.GetReferredUserAdapter;
import com.getfreedash.Authentication.LoginActivity;
import com.getfreedash.POJO.POJOGetReferredUser;
import com.getfreedash.Utils.FontManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity {
    private SharedPreferences prefrence;
    private String str_fname, str_lname, str_email, str_mobile, str_username, str_id, str_image, str_ref_code, str_ref_link, str_country_code;
    private TextView tv_email, tv_name, tv_phone, tv_reference, tv_link, tv_withdraw;
    private TextView tv_totalReferred, tv_balance, tv_paidBalance;
    private String str_total_paid_balance, str_balance, str_total_reffered;
    private Toolbar toolbar;
    private RequestQueue requestQueue;
    private ImageView profile_pic, iv_pic;
    SharedPreferences.Editor editor;
    private RecyclerView rv_view;
    private ArrayList<POJOGetReferredUser> arrayList = new ArrayList<>();
    private TextView tv_withdraw_status;
    private Uri imageUri;
    private int GET_IMAGE_REQUEST_CODE = 2563, REQUEST_CAMERA = 0, SELECT_FILE = 2563;
    private String android_Deviceid, android_MacAddress, android_IMEINumber;
    private TelephonyManager tm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        initView();
        toolbar();

    }

    private void initView() {
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        android_IMEINumber = tm.getDeviceId();
        android_Deviceid = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        android_MacAddress = wInfo.getMacAddress();
        profile_pic = (ImageView) findViewById(R.id.profile_pic);
        rv_view = (RecyclerView) findViewById(R.id.rv_view);
        requestQueue = Volley.newRequestQueue(this);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_link = (TextView) findViewById(R.id.tv_link);
        tv_link.setTextIsSelectable(true);

        tv_reference = (TextView) findViewById(R.id.tv_reference);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_totalReferred = (TextView) findViewById(R.id.tv_totalReferred);
        tv_balance = (TextView) findViewById(R.id.tv_balance);
        tv_withdraw = (TextView) findViewById(R.id.tv_withdraw);
        tv_paidBalance = (TextView) findViewById(R.id.tv_paidBalance);
        prefrence = getSharedPreferences("GetFreeDash", Context.MODE_PRIVATE);

       /* profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cameraIntent();
            }
        });*/
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation xmlAnimationSample;
                xmlAnimationSample = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounch);
                tv_withdraw.startAnimation(xmlAnimationSample);

                withdraw();
            }
        });

        editor = prefrence.edit();


        str_id = prefrence.getString("id", null);
        getDasboard();


    }

    private void toolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("DASHBOARD");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_drawer, menu);

        // Associate searchable configuration with the SearchView
        // SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.share) {
            share_dialog();
        } else if (id == R.id.logout) {

            editor.clear();
            editor.commit();

            //  startActivity(new Intent(DashBoardActivity.this,LoginActivity.class));
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void share_dialog() {
        // custom dialog
        final Dialog dialog = new Dialog(DashBoardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b3000000")));
        dialog.setCancelable(true);


        dialog.setContentView(R.layout.custom_share_dialog);


        TextView btn_cancle = (TextView) dialog.findViewById(R.id.btn_cancle);
        ImageView  iv_fb,iv_twitter, iv_gmail, iv_whatapp;


        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        iv_fb = (ImageView) dialog.findViewById(R.id.iv_fb);


        iv_twitter = (ImageView) dialog.findViewById(R.id.iv_twitter);
        iv_gmail = (ImageView) dialog.findViewById(R.id.iv_gmail);
        iv_whatapp = (ImageView) dialog.findViewById(R.id.iv_whatapp);

        iv_fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook();
            }
        });
        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareOnTwitter();
            }
        });
        iv_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlePlus();
            }
        });
        iv_whatapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharewhatApp();
            }
        });


        dialog.show();
    }

    public void withdraw_dialog() {
        // custom dialog
        final Dialog dialog = new Dialog(DashBoardActivity.this, android.R.style.Theme_Black_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#b3000000")));
        dialog.setCancelable(true);


        dialog.setContentView(R.layout.custome_withdraw_dialog);


        tv_withdraw_status = (TextView) dialog.findViewById(R.id.tv_withdraw_status);


        TextView btn_cancle = (TextView) dialog.findViewById(R.id.btn_cancle);
        ImageView iv_facebook, iv_twitter, iv_gmail, iv_whatapp;

        iv_facebook = (ImageView) dialog.findViewById(R.id.iv_facebook);
        iv_twitter = (ImageView) dialog.findViewById(R.id.iv_twitter);
        iv_gmail = (ImageView) dialog.findViewById(R.id.iv_gmail);
        iv_whatapp = (ImageView) dialog.findViewById(R.id.iv_whatapp);

        iv_facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebook();
            }
        });
        iv_twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shareOnTwitter();
            }
        });
        iv_gmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlePlus();
            }
        });
        iv_whatapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharewhatApp();
            }
        });

        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void getDasboard() {
        String url = "http://api.getfreedash.com/getDashboardData";


        final ProgressDialog mDialog = new ProgressDialog(DashBoardActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            if (status.contains("Success")) {
                                str_fname = mJsonObject.getString("fname");
                                str_lname = mJsonObject.getString("lname");
                                str_email = mJsonObject.getString("email");
                                str_image = mJsonObject.getString("image");
                                str_ref_code = mJsonObject.getString("ref_code");
                                str_ref_link = mJsonObject.getString("ref_link");
                                str_country_code = mJsonObject.getString("country_code");
                                str_mobile = mJsonObject.getString("mobile");

                                tv_email.setText(str_email);
                                tv_link.setText(str_ref_link);


                                str_username = str_fname + " " + str_lname;
                                tv_name.setText(str_username);
                                tv_phone.setText(str_mobile);
                                Log.e(">>>", str_image);
                                tv_reference.setText("All Referred by YOU " + str_mobile);

                                getBalanceinfo();

                                str_image.replace("\\", "");
                                Log.e(">>", str_image);
                                //   str_image.replace("\\/","\"");
                                // iv_image= (ImageView) findViewById(R.id.profile_pic);
                                // iv_pic.setImageResource(R.drawable.app_icon);
                                Picasso.with(DashBoardActivity.this).load(mJsonObject.getString("image")).into(profile_pic);
                                //   Picasso.with(DashBoardActivity.this).load(str_image).into(iv_image);
                                // https://getfreedash.s3.amazonaws.com/selfie2/824987965d0b11ecaf0b6fb6104abf9f.png

                            }

                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(DashBoardActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(DashBoardActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(DashBoardActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(DashBoardActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_id);
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

    private void getBalanceinfo() {
        String url = "http://api.getfreedash.com/getBalanceInfo";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            if (status.contains("Success")) {
                                str_total_paid_balance = mJsonObject.getString("total_paid_balance");
                                str_balance = mJsonObject.getString("balance");
                                str_total_reffered = mJsonObject.getString("total_reffered");

                                tv_paidBalance.setText("Total Paid Balance : $" + str_total_paid_balance);
                                tv_balance.setText("Balance $" + str_total_paid_balance);
                                tv_totalReferred.setText("Total Referred :" + str_total_reffered);

                                JSONArray jsonArray = mJsonObject.getJSONArray("ref_users");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject innerOj = jsonArray.getJSONObject(i);

                                    POJOGetReferredUser pojo = new POJOGetReferredUser();
                                    pojo.setName(innerOj.getString("name"));
                                    pojo.setDate(innerOj.getString("date"));
                                    pojo.setTime(innerOj.getString("time"));
                                    pojo.setBonus_given(innerOj.getString("bonus_given"));
                                    arrayList.add(pojo);


                                }
                                GetReferredUserAdapter adapter = new GetReferredUserAdapter(DashBoardActivity.this, arrayList);
                                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(DashBoardActivity.this, 1);
                                rv_view.setLayoutManager(mLayoutManager);
                                rv_view.setItemAnimator(new DefaultItemAnimator());
                                rv_view.setAdapter(adapter);


                            }

                        } catch (JSONException e) {


                            Toast.makeText(DashBoardActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(DashBoardActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(DashBoardActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(DashBoardActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_id);
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

    private void withdraw() {
        final ProgressDialog mDialog = new ProgressDialog(DashBoardActivity.this);
        mDialog.setMessage("Loading..");
        mDialog.show();
        String url = " http://api.getfreedash.com/withdrawRequest";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mDialog.dismiss();
                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");
                            withdraw_dialog();
                            tv_withdraw_status.setText(status);
                          /*  if (status.contains("Userid doesn't exists"))
                            {

                            }else if (status.contains("Image not exists")) {

                            }
                            else if (status.contains("Wrong Wallet ID")) {

                            }else if (status.contains("You have already requested withdrawal request of [‘amount’] on [‘date’]")) {

                            }else if (status.contains("No amount need to be paid"))
                            {
                                tv_withdraw_status.setText("No amount need to be paid");

                            }else if (status.contains("Minimum amount for withdrawal is 1")) {
                                tv_withdraw_status.setText("Minimum amount for withdrawal is 1");
                            }
*/


                        } catch (JSONException e) {

                            mDialog.dismiss();
                            Toast.makeText(DashBoardActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(DashBoardActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(DashBoardActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(DashBoardActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", str_id);
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

    private void update_selfie() {
        String url = "http://api.getfreedash.com/updateSelfie";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject mJsonObject = new JSONObject(response);
                            String status = mJsonObject.getString("status");

                            Toast.makeText(DashBoardActivity.this, "Status" + status, Toast.LENGTH_SHORT).show();


                        } catch (JSONException e) {


                            Toast.makeText(DashBoardActivity.this, "Please check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkTimeout", Toast.LENGTH_SHORT).show();

                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(DashBoardActivity.this, "AuthFailureError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast.makeText(DashBoardActivity.this, "ServerError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(DashBoardActivity.this, "NetworkError", Toast.LENGTH_SHORT).show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            Toast.makeText(DashBoardActivity.this, "ParseError", Toast.LENGTH_SHORT).show();
                            //TODO
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeFile(String.valueOf(imageUri));
                }
                //   bitmap = BitmapFactory.decodeFile(String.valueOf(imageUri));
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
                byte[] b = baos.toByteArray();
                params.put("user_id", str_id);
                params.put("selfie", String.valueOf(b));
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == RESULT_OK) {
                //   onCaptureImageResult(data);

                // update_selfie();

                Toast.makeText(this, "CaptureImage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        // intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, REQUEST_CAMERA);

    }


    /*
    Sharing Mehtod's

    */
    public void shareOnTwitter() {

        String urlToShare = str_ref_link;


        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.setType("text/plain");
        tweetIntent.setAction(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
             i.putExtra(Intent.EXTRA_TEXT, urlToShare);
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text="+urlToShare));//+ urlEncode(message)
            startActivity(i);

        }
    }

    public void sharewhatApp() {
        String urlToShare = str_ref_link;
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DashBoardActivity.this, "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
        }

    }

    private void googlePlus() {
        String urlToShare = str_ref_link;
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)

                .setText(urlToShare)

                .getIntent()
                .setPackage("com.google.android.apps.plus");
        try {
            startActivity(shareIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(DashBoardActivity.this, "GooglePlus have not been installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void facebook() {
        String urlToShare = str_ref_link;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        intent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// See if official Facebook app is found
        boolean facebookAppFound = false;
        List<ResolveInfo> matches = getPackageManager().queryIntentActivities(intent, 0);
        for (ResolveInfo info : matches) {
            if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                intent.setPackage(info.activityInfo.packageName);
                facebookAppFound = true;

                break;
            }
        }

// As fallback, launch sharer.php in a browser
        if (!facebookAppFound) {
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + str_ref_link;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));

        }
        startActivity(intent);
    }

}

package com.getfreedash;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sinch.verification.CodeInterceptionException;
import com.sinch.verification.InvalidInputException;
import com.sinch.verification.ServiceErrorException;
import com.sinch.verification.SinchVerification;
import com.sinch.verification.Verification;
import com.sinch.verification.VerificationListener;

public class VerificationActivity extends AppCompatActivity {
    EditText numberPhone;
    Button verify;
    ProgressBar progressBar;
    String Key="2815a687-fa1f-4b03-a2f6-6d95e395b0e5";
    Toolbar toolbar;
    private Verification verification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        initView();
    }
    private void initView() {
        toolbar();
        numberPhone = (EditText) findViewById(R.id.phoneNumber);
        verify = (Button) findViewById(R.id.verify);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        verify.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v) {
                String number = numberPhone.getText().toString();
                if(number.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Phone number cannot be empty!", Toast.LENGTH_LONG).show();
                }
                else {
                    showProgressDialog();
                    startVerification(number);
                }
            }
        });
    }

    private void toolbar(){
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        toolbar.setTitle("  VERIFICATION");
        setSupportActionBar(toolbar);
       /* getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_arrow);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });*/}
    private void startVerification(String phoneNumber) {
        com.sinch.verification.Config config = SinchVerification.config().applicationKey(Key).context(getApplicationContext()).build();
        VerificationListener listener = new MyVerificationListener();
        verification = SinchVerification.createFlashCallVerification(config, phoneNumber, listener);
        verification.initiate();
    }

    private class MyVerificationListener implements VerificationListener {
        @Override
        public void onInitiated() {}

        @Override
        public void onInitiationFailed(Exception e) {
            hideProgressDialog();
            if (e instanceof InvalidInputException) {
                Toast.makeText(VerificationActivity.this,"Incorrect number provided",Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(VerificationActivity.this,"Sinch service error",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VerificationActivity.this,"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onVerified() {
            hideProgressDialog();
            Toast.makeText(VerificationActivity.this, "Verification Successful!",Toast.LENGTH_LONG).show();
            /*new AlertDialog.Builder(VerificationActivity.this)
                    .setMessage("Verification Successful!")
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    })
                    .show();*/
            startActivity(new Intent(VerificationActivity.this,MainActivity.class));
        }

        @Override
        public void onVerificationFailed(Exception e) {
            hideProgressDialog();
            if (e instanceof CodeInterceptionException) {
                Toast.makeText(VerificationActivity.this,"Intercepting the verification call automatically failed",Toast.LENGTH_LONG).show();
            } else if (e instanceof ServiceErrorException) {
                Toast.makeText(VerificationActivity.this, "Sinch service error",Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(VerificationActivity.this,"Other system error, check your network state", Toast.LENGTH_LONG).show();
            }
        }
    }



    private void showProgressDialog()
    {
        progressBar.setVisibility(ProgressBar.VISIBLE);
    }

    private void hideProgressDialog()
    {
        progressBar.setVisibility(ProgressBar.INVISIBLE);
    }

}

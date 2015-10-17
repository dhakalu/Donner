package com.example.upen.donner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by upen on 10/10/15.
 */
public class LoginActivity extends Activity {
    @InjectView(R.id.login_email) EditText mEmailText;
    @InjectView(R.id.login_password) EditText mPasswordText;
    @InjectView(R.id.login_btn) Button mLoginButton;
    @InjectView(R.id.link_signup) TextView mSignupLink;


    private String mEmail;
    private  String mPassword;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        ButterKnife.inject(this);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailText.getText().toString();
                mPassword = mPasswordText.getText().toString();
                login();
            }
        });

        mSignupLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    public void login(){
        if (isValid()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Logging in...");
            progressDialog.show();
            ParseUser.logInInBackground(mEmail, mPassword, new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    progressDialog.dismiss();
                    if (e == null){
                        Toast.makeText(LoginActivity.this, "Login Sucess", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else{
                        Toast.makeText(LoginActivity.this, "Invalid username/ password", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(LoginActivity.this, "Login Failed!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    public boolean isValid(){
        boolean isValid = true;
        if (mEmail.isEmpty()){
            mEmailText.setError("Username cannot be empty");
            isValid = false;
        }

        if (mPassword.isEmpty() || mPassword.length() < 5){
            mPasswordText.setError("Password must have atleast 5 characters");
            isValid = false;
        }
        return isValid;
    }
}


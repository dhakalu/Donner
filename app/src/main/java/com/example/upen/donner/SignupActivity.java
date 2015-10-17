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

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.TooManyListenersException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Upendra Dhakal
 */
public class SignupActivity extends Activity {

    @InjectView(R.id.signup_btn)
    Button mSignupBtn;
    @InjectView(R.id.signup_firstname)
    EditText mFullNameView;
    @InjectView(R.id.signup_username) EditText mUsernameView;
    @InjectView(R.id.signup_email) EditText mEmailView;
    @InjectView(R.id.signup_password) EditText mPasswordView;
    @InjectView(R.id.link_login)
    TextView mLoginLink;


    private String mEmail;
    private String mPassword;
    private String mFullname;
    private String mUsername;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        ButterKnife.inject(this);

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailView.getText().toString();
                mPassword = mPasswordView.getText().toString();
                mFullname = mFullNameView.getText().toString();
                mUsername = mUsernameView.getText().toString();
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    /**
     * This function signs up a user
     */
    public void signup(){
        if(isValid()){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing up....");
            progressDialog.show();
            putUserInDataBase();
        } else {
            Toast.makeText(this, "Signup falied!", Toast.LENGTH_LONG).show();
            return;
        }
    }

    /**
     * This method checks if the user input is valid
     * @return Returns true if user gave the correct information
     */
    public boolean isValid(){
        boolean isValid = true;
        if (mFullname.isEmpty()){
            mFullNameView.setError("Name is a required field");
            isValid = false;
        }

        if (mUsername.isEmpty()){
            mUsernameView.setError("Username is a required field");
            isValid = false;
        }

        if (mEmail.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()){
            mEmailView.setError("Please enter a valid email");
            isValid = false;
        }

        if (mPassword.isEmpty() || mPassword.length() < 5){
            mPasswordView.setError("Password must have atleast 5 characters");
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method puts the user into the database
     */
    public void putUserInDataBase(){
        ParseUser user = new ParseUser();
        user.setUsername(mUsername);
        user.setEmail(mEmail);
        user.setPassword(mPassword);
        user.put("fullname", mFullname);

        user.signUpInBackground(new SignUpCallback() {
            public void done(com.parse.ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    resetForm();
                    Toast.makeText(SignupActivity.this, "Signup Sucess", Toast.LENGTH_LONG).show();
                    startMainActivity();
                } else {
                    Toast.makeText(SignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startMainActivity(){
        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void resetForm(){
        mPasswordView.setText("");
        mUsernameView.setText("");
        mFullNameView.setText("");
        mEmailView.setText("");
    }
}

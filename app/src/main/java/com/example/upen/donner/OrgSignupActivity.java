package com.example.upen.donner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * @author Upendra Dhakal
 */
public class OrgSignupActivity extends Activity {

    @InjectView(R.id.org_signup_btn)
    Button mSignupBtn;
    @InjectView(R.id.org_signup_login_name) EditText mLoginNameView;
    @InjectView(R.id.org_signup_name) EditText mSignUpNameView;
    @InjectView(R.id.org_signup_address) EditText mAddressView;
    @InjectView(R.id.org_signup_email) EditText mEmailView;
    @InjectView(R.id.org_signup_password) EditText mPasswordView;
    @InjectView(R.id.org_signup_phone) EditText mPhoneView;
    @InjectView(R.id.org_spinner_categories) Spinner mCategorySpinner;
    @InjectView(R.id.org_link_login) TextView mLoginLink;


    private String mEmail;
    private String mPassword;
    private String mOrgName;
    private String mLocation;
    private String mLoginName;
    private String mPhoneNumber;
    private String mCategory;
    private String mDescription = "";


    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_org_signup);
        ButterKnife.inject(this);

        mSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEmail = mEmailView.getText().toString();
                mPassword = mPasswordView.getText().toString();
                mOrgName = mSignUpNameView.getText().toString();
                mLocation = mAddressView.getText().toString();
                mLoginName = mLoginNameView.getText().toString();
                mPhoneNumber = mPhoneView.getText().toString();
                mCategory = mCategorySpinner.getSelectedItem().toString();
                signup();
            }
        });

        mLoginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrgSignupActivity.this, LoginActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        if (mOrgName.isEmpty()){
            mSignUpNameView.setError("Name is a required field");
            isValid = false;
        }

        if (mLoginName.isEmpty()){
            mLoginNameView.setError("Username is a required field");
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
        if (mPhoneNumber.isEmpty() || mPhoneNumber.length() < 10){
            mPhoneView.setError("Valid Phone number needed");
            isValid = false;
        }
        if (mLocation.isEmpty()){
            mAddressView.setError("Address needed.");
            isValid = false;
        }
        return isValid;
    }

    /**
     * This method puts the user into the database
     */
    public void putUserInDataBase(){
        Organization user = new Organization();
        user.setLoginName(mLoginName);
        user.setEmail(mEmail);
        user.setLoginPassword(mPassword);
        user.setName(mOrgName);
        user.setPhone(mPhoneNumber);
        user.setLocation(mLocation);
        user.setCatogery(mCategory);
        user.setAmount(0);
        user.setDescription(mDescription);

        user.saveInBackground(new SaveCallback() {
            public void done(com.parse.ParseException e) {
                progressDialog.dismiss();
                if (e == null) {
                    resetForm();
                    Toast.makeText(OrgSignupActivity.this, "Signup Sucess", Toast.LENGTH_LONG).show();
                    startMainActivity();
                } else {
                    Toast.makeText(OrgSignupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void startMainActivity(){
        Intent intent = new Intent(OrgSignupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void resetForm(){
        mPasswordView.setText("");
        mLoginNameView.setText("");
        mSignUpNameView.setText("");
        mEmailView.setText("");
        mPhoneView.setText("");
        mAddressView.setText("");
    }
}


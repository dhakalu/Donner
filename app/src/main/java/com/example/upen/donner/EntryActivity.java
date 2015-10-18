package com.example.upen.donner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class EntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ParseUser.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }else {

            setContentView(R.layout.activity_entry);
            Button signUp = (Button) findViewById(R.id.button_sign_up);
            Button logIn = (Button) findViewById(R.id.button_log_in);
            Button orgSignUpTest = (Button) findViewById(R.id.button_org_sign_up);
            orgSignUpTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(EntryActivity.this, OrgSignupActivity.class));
                }
            });
            logIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if (id == R.id.button_log_in) {
                        Intent intent = new Intent(EntryActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    if (id == R.id.button_sign_up) {
                        Intent intent = new Intent(EntryActivity.this, SignupActivity.class);
                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}

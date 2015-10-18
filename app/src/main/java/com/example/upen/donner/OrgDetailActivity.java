package com.example.upen.donner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by upen on 10/17/15.
 */
public class OrgDetailActivity extends Activity {

    private Organization thisOrg;
    @InjectView(R.id.orgCategory) TextView mOrgCategoryView;
    @InjectView(R.id.orgName) TextView mOrgName;
    @InjectView(R.id.orgLocation) TextView mOrgLocationView;
    @InjectView(R.id.orgDescription) TextView mOrgDescriptionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_details);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String orgId = intent.getStringExtra("id");

        ParseQuery<Organization> parseQuery = ParseQuery.getQuery("Organization");
        parseQuery.whereEqualTo("objectId", orgId);
        parseQuery.findInBackground(new FindCallback<Organization>() {
            @Override
            public void done(List<Organization> list, ParseException e) {
                if (e == null) {
                    thisOrg = list.get(0);
                    mOrgCategoryView.setText(thisOrg.getCatogery());
                    mOrgLocationView.setText(thisOrg.getLocation());
                    mOrgName.setText(thisOrg.getName());
                    mOrgDescriptionView.setText(thisOrg.getDescription());
                }
            }
        });

        mOrgCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmnIntentUri = Uri.parse("geo:0,0?q=" + thisOrg.getLocation());
                Intent intent = new Intent(Intent.ACTION_VIEW, gmnIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                startActivity(intent);
            }
        });
    }
}

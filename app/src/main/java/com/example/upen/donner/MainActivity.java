package com.example.upen.donner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    List<Organization> orgs = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    OrganizationsAdapter adapter;
    private String defaultAddress;
    private boolean isAnywhere;
    private int defaultDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultAddress = sharedPreferences.getString(getString(R.string.pref_key_default_address), Constants.DEFAULT_ADDRESS);
        String distance = sharedPreferences.getString(getString(R.string.pref_key_distance), "26400");
        defaultDistance = Integer.parseInt(distance);
        isAnywhere = sharedPreferences.getBoolean(getString(R.string.pref_key_anywhere), true);
        Log.e("Upen", defaultAddress);
        setContentView(R.layout.activity_main);
        if (ParseUser.getCurrentUser() == null){
            startActivity(new Intent(this, EntryActivity.class));
            finish();
        }
        recyclerView = (RecyclerView) findViewById(R.id.org_lists);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        updateList();
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (id == R.id.action_logout){
            ParseUser.logOutInBackground();
            finish();
        } else  if (id == R.id.action_refresh){
            finish();
            startActivity(getIntent());
        }
        return super.onOptionsItemSelected(item);
    }

    public void updateList(){
        ParseQuery<Organization> query = ParseQuery.getQuery("Organization");
        query.findInBackground(new FindCallback<Organization>() {
            @Override
            public void done(List<Organization> orgList, ParseException e) {
                if (e == null) {

                    if (isAnywhere) orgs = orgList;
                    else{
                        for (Organization currOrg: orgList){
                            Log.e("Upen", currOrg.getLocation());
                            String dis = DistanceUtils.getDistance(defaultAddress, currOrg.getLocation())[0];
                            //Log.e("Upen", dis);
                            if (Integer.parseInt(dis) < defaultDistance){
                                orgs.add(currOrg);
                            }
                        }
                    }
                    adapter = new OrganizationsAdapter(MainActivity.this, orgs);
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

}

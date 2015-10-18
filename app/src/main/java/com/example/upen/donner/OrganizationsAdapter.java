package com.example.upen.donner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 10/17/15.
 * @author Upendra Dhakal
 */
public class OrganizationsAdapter extends RecyclerView.Adapter<OrganizationsAdapter.OrganizationHolder> {

    List<Organization> list;
    Activity activity;

    public OrganizationsAdapter(Activity activity, List<Organization> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public OrganizationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_org, parent, false);
        return new OrganizationHolder(v);
    }

    @Override
    public void onBindViewHolder(OrganizationHolder holder, int position) {
        final Organization currOrg = list.get(position);
        holder.mOrgNameView.setText(currOrg.getName());
        holder.mOrgCategoryView.setText(currOrg.getCatogery());
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(activity);
        String[] distance = DistanceUtils.getDistance(pref.getString(activity.getString(R.string.pref_key_default_address), ""), currOrg.getLocation());
        holder.mOrgLocationView.setText(currOrg.getLocation() + "(" +distance[1] + " / "+ distance[2]+")");

        holder.mOrgNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrgDetailActivity.class);
                intent.putExtra("id", currOrg.getObjectId());
                activity.startActivity(intent);
            }
        });

        holder.mOrgLocationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmnIntentUri = Uri.parse("geo:0,0?q=" + currOrg.getLocation());
                Intent intent = new Intent(Intent.ACTION_VIEW, gmnIntentUri);
                intent.setPackage("com.google.android.apps.maps");
                activity.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return (list == null)? 0: list.size();
    }

    class OrganizationHolder extends RecyclerView.ViewHolder{
        private TextView mOrgNameView;
        private TextView mOrgLocationView;
        private TextView mOrgCategoryView;

        public OrganizationHolder(View itemView) {
            super(itemView);
            mOrgCategoryView = (TextView) itemView.findViewById(R.id.org_catagories);
            mOrgLocationView = (TextView) itemView.findViewById(R.id.org_location);
            mOrgNameView = (TextView) itemView.findViewById(R.id.org_name);
        }
    }
}

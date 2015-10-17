package com.example.upen.donner;

import android.support.v7.widget.RecyclerView;
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

    @Override
    public OrganizationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_org, parent, false);
        return new OrganizationHolder(v);
    }

    @Override
    public void onBindViewHolder(OrganizationHolder holder, int position) {
        Organization currOrg = list.get(position);
        holder.mOrgNameView.setText(currOrg.getName());
        holder.mOrgCategoryView.setText(currOrg.getCatogery());
        holder.mOrgLocationView.setText(currOrg.getLocation());
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

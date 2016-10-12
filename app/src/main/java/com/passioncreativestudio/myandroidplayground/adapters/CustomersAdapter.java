package com.passioncreativestudio.myandroidplayground.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.passioncreativestudio.myandroidplayground.models.Customer;

public class CustomersAdapter extends ArrayAdapter<Customer> {


    public CustomersAdapter(Context context, int resource) {
        super(context, resource);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }
}

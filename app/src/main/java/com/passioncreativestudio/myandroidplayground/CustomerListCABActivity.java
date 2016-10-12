package com.passioncreativestudio.myandroidplayground;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.passioncreativestudio.myandroidplayground.adapters.CustomersAdapter;
import com.passioncreativestudio.myandroidplayground.models.Customer;

import java.util.HashSet;

public class CustomerListCABActivity extends AppCompatActivity {
    private ListView customersListView;
    private HashSet<Customer> selectedCustomers;
    private CustomersAdapter customersAdapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list_cab);

        customersListView = (ListView) findViewById(R.id.activity_customer_list_cab_listView);
        selectedCustomers = new HashSet<>();
        customersAdapter = new CustomersAdapter(this, R.layout.list_item_customer);

        for(int i = 0; i < 100; i++) {
            customersAdapter.add(new Customer("Customer " + Integer.toString(i + 1)));
        }

        customersListView.setAdapter(customersAdapter);

        customersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Customer customer = customersAdapter.getItem(position);

                if(actionMode != null) {
                    toggleCustomerSelection(customer);
                } else {
                    showCustomer(customer);
                }
            }
        });

        customersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                Customer customer = customersAdapter.getItem(position);
                toggleCustomerSelection(customer);

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer_list_cab, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_customer_list_cab_addCustomer) {
            customersAdapter.insert(new Customer("Inserted Customer " + Integer.toString(customersAdapter.getCount())), 0);
            customersAdapter.notifyDataSetChanged();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showCustomer(Customer customer) {
        Toast.makeText(this, "Showing customer " + customer.getName(), Toast.LENGTH_SHORT).show();
    }

    private void deleteCustomers(Iterable<Customer> customers) {
        customersAdapter.setNotifyOnChange(false);

        for(Customer customer : customers) {
            customersAdapter.remove(customer);
        }

        customersAdapter.setNotifyOnChange(true);
        customersAdapter.notifyDataSetChanged();
    }

    private void toggleCustomerSelection(Customer customer) {
        if(selectedCustomers.contains(customer)) {
            selectedCustomers.remove(customer);
        } else {
            selectedCustomers.add(customer);
        }

        if(selectedCustomers.size() == 0 && actionMode != null) {
            actionMode.finish();
            return;
        }

        if(actionMode == null) {
            actionMode = startSupportActionMode(new CustomerActionModeCallback());
        } else {
            actionMode.invalidate();
        }

        customersAdapter.notifyDataSetChanged();
    }

    //region Class ActionMode.Callback
    private class CustomerActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {

        }
    }
    //endregion
}

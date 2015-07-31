package com.example.chungmin.helpu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Hire extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner spHire;
    private Button bOk, bCancel;
    private ServiceSpinAdapter adapter;
    private Service selectedService;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);

        spHire = (Spinner) findViewById(R.id.spHire);
        etDescription = (EditText) findViewById(R.id.etDescription);
        bOk = (Button) findViewById(R.id.bOk);
        bCancel = (Button) findViewById(R.id.bCancel);

        String[] hireArray = getResources().getStringArray(R.array.hire_list);
        Service[] services = new Service[hireArray.length];
        for(int i=0; i<=hireArray.length-1; i++)
        {
            String[] parts = hireArray[i].split("\\|");
            String serviceId = parts[0];
            String service_name = parts[1];
            services[i] = new Service(Integer.parseInt(serviceId), service_name);
        }
        adapter = new ServiceSpinAdapter(this, android.R.layout.simple_spinner_item, services);
        spHire.setOnItemSelectedListener(this);
        spHire.setAdapter(adapter);

        bOk.setOnClickListener(this);
        bCancel.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hire, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position >=1) {
            selectedService = adapter.getItem(position);

            Toast.makeText(this, "You Selected " + selectedService.name + " with ID :" + selectedService.id, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent redirect;
        switch(v.getId()) {
            case R.id.bOk:
                UserLocalStore userLocalStore = new UserLocalStore(this);
                User user = userLocalStore.getLoggedInUser();
                int userId = 0;
                int serviceId = 0;
                String description = "";

                if(user.userId == 0) {
                    Toast.makeText(this, "User ID is null ", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    userId = user.userId;
                }

                if(selectedService == null){
                    Toast.makeText(this, "Service ID is null ", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    serviceId = selectedService.id;
                }

                if(etDescription.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Phone number not fill ", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    description = etDescription.getText().toString();
                }

                CustomerRequest customerRequest = new CustomerRequest(serviceId, userId, description, ProjectStatus.New);
                registerCustomerRequest(customerRequest);

                break;
            case R.id.bCancel:
                redirect = new Intent(this, MainActivity.class);
                startActivity(redirect);
                break;
        }
    }

    private void registerCustomerRequest(CustomerRequest customerRequest) {
        String url = getString(R.string.server_uri)+ ((Globals)getApplication()).getCustomerRequestInsert();
        CustomerRequestServerRequests customerRequestServerRequest = new CustomerRequestServerRequests(this);
        customerRequestServerRequest.storeCustomerRequestDataInBackground(customerRequest, url, new GetCustomerRequestCallback() {
            @Override
            public void done(CustomerRequest returnedCustomerRequest) {
                Intent redirect = new Intent(Hire.this, MainActivity.class);
                startActivity(redirect);
            }
        });
    }
}

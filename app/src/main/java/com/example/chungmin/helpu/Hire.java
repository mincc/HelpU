package com.example.chungmin.helpu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class Hire extends HelpUBaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    private Spinner spHire;
    private Button btnOk, btnCancel;
    private ServiceSpinAdapter adapter;
    private Service selectedService;
    private EditText etDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);

        spHire = (Spinner) findViewById(R.id.spHire);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);

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

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position >=1) {
            selectedService = adapter.getItem(position);

            //Toast.makeText(this, "You Selected " + selectedService.name + " with ID :" + selectedService.id, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent redirect;
        switch(v.getId()) {
            case R.id.btnOk:
                UserLocalStore userLocalStore = new UserLocalStore(this);
                User user = userLocalStore.getLoggedInUser();
                int userId = 0;
                int serviceId = 0;
                String description = "";

                if (user.getUserId() == 0) {
                    Toast.makeText(this, "User ID is null ", Toast.LENGTH_SHORT).show();
                    break;
                }
                else {
                    userId = user.getUserId();
                }

                if(selectedService == null){
                    Toast.makeText(this, "Service ID is null ", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    serviceId = selectedService.id;
                }

                if(etDescription.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Description not fill ", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    description = etDescription.getText().toString();
                }

                btnOk.setEnabled(false);
                CustomerRequest customerRequest = new CustomerRequest(serviceId, userId, description, ProjectStatus.New);
                registerCustomerRequest(customerRequest);


                break;
            case R.id.btnCancel:
                redirect = new Intent(this, MainActivity.class);
                startActivity(redirect);
                finish();
                break;
        }
    }

    private void registerCustomerRequest(final CustomerRequest customerRequest) {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getCustomerRequestInsert();
        CustomerRequestServerRequests customerRequestServerRequest = new CustomerRequestServerRequests();
        customerRequestServerRequest.storeCustomerRequestDataInBackground(customerRequest, url, new GetCustomerRequestCallback() {
            @Override
            public void done(CustomerRequest returnedCustomerRequest) {
                ((Globals) getApplication()).setCustomerRequest(returnedCustomerRequest);
                Intent redirect = new Intent(Hire.this, ServiceProviderListByServiceID.class);
                Bundle b = new Bundle();
                b.putInt("serviceId", customerRequest.getServiceId());
                redirect.putExtras(b);
                startActivity(redirect);
                finish();
            }
        });
    }
}

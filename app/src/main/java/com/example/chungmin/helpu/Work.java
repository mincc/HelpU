package com.example.chungmin.helpu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class Work extends ActionBarActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private Spinner spHire;
    private EditText etPhone, etEmail;
    private Button bRegister;
    private Service selectedService;

    private ServiceSpinAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        spHire = (Spinner) findViewById(R.id.spHire);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        bRegister = (Button) findViewById(R.id.bRegister);

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

        bRegister.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work, menu);
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
    public void onClick(View v) {
        Intent redirect;
        switch(v.getId()) {
            case R.id.bRegister:

                UserLocalStore userLocalStore = new UserLocalStore(this);
                User user = userLocalStore.getLoggedInUser();
                int userId = 0;
                int serviceId = 0;
                String phone ="";
                String email = "";

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

                if(etPhone.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Phone number not fill ", Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    phone = etPhone.getText().toString();
                }

                if(etEmail.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(this, "Email not fill ", Toast.LENGTH_SHORT).show();
                    break;
                }else {
                    email = etEmail.getText().toString();
                }


                ServiceProvider serviceProvider = new ServiceProvider(userId, serviceId, phone, email);
                registerServiceProvider(serviceProvider);

                break;
        }
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

    private void registerServiceProvider(ServiceProvider serviceProvider) {
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getServiceProviderInsert();
        ServiceProviderServerRequests serviceProviderServerRequest = new ServiceProviderServerRequests(this);
        serviceProviderServerRequest.storeServiceProviderDataInBackground(serviceProvider, url, new GetServiceProviderCallback() {
            @Override
            public void done(ServiceProvider returnedServiceProvider) {
                Intent redirect = new Intent(Work.this, ServiceProviderList.class);
                startActivity(redirect);
            }
        });
    }
}

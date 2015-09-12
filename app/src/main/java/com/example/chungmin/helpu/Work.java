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
import android.widget.TextView;
import android.widget.Toast;

import HelpUGenericUtilities.ValidationUtils;


public class Work extends HelpUBaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnFocusChangeListener {

    private Spinner spHire;
    private EditText etContact, etEmail;
    private Button btnRegister;
    private Service mSelectedService;
    private TextView lblType;
    private int mUserId = 0;
    private boolean mIsFirstOnItemSelected = true;
    
    private ServiceSpinAdapter adapter;

    String strContactEmpty;
    String strEmailEmpty;
    String strEmailInvalid;
    String strServiceEmpty;
    String strServiceAlreadyExist;

    boolean mIsServiceValid = false;
    boolean mIsContactValid = false;
    boolean mIsEmailValid = false;
    boolean mIsValid = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work);

        strContactEmpty = getString(R.string.strContactEmpty);
        strEmailEmpty = getString(R.string.strEmailEmpty);
        strEmailInvalid = getString(R.string.strEmailInvalid);
        strServiceEmpty = getString(R.string.strServiceEmpty);
        strServiceAlreadyExist = getString(R.string.strServiceAlreadyExist);

        lblType = (TextView) findViewById(R.id.lblType);
        spHire = (Spinner) findViewById(R.id.spHire);
        etContact = (EditText) findViewById(R.id.etContact);
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnRegister = (Button) findViewById(R.id.btnRegister);

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

        etContact.setOnFocusChangeListener(this);
        etEmail.setOnFocusChangeListener(this);
        btnRegister.setOnClickListener(this);

        UserLocalStore userLocalStore = new UserLocalStore(this);
        User user = userLocalStore.getLoggedInUser();
        if (user.getUserId() == 0) {
            Toast.makeText(this, "User ID is null ", Toast.LENGTH_SHORT).show();
        } else {
            mUserId = user.getUserId();
        }

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btnRegister:

                int serviceId = 0;
                String phone ="";
                String email = "";

                validateDdlService();
                validateContact();
                validateEmail();

                if (mSelectedService == null) {
                    return;
                }
                serviceId = mSelectedService.id;
                phone = etContact.getText().toString();
                email = etEmail.getText().toString();


                if (mIsServiceValid && mIsEmailValid && mIsContactValid) {
                    mIsValid = true;
                }else {
                    mIsValid = false;
                }

                if (mIsValid) {
                    //to prevent execute multiple time
                    btnRegister.setEnabled(false);
                    ServiceProvider serviceProvider = new ServiceProvider(mUserId, serviceId, phone, email);
                    registerServiceProvider(serviceProvider);
                }

                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (mIsFirstOnItemSelected) {
            mIsFirstOnItemSelected = false;
        } else {
            if (position >= 1) {
                mSelectedService = adapter.getItem(position);

                validateDdlService();
//            Toast.makeText(this, "You Selected " + mSelectedService.name + " with ID :" + mSelectedService.id, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        showToast("On Noting Selected");
    }

    private void registerServiceProvider(ServiceProvider serviceProvider) {
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getServiceProviderInsert();
        ServiceProviderServerRequests serviceProviderServerRequest = new ServiceProviderServerRequests();
        serviceProviderServerRequest.storeServiceProviderDataInBackground(serviceProvider, url, new GetServiceProviderCallback() {
            @Override
            public void done(ServiceProvider returnedServiceProvider) {
                Intent redirect = new Intent(Work.this, ServiceProviderList.class);
                startActivity(redirect);
                finish();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etContact:
                    validateContact();
                    break;
                case R.id.etEmail:
                    validateEmail();
                    break;
                default:
                    showToast(getString(R.string.strUnknownAction));
                    break;
            }
        }
    }

    public void validateDdlService() {

        int serviceId = 0;

        if (mSelectedService != null) {
            serviceId = mSelectedService.id;
            lblType.setError(null);
        } else {
            lblType.setError(strServiceEmpty);
            mIsServiceValid = false;
            return;
        }

        if (serviceId >= 1) {
            String url = getString(R.string.server_uri) + ((Globals) getApplication()).serviceProviderIsServiceAlreadyExists();
            ServiceProviderServerRequests serverRequest = new ServiceProviderServerRequests();
            serverRequest.isServiceProviderAlreadyExists(mUserId, serviceId, url, new GetBooleanCallback() {
                @Override
                public void done(Boolean isServiceProviderAlreadyExists) {
                    if (isServiceProviderAlreadyExists) {
                        lblType.setError(strServiceAlreadyExist);
                        mIsServiceValid = false;
                    } else {
                        lblType.setError(null);
                        mIsServiceValid = true;
                    }
                }
            });
        }
    }

    public void validateContact() {
        if (etContact.getText().toString().equals("")) {
            etContact.setError(strContactEmpty);
            mIsContactValid = false;
        } else {
            mIsContactValid = true;
        }
    }

    public void validateEmail() {
        String email = etEmail.getText().toString();
        if (email.equals("")) {
            etEmail.setError(strEmailEmpty);
            mIsEmailValid = false;
            return;
        } else {
            mIsEmailValid = true;
        }

        if (!ValidationUtils.isValidEmail(email)) {
            etEmail.setError(strEmailInvalid);
            mIsEmailValid = false;
        } else {
            mIsEmailValid = true;
        }
    }
}

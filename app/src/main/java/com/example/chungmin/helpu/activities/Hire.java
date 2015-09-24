package com.example.chungmin.helpu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chungmin.helpu.models.CustomerRequest;
import com.example.chungmin.helpu.serverrequest.CustomerRequestManager;
import com.example.chungmin.helpu.callback.GetCustomerRequestCallback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.enumeration.ProjectStatus;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.Service;
import com.example.chungmin.helpu.adapter.ServiceSpinAdapter;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;


public class Hire extends HelpUBaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnFocusChangeListener {

    private Spinner spHire;
    private Button btnOk, btnCancel;
    private ServiceSpinAdapter adapter;
    private Service mSelectedService;
    private EditText etDescription;
    private TextView tvServiceNeeded;
    private boolean mIsFirstOnItemSelected = true;

    String strServiceNeededEmpty;
    String strDescriptionEmpty;

    boolean mIsServiceNeededValid = false;
    boolean mIsDescriptionValid = false;
    boolean mIsValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hire);

        strServiceNeededEmpty = getString(R.string.strServiceEmpty);
        strDescriptionEmpty = getString(R.string.strDescriptionEmpty);

        tvServiceNeeded = (TextView) findViewById(R.id.tvServiceNeeded);
        spHire = (Spinner) findViewById(R.id.spHire);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnOk = (Button) findViewById(R.id.btnOk);
        btnCancel = (Button) findViewById(R.id.btnCancel);

        String[] hireArray = getResources().getStringArray(R.array.hire_list);
        Service[] services = new Service[hireArray.length];
        for (int i = 0; i <= hireArray.length - 1; i++) {
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
        if (mIsFirstOnItemSelected) {
            mIsFirstOnItemSelected = false;
        } else {
            mSelectedService = adapter.getItem(position);
            //Toast.makeText(this, "You Selected " + mSelectedService.name + " with ID :" + mSelectedService.id, Toast.LENGTH_SHORT).show();
            validateServiceNeeded();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Intent redirect;
        switch (v.getId()) {
            case R.id.btnOk:
                validateServiceNeeded();
                validateDescription();

                if (mIsServiceNeededValid && mIsDescriptionValid) {
                    mIsValid = true;
                } else {
                    mIsValid = false;
                }

                if (mIsValid) {
                    UserLocalStore userLocalStore = new UserLocalStore(this);
                    User user = userLocalStore.getLoggedInUser();
                    int userId = 0;
                    int serviceId = 0;
                    String description = "";

                    if (user.getUserId() == 0) {
                        Toast.makeText(this, "User ID is null ", Toast.LENGTH_SHORT).show();
                        break;
                    } else {
                        userId = user.getUserId();
                    }

                    serviceId = mSelectedService.id;
                    description = etDescription.getText().toString();

                    btnOk.setEnabled(false);
                    CustomerRequest customerRequest = new CustomerRequest(serviceId, userId, description, ProjectStatus.New);
                    registerCustomerRequest(customerRequest);

                }
                break;
            case R.id.btnCancel:
                redirect = new Intent(this, MainActivity.class);
                startActivity(redirect);
                finish();
                break;
        }
    }

    private void registerCustomerRequest(final CustomerRequest customerRequest) {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getCustomerRequestInsertUrl();
        CustomerRequestManager customerRequestServerRequest = new CustomerRequestManager();
        customerRequestServerRequest.insert(customerRequest, url, new GetCustomerRequestCallback() {
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

    public void validateServiceNeeded() {
        if (mSelectedService == null) {
            tvServiceNeeded.setError(strServiceNeededEmpty);
            ;
            mIsServiceNeededValid = false;
        } else {
            if (mSelectedService.id == 0) {
                tvServiceNeeded.setError(strServiceNeededEmpty);
                mIsServiceNeededValid = false;
            } else {
                tvServiceNeeded.setError(null);
                mIsServiceNeededValid = true;
            }
        }
    }

    public void validateDescription() {
        String description = etDescription.getText().toString();

        if (description.equals("")) {
            etDescription.setError(strDescriptionEmpty);
            mIsDescriptionValid = false;
        } else {
            mIsDescriptionValid = true;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            switch (v.getId()) {
                case R.id.etDescription:
                    validateDescription();
                    break;
                default:
                    showToast(getString(R.string.strUnknownAction));
                    break;
            }
        }
    }
}

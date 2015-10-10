package com.example.chungmin.helpu.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.service.AppLocationService;

import HelpUGenericUtilities.GeocodingLocation;

public class LocationActivity extends HelpUBaseActivity implements View.OnClickListener {

    private RadioGroup rgLocationGetType;
    private RelativeLayout rlAddressPortion, rlGetLocation, rlLocationPortion;
    private EditText etAddress, etLatitude, etLongitude;
    private Button btnGetLocation, btnShowLocation;
    private RadioButton rbType, rbByGPS, rbByAddress, rbManual;
    AppLocationService appLocationService;
    double mLatitude = 0.0;
    double mLongitude = 0.0;
    private TextView tvMessageBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        appLocationService = new AppLocationService(
                LocationActivity.this);

        rgLocationGetType = (RadioGroup) findViewById(R.id.rgLocationGetType);
        rlAddressPortion = (RelativeLayout) findViewById(R.id.rlAddressPortion);
        rlGetLocation = (RelativeLayout) findViewById(R.id.rlGetLocation);
        rlLocationPortion = (RelativeLayout) findViewById(R.id.rlLocationPortion);
        etAddress = (EditText) findViewById(R.id.etAddress);
        etLatitude = (EditText) findViewById(R.id.etLatitude);
        etLongitude = (EditText) findViewById(R.id.etLongitude);
        btnGetLocation = (Button) findViewById(R.id.btnGetLocation);
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        rbByGPS = (RadioButton) findViewById(R.id.rbByGPS);
        rbByAddress = (RadioButton) findViewById(R.id.rbByAddress);
        rbManual = (RadioButton) findViewById(R.id.rbManual);
        tvMessageBox = (TextView) findViewById(R.id.tvMessageBox);

        rbByGPS.setOnClickListener(this);
        rbByAddress.setOnClickListener(this);
        rbManual.setOnClickListener(this);
        btnGetLocation.setOnClickListener(this);
        btnShowLocation.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int selectedId = 0;
        switch (v.getId()) {
            case R.id.rbByGPS:
                etLatitude.setEnabled(false);
                etLongitude.setEnabled(false);
                rlAddressPortion.setVisibility(View.GONE);
                rlGetLocation.setVisibility(View.VISIBLE);
                rlLocationPortion.setVisibility(View.GONE);
                break;
            case R.id.rbByAddress:
                etLatitude.setEnabled(false);
                etLongitude.setEnabled(false);
                rlAddressPortion.setVisibility(View.VISIBLE);
                rlGetLocation.setVisibility(View.VISIBLE);
                rlLocationPortion.setVisibility(View.GONE);
                break;
            case R.id.rbManual:
                etLatitude.setEnabled(true);
                etLongitude.setEnabled(true);
                rlAddressPortion.setVisibility(View.GONE);
                rlGetLocation.setVisibility(View.GONE);
                rlLocationPortion.setVisibility(View.VISIBLE);
                break;
            case R.id.btnGetLocation:
                selectedId = rgLocationGetType.getCheckedRadioButtonId();
                rbType = (RadioButton) findViewById(selectedId);
                switch (rbType.getId()) {
                    case R.id.rbByGPS:
                        getLocationByGPS();
                        break;
                    case R.id.rbByAddress:
                        getLocationByAddress();
                        break;
                }

                break;
            case R.id.btnShowLocation:
                selectedId = rgLocationGetType.getCheckedRadioButtonId();
                rbType = (RadioButton) findViewById(selectedId);
                switch (rbType.getId()) {
                    case R.id.rbByGPS:
                    case R.id.rbByAddress:
                        break;
                    case R.id.rbManual:
                        getLocationByManual();
                        break;
                }

                Intent redirect = new Intent(this, MapsActivity.class);
                redirect.putExtra("mLatitude", mLatitude);
                redirect.putExtra("mLongitude", mLongitude);
                startActivity(redirect);
                break;
        }
    }

    private void getLocationByManual() {
        double latitude = Double.parseDouble(etLatitude.getText().toString());
        double longitude = Double.parseDouble(etLongitude.getText().toString());

        if (latitude != 0.0 && longitude != 0.0) {
            mLatitude = latitude;
            mLongitude = longitude;
        } else {
            tvMessageBox.setText("Please reenter the value");
        }
    }

    private void getLocationByAddress() {
        String address = etAddress.getText().toString();

        GeocodingLocation locationAddress = new GeocodingLocation();
        locationAddress.getAddressFromLocation(address,
                getApplicationContext(), new GeocoderHandler());
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            switch (message.what) {
                case 1:
                    mLatitude = bundle.getDouble("latitude");
                    mLongitude = bundle.getDouble("longitude");

                    etLatitude.setText(Double.toString(mLatitude));
                    etLongitude.setText(Double.toString(mLongitude));

                    rlAddressPortion.setVisibility(View.GONE);
                    rlGetLocation.setVisibility(View.GONE);
                    rlLocationPortion.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    String result = bundle.getString("result");
                    tvMessageBox.setText(result);
                    break;
            }

        }
    }

    private void getLocationByGPS() {
        Location gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            mLatitude = gpsLocation.getLatitude();
            mLongitude = gpsLocation.getLongitude();
            etLatitude.setText(Double.toString(mLatitude));
            etLongitude.setText(Double.toString(mLongitude));

            rlAddressPortion.setVisibility(View.GONE);
            rlGetLocation.setVisibility(View.GONE);
            rlLocationPortion.setVisibility(View.VISIBLE);
        } else {
            showSettingsAlert();
        }
    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                LocationActivity.this);
        alertDialog.setTitle("SETTINGS");
        alertDialog.setMessage("Couldn't get the location. Make sure location is enabled on the device ( If already turn on the location provider, please wait a few second before get your location. ). Go to settings menu?");
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        LocationActivity.this.startActivity(intent);
                    }
                });
        alertDialog.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }
}

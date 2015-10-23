package com.example.chungmin.helpu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.serverrequest.UserManager;


public class ServerInfo extends HelpUBaseActivity implements View.OnClickListener {
    private TextView tvServer;

    private RadioGroup rgServer;
    private RadioButton rbServer;
    private Button btnConnectionChecking;

    EditText etUsername, etPassword;
    TextView tvResult;

    private String mUsername = "apple";
    private String mPassword = "Password123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_info);

        tvServer = (TextView) findViewById(R.id.tvServer);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvResult = (TextView) findViewById(R.id.tvResult);
        rgServer = (RadioGroup) findViewById(R.id.rgServer);
        btnConnectionChecking = (Button) findViewById(R.id.btnConnectionChecking);

        tvServer.setText(getString(R.string.server_uri));
        etUsername.setText(mUsername);
        etPassword.setText(mPassword);

        btnConnectionChecking.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // get selected radio button from radioGroup
        int selectedId = rgServer.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        rbServer = (RadioButton) findViewById(selectedId);

        String redirect = rbServer.getText().toString();

//                Toast.makeText(ServerInfo.this,
//                        rbServer.getText(), Toast.LENGTH_SHORT).show();

        mUsername = etUsername.getText().toString();
        mPassword = etPassword.getText().toString();

        User user = new User(0, "", mUsername, mPassword, "", "", 0, "");
        UserManager serverRequest = new UserManager(this);
        serverRequest.login(user, new Callback.GetUserCallback() {
            @Override
            public void complete(User returnedUser) {
                if (returnedUser == null) {
                    tvResult.setText("Retrieve Fail");
                } else {
                    tvResult.setText("Retrieve Successful");
                }
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
    }
}

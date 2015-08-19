package com.example.chungmin.helpu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Register extends ActionBarActivity implements View.OnClickListener{

    EditText etName, etUsername, etPassword, etUserContact, etUserEmail;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etUserContact = (EditText) findViewById(R.id.etUserContact);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRegister:
                String name = etName.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String userContact = etUserContact.getText().toString();
                String userEmail = etUserEmail.getText().toString();

                User user = new User(-1, name, username, password, userContact, userEmail);
                registerUser(user);
                break;
        }
    }

    private void registerUser(User user) {
        String url = getString(R.string.server_uri) + ((Globals)getApplication()).getUserInsert();
        UserServerRequests serverRequest = new UserServerRequests(this);
        serverRequest.storeUserDataInBackground(user, url, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }
}

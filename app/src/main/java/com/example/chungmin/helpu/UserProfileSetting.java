package com.example.chungmin.helpu;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class UserProfileSetting extends ActionBarActivity implements View.OnClickListener {
    UserLocalStore userLocalStore;
    User mUser = null;
    EditText etName, etUsername, etUserContact, etUserEmail;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_setting);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etUserContact = (EditText) findViewById(R.id.etUserContact);
        etUserEmail = (EditText) findViewById(R.id.etUserEmail);
        btnSave = (Button) findViewById(R.id.btnSave);

        userLocalStore = new UserLocalStore(this);
        displayUserDetails();

        btnSave.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_profile_setting, menu);
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

    private void displayUserDetails() {
        mUser = userLocalStore.getLoggedInUser();
        etUsername.setText(mUser.getUsername());
        etName.setText(mUser.getUserName());
        etUserContact.setText(mUser.getUserContact());
        etUserEmail.setText(mUser.getUserEmail());
    }

    @Override
    public void onClick(View v) {
        if (v == btnSave) {

            if (!etName.getText().toString().equals("")) {
                mUser.setUserName(etName.getText().toString());
            } else {
                Toast.makeText(this, "User Name Can Not Empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!etUserContact.getText().toString().equals("")) {
                mUser.setUserContact(etUserContact.getText().toString());
            } else {
                Toast.makeText(this, "User Contact Can Not Empty!", Toast.LENGTH_SHORT).show();
                return;
            }


            if (!etUserEmail.getText().toString().equals("")) {
                mUser.setUserEmail(etUserEmail.getText().toString());
            } else {
                Toast.makeText(this, "User Email Can Not Empty!", Toast.LENGTH_SHORT).show();
                return;
            }


            updateUser(mUser);
        }
    }

    private void updateUser(final User user) {
        String url = getString(R.string.server_uri) + ((Globals) getApplication()).getUserUpdate();
        UserServerRequests serverRequest = new UserServerRequests(this);
        serverRequest.getUserUpdate(user, url, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                userLocalStore.storeUserData(user);
                Intent redirect = new Intent(UserProfileSetting.this, MainActivity.class);
                redirect.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(redirect);
                finish();
            }
        });
    }
}

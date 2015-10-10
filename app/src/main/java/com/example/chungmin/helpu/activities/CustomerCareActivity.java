package com.example.chungmin.helpu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.chungmin.helpu.R;
import com.example.chungmin.helpu.callback.Callback;
import com.example.chungmin.helpu.enumeration.CustomerIssueStatus;
import com.example.chungmin.helpu.enumeration.CustomerIssueTypes;
import com.example.chungmin.helpu.models.CustomerIssue;
import com.example.chungmin.helpu.models.Globals;
import com.example.chungmin.helpu.models.User;
import com.example.chungmin.helpu.models.UserLocalStore;
import com.example.chungmin.helpu.serverrequest.CustomerIssueManager;
import com.example.chungmin.helpu.task.Task;

import HelpUGenericUtilities.ValidationUtils;

public class CustomerCareActivity extends HelpUBaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private String strTypeEmpty;
    private String strSubjectEmpty;
    private String strDescriptionEmpty;

    private TextView tvType;
    private Spinner spType;
    private EditText etSubject, etDescription;
    private Button btnCancel, btnSubmit;

    private boolean mIsTypeValid = false;
    private boolean mIsSubjectValid = false;
    private boolean mIsDescriptionValid = false;
    private boolean mIsValid = false;
    private boolean mIsFirstOnItemSelected = true;
    private int mSelectedType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_care);

        strTypeEmpty = getString(R.string.strTypeEmpty);
        strSubjectEmpty = getString(R.string.strSubjectEmpty);
        strDescriptionEmpty = getString(R.string.strDescriptionEmpty);

        tvType = (TextView) findViewById(R.id.tvType);
        spType = (Spinner) findViewById(R.id.spType);
        etSubject = (EditText) findViewById(R.id.etSubject);
        etDescription = (EditText) findViewById(R.id.etDescription);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        spType.setAdapter(new ArrayAdapter<CustomerIssueTypes>(this, android.R.layout.simple_spinner_item, CustomerIssueTypes.values()));
        spType.setOnItemSelectedListener(this);

        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent redirect;
        switch (v.getId()) {
            case R.id.btnSubmit:
                validateType();
                validateSubject();
                validateDescription();

                if (mIsTypeValid && mIsSubjectValid && mIsDescriptionValid) {
                    mIsValid = true;
                } else {
                    mIsValid = false;
                }

                if (mIsValid) {
                    UserLocalStore userLocalStore = new UserLocalStore(this);
                    User user = userLocalStore.getLoggedInUser();

                    btnSubmit.setEnabled(false);
                    CustomerIssue customerIssue = new CustomerIssue();
                    customerIssue.setUserId(user.getUserId());
                    customerIssue.setCustomerIssueTypeId(mSelectedType);
                    customerIssue.setCustomerIssueStatus(CustomerIssueStatus.NewIssue);
                    customerIssue.setSubject(etSubject.getText().toString());
                    customerIssue.setDescription(etDescription.getText().toString());
                    submitCustomerIssue(customerIssue);

                }
                break;
            case R.id.btnCancel:
                redirect = new Intent(this, MainActivity.class);
                startActivity(redirect);
                finish();
                break;
        }
    }

    private void submitCustomerIssue(CustomerIssue customerIssue) {
        CustomerIssueManager customerIssueServerRequest = new CustomerIssueManager();
        customerIssueServerRequest.insert(customerIssue, new Callback.GetCustomerIssueCallback() {
            @Override
            public void complete(CustomerIssue returnedCustomerIssue) {
                //send email with new password
                Task task = new Task();
                String gmailUsername = "menugarden10@gmail.com";
                String gmailPassword = "1029384756abc";
                String emailRecipients = "bengsnail2002@yahoo.com";
                String emailSender = "menugarden@gmail.com";
                String emailSubject = getString(R.string.strCustomerCare) + " : " + returnedCustomerIssue.getSubject();
                String emailBody = getEmailBody(returnedCustomerIssue);
                task.sendEmail(gmailUsername, gmailPassword, emailRecipients, emailSender, emailSubject, emailBody);


                Intent redirect = new Intent(CustomerCareActivity.this, MainActivity.class);
                startActivity(redirect);
                finish();
            }

            @Override
            public void failure(String msg) {
                msg = ((Globals) getApplication()).translateErrorType(msg);
                showAlert(msg);
            }
        });
    }

    private String getEmailBody(CustomerIssue customerIssue) {
        String msg = "";
        msg = "<html>" +
                "   <body>" +
                "       <h4><b>Customer Care ID : %1$s</b></h4>" +
                "       <p> \n" +
                "           Type : %2$s<br/>" +
                "           Description : %3$s <br/>" +
                "       </p>" +
                "       <p>" +
                "           <br/><br/>Best regards,<br/>" +
                "           <b><i>HelpU Support</i></b>" +
                "       </p>" +
                "   </body>" +
                "</html>";
        msg = String.format(msg, customerIssue.getCustomerIssueId(),
                customerIssue.getCustomerIssueTypes().toString(), customerIssue.getDescription());
        return msg;
    }

    private void validateDescription() {
        mIsDescriptionValid = ValidationUtils.isEditTextFill(etDescription);
        if (!mIsDescriptionValid) {
            etDescription.setError(strDescriptionEmpty);
        }
    }

    private void validateSubject() {
        mIsSubjectValid = ValidationUtils.isEditTextFill(etSubject);
        if (!mIsSubjectValid) {
            etSubject.setError(strSubjectEmpty);
        }
    }

    private void validateType() {
        mIsTypeValid = ValidationUtils.isSpinnerSelected(mSelectedType);

//        TextView spinnerTextView = ((TextView)spType.getSelectedView());
        if (!mIsTypeValid) {
//            tvType.setError(strTypeEmpty);
//            spinnerTextView.setFocusable(true);
//            spinnerTextView.setFocusableInTouchMode(true);
//            spinnerTextView.setError(strTypeEmpty);
            SetError(strTypeEmpty);
        } else {
//            tvType.setError(null);
//            spinnerTextView.setFocusable(false);
//            spinnerTextView.setFocusableInTouchMode(false);
//            spinnerTextView.setError(null);
            SetError(null);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if (mIsFirstOnItemSelected) {
            mIsFirstOnItemSelected = false;
        } else {
            CustomerIssueTypes customerIssueType = (CustomerIssueTypes) parent.getItemAtPosition(position);
            mSelectedType = customerIssueType.getId();
            validateType();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    /**
     * When a <code>errorMessage</code> is specified, pops up an error window with the message
     * text, and creates an error icon in the secondary unit spinner. Error cleared through passing
     * in a null string.
     *
     * @param errorMessage Error message to display, or null to clear.
     */
    public void SetError(String errorMessage) {
        View view = spType.getSelectedView();

        // Set TextView in Secondary Unit spinner to be in error so that red (!) icon
        // appears, and then shake control if in error
        TextView tvListItem = (TextView) view;

        // Set fake TextView to be in error so that the error message appears
        TextView tvInvisibleError = (TextView) findViewById(R.id.tvInvisibleError);

        // Shake and set error if in error state, otherwise clear error
        if (errorMessage != null) {
            tvListItem.setError(errorMessage);
            tvListItem.requestFocus();

            // Shake the spinner to highlight that current selection
            // is invalid -- SEE COMMENT BELOW
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            spType.startAnimation(shake);

            tvInvisibleError.requestFocus();
            tvInvisibleError.setError(errorMessage);
        } else {
            tvListItem.setError(null);
            tvInvisibleError.setError(null);
        }
    }
}

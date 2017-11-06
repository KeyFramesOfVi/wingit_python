package com.example.manuel.wingittest2;

//form checking matches website registration page
//nneeds to connect to our server but we didn't finish our REST api

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {


    //UI elements

    private EditText regFullName;
    private EditText regUserName;
    private EditText regEmail;
    private EditText regPassword;
    private EditText regConfirmPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //grab appropriate views
        regFullName = (EditText)findViewById(R.id.etRegName);
        regUserName = (EditText)findViewById(R.id.etRegUser);
        regEmail = (EditText)findViewById(R.id.etRegEmail);
        regPassword = (EditText)findViewById(R.id.etRegPass);
        regConfirmPass = (EditText)findViewById(R.id.etRegConfirmPass);


    }

    public void RegisterClicked(View view){
        // Reset errors.
        regFullName.setError(null);
        regUserName.setError(null);
        regEmail.setError(null);
        regPassword.setError(null);
        regConfirmPass.setError(null);

        boolean cancel = false;
        View focusView = null;

        //grab values
        String FullName = regFullName.getText().toString();
        String UserName = regUserName.getText().toString();
        String Email = regEmail.getText().toString();
        String Password = regPassword.getText().toString();
        String ConfirmPass = regConfirmPass.getText().toString();

        //check the values

        if(TextUtils.isEmpty(FullName)) {
            regFullName.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = regFullName;
        }

        if(TextUtils.isEmpty(UserName)) {
            regUserName.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = regUserName;
        }
        else if (!isUserNameValid(UserName)){
            regUserName.setError(getString(R.string.error_invalid_username));
            cancel = true;
            focusView = regUserName;
        }

        if(TextUtils.isEmpty(Email)) {
            regEmail.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = regEmail;
        }
        else if (!isEmailValid(Email)){
            regEmail.setError(getString(R.string.error_invalid_email));
            cancel = true;
            focusView = regEmail;

        }

        if(TextUtils.isEmpty(Password)) {
            regPassword.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = regPassword;
        }
        else if (!isPasswordValid(Password)){
            regPassword.setError(getString(R.string.error_invalid_password));
            cancel = true;
            focusView = regPassword;
        }

        if(TextUtils.isEmpty(ConfirmPass)) {
            regConfirmPass.setError(getString(R.string.error_field_required));
            cancel = true;
            focusView = regConfirmPass;
        }
        else if (!PasswordsMatch(Password, ConfirmPass)){
            regConfirmPass.setError(getString(R.string.error_password_match));
            cancel = true;
            focusView = regConfirmPass;
        }



        //If there was an error, focus on the error
        if (cancel){
            focusView.requestFocus();
        }
        else {

            InternetTester it = new InternetTester(getApplicationContext());
            Boolean isConnected = it.isConnectingToInternet();
            // check for Internet status
            if (isConnected) {
                // Internet Connection is Present
                // make HTTP requests
            } else {
                // Internet connection is not present
                // Ask user to connect to Internet
                showAlertDialog(RegisterActivity.this, "No Internet Connection",
                        "You don't have internet connection.", false);
            }
        }

    }
    //check if inputs are valid
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isUserNameValid(String UserName) {
        return (UserName.length()<30 && !(UserName.contains(" ")));
    }

    private boolean isPasswordValid(String password) {
        return (password.length() > 7 && !(password.contains(" ")));
    }

    private boolean PasswordsMatch(String password, String confirm){
        return password.equals(confirm);
    }
        /**
         * Function to display simple Alert Dialog
         * @param context - application context
         * @param title - alert dialog title
         * @param message - alert message
         * @param status - success/failure (used to set icon)
         * */
        public void showAlertDialog(Context context, String title, String message, Boolean status) {
            AlertDialog alertDialog = new AlertDialog.Builder(context).create();

            // Setting Dialog Title
            alertDialog.setTitle(title);

            // Setting Dialog Message
            alertDialog.setMessage(message);

            // Setting OK Button
          //  alertDialog.setButton(1,);


            // Showing Alert Message
            alertDialog.show();
        }
}

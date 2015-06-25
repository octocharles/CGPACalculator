package com.pendragon.xavier.cgpacalculator;

import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    private EditText username, password, recoveryQuestion, recoveryAnswer;
    private Button signIn, register;
    private Cursor c;
    private TextView forgotPassword;

    //database row ID of the User and Record
    private long rowID, recordRowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login/Sign Up");

        username = (EditText) findViewById(R.id.usernameEditText);
        password = (EditText) findViewById(R.id.passwordEditText);
        recoveryQuestion = (EditText) findViewById(R.id.recoveryQuestionEditText);
        recoveryAnswer = (EditText) findViewById(R.id.recoveryAnswerEditText);
        signIn = (Button) findViewById(R.id.signInButton);
        register = (Button) findViewById(R.id.registerButton);
        signIn.setOnClickListener(signInClick);
        register.setOnClickListener(registerClick);

        forgotPassword = (TextView) findViewById(R.id.forgotPasswordText);
        forgotPassword.setOnClickListener(forgotPasswordListener);

        c = this.managedQuery(MyContentProvider.USER_CONTENT_URI, null, null, null, null);

        if (c.getCount() < 1){
            signIn.setVisibility(View.GONE);
            forgotPassword.setVisibility(View.GONE);
        }
        else {
            register.setVisibility(View.GONE);
            recoveryQuestion.setVisibility(View.GONE);
            recoveryAnswer.setVisibility(View.GONE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    View.OnClickListener signInClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((username.getText().toString()).equals(c.getString(c.getColumnIndex(MyContentProvider.COLUMN_USERNAME))) &&
                    (password.getText().toString()).equals(c.getString(c.getColumnIndex(MyContentProvider.COLUMN_PASSWORD)))){

                Intent i = new Intent(getApplicationContext(), UserHome.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Welcome " +
                        (c.getString(c.getColumnIndex(MyContentProvider.COLUMN_USERNAME))), Toast.LENGTH_LONG).show();
            }

            else {
                if (!(username.getText().toString()).equals(c.getString(c.getColumnIndex(MyContentProvider.COLUMN_USERNAME)))){
                    DialogFragment errorSigningIn = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            //declare the AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("The Username Entered is Incorrect");
                            builder.setPositiveButton("OK", null);
                            return builder.create();
                        }
                    };
                    errorSigningIn.show(getSupportFragmentManager(), "Error Signing In");
                }
                else {
                    DialogFragment errorSigningIn = new DialogFragment() {
                        @Override
                        public Dialog onCreateDialog(Bundle savedInstanceState) {
                            //declare the AlertDialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setMessage("The Password Entered is Incorrect");
                            builder.setPositiveButton("OK", null);
                            return builder.create();
                        }
                    };
                    errorSigningIn.show(getSupportFragmentManager(), "Error Signing In");
                }
            }
        }
    };

    View.OnClickListener registerClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (username.getText().toString().trim().length() > 3 && password.getText().toString().trim().length() >= 6 &&
                    recoveryQuestion.getText().toString().trim().length() != 0 && recoveryAnswer.getText().toString().trim().length() != 0) {
                ContentValues userValues = new ContentValues();
                userValues.put(MyContentProvider.COLUMN_ROWID, rowID);
                userValues.put(MyContentProvider.COLUMN_USERNAME, username.getText().toString());
                userValues.put(MyContentProvider.COLUMN_PASSWORD, password.getText().toString());
                userValues.put(MyContentProvider.COLUMN_RECOVERY_QUESTION, recoveryQuestion.getText().toString());
                userValues.put(MyContentProvider.COLUMN_RECOVERY_ANSWER, recoveryAnswer.getText().toString());
                userValues.put(MyContentProvider.COLUMN_CGPA, 0.00);
                userValues.put(MyContentProvider.COLUMN_TOTAL_POINT, 0);
                userValues.put(MyContentProvider.COLUMN_TOTAL_UNIT, 0);

                Uri uri = getApplicationContext().getContentResolver().insert(MyContentProvider.USER_CONTENT_URI, userValues);
                rowID = ContentUris.parseId(uri);

                for (int i = 0; i <= 12; i++){
                    ContentValues recordValues = new ContentValues();
                    recordValues.put(MyContentProvider.RECORD_ROWID, recordRowID);
                    recordValues.put(MyContentProvider.RECORD_COLUMN_COURSE, "0");
                    recordValues.put(MyContentProvider.RECORD_COLUMN_UNIT, "0");
                    recordValues.put(MyContentProvider.RECORD_COLUMN_GRADE, "0");
                    recordValues.put(MyContentProvider.RECORD_COLUMN_SESSION, 0);
                    Uri uri1 = getApplicationContext().getContentResolver().insert(MyContentProvider.RECORD_CONTENT_URI, recordValues);
                    recordRowID = ContentUris.parseId(uri1);
                }

                Intent i = new Intent(getApplicationContext(), UserHome.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
            }

            else {
                if (username.getText().toString().trim().length() < 4){
                    username.setError("Username has to be greater the 4 Characters");
                }
                else if (password.getText().toString().trim().length() < 6){
                    password.setError("Password must be at least 6 Characters");
                }
                else if (recoveryQuestion.getText().toString().trim().length() == 0){
                    recoveryQuestion.setError("This Field is required");
                }
                else {
                    recoveryAnswer.setError("This Field is required");
                }
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

        c.close();
        finish();
    }

    View.OnClickListener forgotPasswordListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), RecoveryActivity.class);
            startActivity(i);
        }
    };

}

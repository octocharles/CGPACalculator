package com.pendragon.xavier.cgpacalculator;

import android.app.Dialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
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


public class RecoveryActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText recoveryAnswer;
    private TextView recoveryQuestion, recoveryUsername, recoveryPassword;
    private Button confirmButton;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recovery);

        recoveryAnswer = (EditText) findViewById(R.id.recoveryAnswer);
        recoveryQuestion = (TextView) findViewById(R.id.recoveryQuestionTextView);
        recoveryUsername = (TextView) findViewById(R.id.usernameText);
        recoveryPassword = (TextView) findViewById(R.id.passwordText);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(confirmButtonListener);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_recovery, menu);
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

    View.OnClickListener confirmButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (recoveryAnswer.getText().toString().equals(cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_RECOVERY_ANSWER)))){
                recoveryUsername.setText("Username is: " + cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_USERNAME)));
                recoveryPassword.setText("Password is: " + cursor.getString(cursor.getColumnIndex(MyContentProvider.COLUMN_PASSWORD)));

                Toast.makeText(getApplicationContext(), "Please grab your Username and Password before this screen closes", Toast.LENGTH_LONG).show();

                Thread timer = new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        try {
                            sleep(2500);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        finally {
                            startActivity(new Intent(getApplicationContext(), UserHome.class));
                        }
                    }
                };

                timer.start();

            }
            else {
                DialogFragment errorSigningIn = new DialogFragment() {
                    @Override
                    public Dialog onCreateDialog(Bundle savedInstanceState) {
                        //declare the AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage("The Recovery Answer Entered is Incorrect");
                        builder.setPositiveButton("OK", null);
                        return builder.create();
                    }
                };
                errorSigningIn.show(getSupportFragmentManager(), "Error Retrieving Info");
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), MyContentProvider.USER_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cursor = data;

        recoveryQuestion.setText(data.getString(data.getColumnIndex(MyContentProvider.COLUMN_RECOVERY_QUESTION)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

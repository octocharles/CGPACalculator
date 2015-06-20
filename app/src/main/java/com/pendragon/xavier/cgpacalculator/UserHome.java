package com.pendragon.xavier.cgpacalculator;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;


public class UserHome extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    TextView cgpaText, totalGradePointText, totalUnitText;
    DecimalFormat decimalFormat = new DecimalFormat("#.##");
    double cgpaValue;
    int totalGradePointValue, totalUnitValue;
    String format_cgpa, format_totalGradePoint, format_totalUnit;
    Button calcCGPA, calcGPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        cgpaText = (TextView) findViewById(R.id.cgpaText);
        totalGradePointText = (TextView) findViewById(R.id.totalGradePoint);
        totalUnitText = (TextView) findViewById(R.id.totalUnit);
        calcCGPA = (Button) findViewById(R.id.calculateCgpaButton);
        calcGPA = (Button) findViewById(R.id.calculateGpaButton);
        calcGPA.setOnClickListener(calcGPAListener);
        calcCGPA.setOnClickListener(calcCGPAListener);

        getLoaderManager().initLoader(0, null, this);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clear_records) {

            ContentValues cgpaValues = new ContentValues();
            cgpaValues.put(MyContentProvider.COLUMN_CGPA, 0.00);
            cgpaValues.put(MyContentProvider.COLUMN_TOTAL_POINT, 0);
            cgpaValues.put(MyContentProvider.COLUMN_TOTAL_UNIT, 0);

            getApplicationContext().getContentResolver().update(MyContentProvider.USER_CONTENT_URI, cgpaValues, null, null);

            return true;
        }

        else if (id == R.id.action_records){
            //startActivity(new Intent(getApplicationContext(), RecordActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(getApplicationContext(), MyContentProvider.USER_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        cgpaValue = (data.getDouble(data.getColumnIndex(MyContentProvider.COLUMN_CGPA)));
        totalGradePointValue = (data.getInt(data.getColumnIndex(MyContentProvider.COLUMN_TOTAL_POINT)));
        totalUnitValue = (data.getInt(data.getColumnIndex(MyContentProvider.COLUMN_TOTAL_UNIT)));

        format_cgpa = decimalFormat.format(cgpaValue);
        format_totalGradePoint = decimalFormat.format(totalGradePointValue);
        format_totalUnit = decimalFormat.format(totalUnitValue);

        cgpaText.setText(format_cgpa);
        totalGradePointText.setText(format_totalGradePoint);
        totalUnitText.setText(format_totalUnit);

        if (cgpaValue >= 4.5){
            //first class
            cgpaText.setBackgroundResource(R.drawable.cgpa_first_class_background);
        }
        else if (cgpaValue >= 3.5){
            //second class upper division
            cgpaText.setBackgroundResource(R.drawable.cgpa_2_1_background);
        }
        else if (cgpaValue >= 2.5){
            //second class lower division
            cgpaText.setBackgroundResource(R.drawable.cgpa_2_2_background);
        }
        else if (cgpaValue >= 1.5){
            //third class
            cgpaText.setBackgroundResource(R.drawable.cgpa_third_class_background);
        }
        else {
            //pass
            cgpaText.setBackgroundResource(R.drawable.cgpa_pass_background);
        }

        if (cgpaValue == 5) {
            cgpaText.setText("5.00");
        } else if (cgpaValue == 4) {
            cgpaText.setText("4.00");
        } else if (cgpaValue == 3) {
            cgpaText.setText("3.00");
        } else if (cgpaValue == 2) {
            cgpaText.setText("2.00");
        } else if (cgpaValue == 1) {
            cgpaText.setText("1.00");
        } else if (cgpaValue == 0) {
            cgpaText.setText("0.00");
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    View.OnClickListener calcGPAListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CalculateGpa.class);
            startActivity(intent);
        }
    };

    View.OnClickListener calcCGPAListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), CalculateCGPA.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        //finish();
    }
}

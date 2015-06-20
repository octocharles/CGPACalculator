package com.pendragon.xavier.cgpacalculator;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static android.view.View.VISIBLE;


public class CalculateCGPA extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private double[] grade_values = {-100.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0};
    private int[] session_values = {-100, 11, 12, 21, 22, 31, 32, 41, 42, 51, 52};
    static int sessionValue;

    double cgpaValue, cgpa, totalGradePoint;
    int totalGradePointValue, totalCredit, totalUnitValue;

    private double gpa_grade_0, gpa_grade_1, gpa_grade_2, gpa_grade_3, gpa_grade_4, gpa_grade_5, gpa_grade_6,
            gpa_grade_7, gpa_grade_8, gpa_grade_9, gpa_grade_10, gpa_grade_11;

    private int gpa_credit_0, gpa_credit_1, gpa_credit_2, gpa_credit_3, gpa_credit_4, gpa_credit_5, gpa_credit_6,
            gpa_credit_7, gpa_credit_8, gpa_credit_9, gpa_credit_10, gpa_credit_11;

    private String gpa_course0, gpa_course1, gpa_course2, gpa_course3, gpa_course4, gpa_course5, gpa_course6, gpa_course7, gpa_course8,
            gpa_course9, gpa_course10, gpa_course11;

    private String gpa_grade0, gpa_grade1, gpa_grade2, gpa_grade3, gpa_grade4, gpa_grade5, gpa_grade6, gpa_grade7, gpa_grade8,
            gpa_grade9, gpa_grade10, gpa_grade11;

    private String[] gpaCourse = {gpa_course0, gpa_course1, gpa_course2, gpa_course3, gpa_course4, gpa_course5, gpa_course6, gpa_course7, gpa_course8,
            gpa_course9, gpa_course10, gpa_course11};

    private String[] gpaGrade = {gpa_grade0, gpa_grade1, gpa_grade2, gpa_grade3, gpa_grade4, gpa_grade5, gpa_grade6, gpa_grade7, gpa_grade8,
            gpa_grade9, gpa_grade10, gpa_grade11};

    private int[] gpaUnit = {gpa_credit_0, gpa_credit_1, gpa_credit_2, gpa_credit_3, gpa_credit_4, gpa_credit_5, gpa_credit_6,
            gpa_credit_7, gpa_credit_8, gpa_credit_9, gpa_credit_10, gpa_credit_11};

    EditText credits0, credits1, credits2, credits3, credits4, credits5, credits6, credits7, credits8, credits9, credits10, credits11;
    EditText course0, course1, course2, course3, course4, course5, course6, course7, course8, course9, course10, course11;
    Button gpButton, saveButton;
    TableRow row5, row6, row7, row8, row9, row10, row11;
    Spinner spinner0, spinner1, spinner2, spinner3, spinner4, spinner5, spinner6, spinner7, spinner8, spinner9, spinner10, spinner11;
    Spinner sessionSpinner;
    int addButtonClickCount = 0;
    int deleteButtonClickCount = 0;
    ArrayAdapter<String> each_grade;
    ArrayAdapter<String> session;
    long rowID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_cgpa);

        findAllViewsById();

        getLoaderManager().initLoader(0, null, this);

        saveButton.setEnabled(false);
    }

    private void findAllViewsById() {

        String[] grades = getResources().getStringArray(R.array.grades);
        String[] sessions = getResources().getStringArray(R.array.session);

        credits0 = (EditText) findViewById(R.id.credit0);
        credits1 = (EditText) findViewById(R.id.credit1);
        credits2 = (EditText) findViewById(R.id.credit2);
        credits3 = (EditText) findViewById(R.id.credit3);
        credits4 = (EditText) findViewById(R.id.credit4);
        credits5 = (EditText) findViewById(R.id.credit5);
        credits6 = (EditText) findViewById(R.id.credit6);
        credits7 = (EditText) findViewById(R.id.credit7);
        credits8 = (EditText) findViewById(R.id.credit8);
        credits9 = (EditText) findViewById(R.id.credit9);
        credits10 = (EditText) findViewById(R.id.credit10);
        credits11 = (EditText) findViewById(R.id.credit11);

        course0 = (EditText) findViewById(R.id.course0);
        course1 = (EditText) findViewById(R.id.course1);
        course2 = (EditText) findViewById(R.id.course2);
        course3 = (EditText) findViewById(R.id.course3);
        course4 = (EditText) findViewById(R.id.course4);
        course5 = (EditText) findViewById(R.id.course5);
        course6 = (EditText) findViewById(R.id.course6);
        course7 = (EditText) findViewById(R.id.course7);
        course8 = (EditText) findViewById(R.id.course8);
        course9 = (EditText) findViewById(R.id.course9);
        course10 = (EditText) findViewById(R.id.course10);
        course11 = (EditText) findViewById(R.id.course11);

        gpButton = (Button) findViewById(R.id.calculateButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        gpButton.setOnClickListener(calculateGPA);
        saveButton.setOnClickListener(saveButtonListener);

        row5 = (TableRow) findViewById(R.id.row5);
        row6 = (TableRow) findViewById(R.id.row6);
        row7 = (TableRow) findViewById(R.id.row7);
        row8 = (TableRow) findViewById(R.id.row8);
        row9 = (TableRow) findViewById(R.id.row9);
        row10 = (TableRow) findViewById(R.id.row10);
        row11 = (TableRow) findViewById(R.id.row11);

        spinner0 = (Spinner) findViewById(R.id.spinner0);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner5 = (Spinner) findViewById(R.id.spinner5);
        spinner6 = (Spinner) findViewById(R.id.spinner6);
        spinner7 = (Spinner) findViewById(R.id.spinner7);
        spinner8 = (Spinner) findViewById(R.id.spinner8);
        spinner9 = (Spinner) findViewById(R.id.spinner9);
        spinner10 = (Spinner) findViewById(R.id.spinner10);
        spinner11 = (Spinner) findViewById(R.id.spinner11);
        sessionSpinner = (Spinner) findViewById(R.id.sessionSpinner);

        spinner0.setOnItemSelectedListener(spinner0Listener);
        spinner1.setOnItemSelectedListener(spinner1Listener);
        spinner2.setOnItemSelectedListener(spinner2Listener);
        spinner3.setOnItemSelectedListener(spinner3Listener);
        spinner4.setOnItemSelectedListener(spinner4Listener);
        spinner5.setOnItemSelectedListener(spinner5Listener);
        spinner6.setOnItemSelectedListener(spinner6Listener);
        spinner7.setOnItemSelectedListener(spinner7Listener);
        spinner8.setOnItemSelectedListener(spinner8Listener);
        spinner9.setOnItemSelectedListener(spinner9Listener);
        spinner10.setOnItemSelectedListener(spinner10Listener);
        spinner11.setOnItemSelectedListener(spinner11Listener);
        sessionSpinner.setOnItemSelectedListener(sessionSpinnerListener);

        each_grade = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, grades);
        session = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sessions);

        each_grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        session.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner0.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner1.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner2.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner3.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner4.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner5.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner6.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner7.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner8.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner9.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner10.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        spinner11.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
        sessionSpinner.setAdapter(new NothingSelectedSpinnerAdapter(session, R.layout.contact_spinner_row_nothing_selected, this));

    }

    AdapterView.OnItemSelectedListener spinner0Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_0 = grade_values[position];

            switch ((int) gpa_grade_0){
                case 5:
                    gpa_grade0 = "A";
                    break;
                case 4:
                    gpa_grade0 = "B";
                    break;
                case 3:
                    gpa_grade0 = "C";
                    break;
                case 2:
                    gpa_grade0 = "D";
                    break;
                case 1:
                    gpa_grade0 = "E";
                    break;
                case 0:
                    gpa_grade0 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner1Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_1 = grade_values[position];

            switch ((int) gpa_grade_1){
                case 5:
                    gpa_grade1 = "A";
                    break;
                case 4:
                    gpa_grade1 = "B";
                    break;
                case 3:
                    gpa_grade1 = "C";
                    break;
                case 2:
                    gpa_grade1 = "D";
                    break;
                case 1:
                    gpa_grade1 = "E";
                    break;
                case 0:
                    gpa_grade1 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner2Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_2 = grade_values[position];

            switch ((int) gpa_grade_2){
                case 5:
                    gpa_grade2 = "A";
                    break;
                case 4:
                    gpa_grade2 = "B";
                    break;
                case 3:
                    gpa_grade2 = "C";
                    break;
                case 2:
                    gpa_grade2 = "D";
                    break;
                case 1:
                    gpa_grade2 = "E";
                    break;
                case 0:
                    gpa_grade2 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner3Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_3 = grade_values[position];

            switch ((int) gpa_grade_3){
                case 5:
                    gpa_grade3 = "A";
                    break;
                case 4:
                    gpa_grade3 = "B";
                    break;
                case 3:
                    gpa_grade3 = "C";
                    break;
                case 2:
                    gpa_grade3 = "D";
                    break;
                case 1:
                    gpa_grade3 = "E";
                    break;
                case 0:
                    gpa_grade3 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner4Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_4 = grade_values[position];

            switch ((int) gpa_grade_4){
                case 5:
                    gpa_grade4 = "A";
                    break;
                case 4:
                    gpa_grade4 = "B";
                    break;
                case 3:
                    gpa_grade4 = "C";
                    break;
                case 2:
                    gpa_grade4 = "D";
                    break;
                case 1:
                    gpa_grade4 = "E";
                    break;
                case 0:
                    gpa_grade4 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner5Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_5 = grade_values[position];

            switch ((int) gpa_grade_5){
                case 5:
                    gpa_grade5 = "A";
                    break;
                case 4:
                    gpa_grade5 = "B";
                    break;
                case 3:
                    gpa_grade5 = "C";
                    break;
                case 2:
                    gpa_grade5 = "D";
                    break;
                case 1:
                    gpa_grade5 = "E";
                    break;
                case 0:
                    gpa_grade5 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner6Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_6 = grade_values[position];

            switch ((int) gpa_grade_6){
                case 5:
                    gpa_grade6 = "A";
                    break;
                case 4:
                    gpa_grade6 = "B";
                    break;
                case 3:
                    gpa_grade6 = "C";
                    break;
                case 2:
                    gpa_grade6 = "D";
                    break;
                case 1:
                    gpa_grade6 = "E";
                    break;
                case 0:
                    gpa_grade6 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner7Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_7 = grade_values[position];

            switch ((int) gpa_grade_7){
                case 5:
                    gpa_grade7 = "A";
                    break;
                case 4:
                    gpa_grade7 = "B";
                    break;
                case 3:
                    gpa_grade7 = "C";
                    break;
                case 2:
                    gpa_grade7 = "D";
                    break;
                case 1:
                    gpa_grade7 = "E";
                    break;
                case 0:
                    gpa_grade7 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner8Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_8 = grade_values[position];

            switch ((int) gpa_grade_8){
                case 5:
                    gpa_grade8 = "A";
                    break;
                case 4:
                    gpa_grade8 = "B";
                    break;
                case 3:
                    gpa_grade8 = "C";
                    break;
                case 2:
                    gpa_grade8 = "D";
                    break;
                case 1:
                    gpa_grade8 = "E";
                    break;
                case 0:
                    gpa_grade8 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner9Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_9 = grade_values[position];

            switch ((int) gpa_grade_9){
                case 5:
                    gpa_grade9 = "A";
                    break;
                case 4:
                    gpa_grade9 = "B";
                    break;
                case 3:
                    gpa_grade9 = "C";
                    break;
                case 2:
                    gpa_grade9 = "D";
                    break;
                case 1:
                    gpa_grade9 = "E";
                    break;
                case 0:
                    gpa_grade9 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner10Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_10 = grade_values[position];

            switch ((int) gpa_grade_10){
                case 5:
                    gpa_grade10 = "A";
                    break;
                case 4:
                    gpa_grade10 = "B";
                    break;
                case 3:
                    gpa_grade10 = "C";
                    break;
                case 2:
                    gpa_grade10 = "D";
                    break;
                case 1:
                    gpa_grade10 = "E";
                    break;
                case 0:
                    gpa_grade10 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    AdapterView.OnItemSelectedListener spinner11Listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            gpa_grade_11 = grade_values[position];

            switch ((int) gpa_grade_11){
                case 5:
                    gpa_grade11 = "A";
                    break;
                case 4:
                    gpa_grade11 = "B";
                    break;
                case 3:
                    gpa_grade11 = "C";
                    break;
                case 2:
                    gpa_grade11 = "D";
                    break;
                case 1:
                    gpa_grade11 = "E";
                    break;
                case 0:
                    gpa_grade11 = "F";
                    break;
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    AdapterView.OnItemSelectedListener sessionSpinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sessionValue = session_values[position];
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_calculate_cgpa, menu);
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

        switch (id) {

            case R.id.action_add:
                addCourse();
                return true;

            case R.id.action_delete:
                deleteCourse();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void addCourse() {

        switch (++addButtonClickCount) {
            case 1:
                row5.setVisibility(VISIBLE);
                row5.requestFocus();
                credits5.setText("1");
                course5.setText("");
                break;
            case 2:
                row6.setVisibility(VISIBLE);
                row6.requestFocus();
                credits6.setText("1");
                course6.setText("");
//                spinner6.setAdapter(new NothingSelectedSpinnerAdapter(each_grade, R.layout.contact_spinner_row_nothing_selected, this));
//                spinner6.setOnItemSelectedListener(itemSelect0);
                break;
            case 3:
                row7.setVisibility(VISIBLE);
                row7.requestFocus();
                credits7.setText("1");
                course7.setText("");
                break;
            case 4:
                row8.setVisibility(VISIBLE);
                row8.requestFocus();
                credits8.setText("1");
                course8.setText("");
                break;
            case 5:
                row9.setVisibility(VISIBLE);
                row9.requestFocus();
                credits9.setText("1");
                course9.setText("");
                break;
            case 6:
                row10.setVisibility(VISIBLE);
                row10.requestFocus();
                credits10.setText("1");
                course10.setText("");
                break;
            case 7:
                row11.setVisibility(VISIBLE);
                row11.requestFocus();
                credits11.setText("1");
                course11.setText("");
                break;
            default:
                Toast.makeText(this, "Maximum Number of Courses added", Toast.LENGTH_SHORT).show();
        }
        deleteButtonClickCount = addButtonClickCount + 1;
    }

    private void deleteCourse() {

        switch (--deleteButtonClickCount) {
            case 1:
                row5.setVisibility(View.GONE);
                gpa_credit_5 = 0;
                break;
            case 2:
                row6.setVisibility(View.GONE);
                gpa_credit_6 = 0;
                break;
            case 3:
                row7.setVisibility(View.GONE);
                gpa_credit_7 = 0;
                break;
            case 4:
                row8.setVisibility(View.GONE);
                gpa_credit_8 = 0;
                break;
            case 5:
                row9.setVisibility(View.GONE);
                gpa_credit_9 = 0;
                break;
            case 6:
                row10.setVisibility(View.GONE);
                gpa_credit_10 = 0;
                break;
            case 7:
                row11.setVisibility(View.GONE);
                gpa_credit_11 = 0;
                break;

            default:
                Toast.makeText(this, "Maximum Number of Courses deleted", Toast.LENGTH_SHORT).show();

        }

        addButtonClickCount = deleteButtonClickCount - 1;
    }


    View.OnClickListener calculateGPA = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (!credits0.getText().toString().equals("")) {
                    gpa_credit_0 = Integer.parseInt(credits0.getText().toString());
                } else {
                    credits0.setError("Blank");
                    gpa_credit_0 = 0;
                }
                if (!credits1.getText().toString().equals("")) {
                    gpa_credit_1 = Integer.parseInt(credits1.getText().toString());
                } else {
                    credits1.setError("Blank");
                    gpa_credit_1 = 0;
                }
                if (!credits2.getText().toString().equals("")) {
                    gpa_credit_2 = Integer.parseInt(credits2.getText().toString());
                } else {
                    credits2.setError("Blank");
                    gpa_credit_2 = 0;
                }
                if (!credits3.getText().toString().equals("")) {
                    gpa_credit_3 = Integer.parseInt(credits3.getText().toString());
                } else {
                    credits3.setError("Blank");
                    gpa_credit_3 = 0;
                }
                if (!credits4.getText().toString().equals("")) {
                    gpa_credit_4 = Integer.parseInt(credits4.getText().toString());
                } else {
                    credits4.setError("Blank");
                    gpa_credit_4 = 0;
                }

                if (row5.getVisibility() == (VISIBLE)) {
                    if (!credits5.getText().toString().equals("")) {
                        gpa_credit_5 = Integer.parseInt(credits5.getText().toString());
                    } else {
                        gpa_credit_5 = 0;
                        credits5.setError("Blank");
                    }
                } else {
                    gpa_credit_5 = 0;
                }

                if (row6.getVisibility() == (VISIBLE)) {
                    if (!credits6.getText().toString().equals("")) {
                        gpa_credit_6 = Integer.parseInt(credits6.getText().toString());
                    } else {
                        gpa_credit_6 = 0;
                        credits6.setError("Blank");
                    }
                } else {
                    gpa_credit_6 = 0;
                }

                if (row7.getVisibility() == (VISIBLE)) {
                    if (!credits7.getText().toString().equals("")) {
                        gpa_credit_7 = Integer.parseInt(credits7.getText().toString());
                    } else {
                        gpa_credit_7 = 0;
                        credits7.setError("Blank");
                    }
                } else {
                    gpa_credit_7 = 0;
                }

                if (row8.getVisibility() == (VISIBLE)) {
                    if (!credits8.getText().toString().equals("")) {
                        gpa_credit_8 = Integer.parseInt(credits8.getText().toString());
                    } else {
                        gpa_credit_8 = 0;
                        credits8.setError("Blank");
                    }
                } else {
                    gpa_credit_8 = 0;
                }

                if (row9.getVisibility() == (VISIBLE)) {
                    if (!credits9.getText().toString().equals("")) {
                        gpa_credit_9 = Integer.parseInt(credits9.getText().toString());
                    } else {
                        gpa_credit_9 = 0;
                        credits9.setError("Blank");
                    }
                } else {
                    gpa_credit_9 = 0;
                }

                if (row10.getVisibility() == (VISIBLE)) {
                    if (!credits10.getText().toString().equals("")) {
                        gpa_credit_10 = Integer.parseInt(credits10.getText().toString());
                    } else {
                        gpa_credit_10 = 0;
                        credits10.setError("Blank");
                    }
                } else {
                    gpa_credit_10 = 0;
                }

                if (row11.getVisibility() == (VISIBLE)) {
                    if (!credits11.getText().toString().equals("")) {
                        gpa_credit_11 = Integer.parseInt(credits11.getText().toString());
                    } else {
                        gpa_credit_11 = 0;
                        credits11.setError("Blank");
                    }
                } else {
                    gpa_credit_11 = 0;
                }


                if (!course0.getText().toString().equals("")) {
                    gpa_course0 = (course0.getText().toString());
                } else {
                    course0.setError("Blank");
                }
                if (!course1.getText().toString().equals("")) {
                    gpa_course1 = (course1.getText().toString());
                } else {
                    course1.setError("Blank");
                }
                if (!course2.getText().toString().equals("")) {
                    gpa_course2 = (course2.getText().toString());
                } else {
                    course2.setError("Blank");
                }
                if (!course3.getText().toString().equals("")) {
                    gpa_course3 = (course3.getText().toString());
                } else {
                    course3.setError("Blank");
                }
                if (!course4.getText().toString().equals("")) {
                    gpa_course4 = (course4.getText().toString());
                } else {
                    course4.setError("Blank");
                }

                if (row5.getVisibility() == (VISIBLE)) {
                    if (!course5.getText().toString().equals("")) {
                        gpa_course5 = (course5.getText().toString());
                    } else {
                        course5.setError("Blank");
                    }
                } else {
                    gpa_course5 = null;
                }

                if (row6.getVisibility() == (VISIBLE)) {
                    if (!course6.getText().toString().equals("")) {
                        gpa_course6 = (course6.getText().toString());
                    } else {
                        course6.setError("Blank");
                    }
                } else {
                    gpa_course6 = null;
                }

                if (row7.getVisibility() == (VISIBLE)) {
                    if (!course7.getText().toString().equals("")) {
                        gpa_course7 = (course7.getText().toString());
                    } else {
                        course7.setError("Blank");
                    }
                } else {
                    gpa_course7 = null;
                }

                if (row8.getVisibility() == (VISIBLE)) {
                    if (!course8.getText().toString().equals("")) {
                        gpa_course8 = (course8.getText().toString());
                    } else {
                        course8.setError("Blank");
                    }
                } else {
                    gpa_course8 = null;
                }

                if (row9.getVisibility() == (VISIBLE)) {
                    if (!course9.getText().toString().equals("")) {
                        gpa_course9 = (course9.getText().toString());
                    } else {
                        course9.setError("Blank");
                    }
                } else {
                    gpa_course9 = null;
                }

                if (row10.getVisibility() == (VISIBLE)) {
                    if (!course10.getText().toString().equals("")) {
                        gpa_course10 = (course10.getText().toString());
                    } else {
                        course10.setError("Blank");
                    }
                } else {
                    gpa_course10 = null;
                }

                if (row11.getVisibility() == (VISIBLE)) {
                    if (!course11.getText().toString().equals("")) {
                        gpa_course11 = (course11.getText().toString());
                    } else {
                        course11.setError("Blank");
                    }
                } else {
                    gpa_course11 = null;
                }


                int credits_attempted = gpa_credit_0 + gpa_credit_1 + gpa_credit_2 + gpa_credit_3 +
                        gpa_credit_4 + gpa_credit_5 + gpa_credit_6 + gpa_credit_7 + gpa_credit_8 +
                        gpa_credit_9 + gpa_credit_10 + gpa_credit_11;
                double credit_pts_earned = ((gpa_credit_0 * gpa_grade_0) + (gpa_credit_1 * gpa_grade_1) + (gpa_credit_2 * gpa_grade_2) +
                        (gpa_credit_3 * gpa_grade_3) + (gpa_credit_4 * gpa_grade_4) + (gpa_credit_5 * gpa_grade_5) +
                        (gpa_credit_6 * gpa_grade_6) + (gpa_credit_7 * gpa_grade_7) + (gpa_credit_8 * gpa_grade_8) +
                        (gpa_credit_9 * gpa_grade_9) + (gpa_credit_10 * gpa_grade_10) + (gpa_credit_11 * gpa_grade_11));

                if (credit_pts_earned > 0 && sessionValue > 0) {
                    double calc_gpa = credit_pts_earned / credits_attempted;
                    DecimalFormat gpa_format = new DecimalFormat("#.##");
                    String final_gpa = gpa_format.format(calc_gpa);


                    TextView gpaText = (TextView) findViewById(R.id.gpaText);

                    gpaText.setText(final_gpa);

                    totalCredit = credits_attempted + totalUnitValue;
                    totalGradePoint = credit_pts_earned + totalGradePointValue;
                    cgpa = totalGradePoint / totalCredit;

                    if (calc_gpa >= 4.5) {
                        gpaText.setBackgroundResource(R.drawable.cgpa_first_class_background);
                    } else if (calc_gpa >= 3.5) {
                        gpaText.setBackgroundResource(R.drawable.cgpa_2_1_background);
                    } else if (calc_gpa >= 2.5) {
                        gpaText.setBackgroundResource(R.drawable.cgpa_2_2_background);
                    } else if (calc_gpa >= 1.5) {
                        gpaText.setBackgroundResource(R.drawable.cgpa_third_class_background);
                    } else {
                        gpaText.setBackgroundResource(R.drawable.cgpa_pass_background);
                    }

                    if (calc_gpa == 5) {
                        gpaText.setText("5.00");
                    } else if (calc_gpa == 4) {
                        gpaText.setText("4.00");
                    } else if (calc_gpa == 3) {
                        gpaText.setText("3.00");
                    } else if (calc_gpa == 2) {
                        gpaText.setText("2.00");
                    } else if (calc_gpa == 1) {
                        gpaText.setText("1.00");
                    } else if (calc_gpa == 0) {
                        gpaText.setText("0.00");
                    }

                    saveButton.setEnabled(true);


                } else{
                    if (credit_pts_earned <= 0){
                        Toast.makeText(getApplicationContext(), "Grade Field is Blank", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Session Field is Blank", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something happened", Toast.LENGTH_SHORT).show();
            }

        }


    };

    View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ContentValues cgpaValues = new ContentValues();
                    cgpaValues.put(MyContentProvider.COLUMN_CGPA, cgpa);
                    cgpaValues.put(MyContentProvider.COLUMN_TOTAL_POINT, totalGradePoint);
                    cgpaValues.put(MyContentProvider.COLUMN_TOTAL_UNIT, totalCredit);

                    getApplicationContext().getContentResolver().update(MyContentProvider.USER_CONTENT_URI, cgpaValues, null, null);

//            for (int i = 0; i <= gpaCourse.length; i++) {
//                ContentValues recordValues = new ContentValues();
//                recordValues.put(MyContentProvider.RECORD_ROWID, rowID);
//                recordValues.put(MyContentProvider.RECORD_COLUMN_COURSE, gpaCourse[i]);
//                recordValues.put(MyContentProvider.RECORD_COLUMN_UNIT, gpaUnit[i]);
//                recordValues.put(MyContentProvider.RECORD_COLUMN_GRADE, gpaGrade[i]);
//                recordValues.put(MyContentProvider.RECORD_COLUMN_SESSION, sessionValue);
//                getApplicationContext().getContentResolver().update(MyContentProvider.RECORD_CONTENT_URI, recordValues, null, null);
//                rowID++;
//            }
            finish();
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

        cgpaValue = (data.getDouble(data.getColumnIndex(MyContentProvider.COLUMN_CGPA)));
        totalGradePointValue = (data.getInt(data.getColumnIndex(MyContentProvider.COLUMN_TOTAL_POINT)));
        totalUnitValue = (data.getInt(data.getColumnIndex(MyContentProvider.COLUMN_TOTAL_UNIT)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

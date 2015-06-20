package com.pendragon.xavier.cgpacalculator;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class RecordActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    TextView course0, credit0, grade0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        course0 = (TextView) findViewById(R.id.courseText0);
        credit0 = (TextView) findViewById(R.id.creditText0);
        grade0 = (TextView) findViewById(R.id.gradeText0);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record, menu);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getApplicationContext(), MyContentProvider.RECORD_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        course0.setText(data.getString(data.getColumnIndex(MyContentProvider.RECORD_COLUMN_COURSE)));
        credit0.setText(data.getString(data.getColumnIndex(MyContentProvider.RECORD_COLUMN_UNIT)));
        grade0.setText(data.getString(data.getColumnIndex(MyContentProvider.RECORD_COLUMN_GRADE)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

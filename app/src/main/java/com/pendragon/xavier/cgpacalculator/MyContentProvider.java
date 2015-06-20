package com.pendragon.xavier.cgpacalculator;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class MyContentProvider extends ContentProvider {

    //classes provider
    static final String PROVIDER_AUTHORITY_NAME = "com.pendragon.xavier.cgpacalculator";
    static final String URL = "content://" + PROVIDER_AUTHORITY_NAME + "/users";
    static final Uri USER_CONTENT_URI = Uri.parse(URL);
    static final String RECORD_URL = "content://" + PROVIDER_AUTHORITY_NAME + "/records";
    static final Uri RECORD_CONTENT_URI = Uri.parse(RECORD_URL);


    //database specific constant declaration with columns for the database table
    private SQLiteDatabase database;
    static final String DATABASE_NAME = "CGPA_Database";
    static final int DATABASE_VERSION = 1;
    static final String USER_TABLE_NAME = "users";
    static final String COLUMN_ROWID = "_id";
    static final String COLUMN_USERNAME = "username";
    static final String COLUMN_PASSWORD = "password";
    static final String COLUMN_RECOVERY_QUESTION = "recovery_question";
    static final String COLUMN_RECOVERY_ANSWER = "recovery_answer";
    static final String COLUMN_CGPA = "cgpa";
    static final String COLUMN_TOTAL_POINT = "total_grade_point";
    static final String COLUMN_TOTAL_UNIT = "total_unit";


    static final String RECORDS_TABLE_NAME = "records";
    static final String RECORD_ROWID = "_id";
    static final String RECORD_COLUMN_COURSE = "courses";
    static final String RECORD_COLUMN_UNIT = "units";
    static final String RECORD_COLUMN_GRADE = "grade";
    static final String RECORD_COLUMN_SESSION = "session";



    static final String CREATE_DATABASE_USERS_TABLE = "CREATE TABLE " + USER_TABLE_NAME + " (" + COLUMN_ROWID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_USERNAME + " TEXT NOT NULL, " + COLUMN_PASSWORD + " TEXT NOT NULL," +
            COLUMN_RECOVERY_QUESTION + " TEXT NOT NULL," + COLUMN_RECOVERY_ANSWER + " TEXT NOT NULL," + COLUMN_CGPA + " REAL NOT NULL, " +
            COLUMN_TOTAL_POINT + " INTEGER NOT NULL, " + COLUMN_TOTAL_UNIT + " INTEGER NOT NULL);";

    static final String CREATE_DATABASE_RECORD_TABLE = "CREATE TABLE " + RECORDS_TABLE_NAME + " (" + RECORD_ROWID +
            " INTEGER PRIMARY KEY AUTOINCREMENT, " + RECORD_COLUMN_COURSE + " TEXT, " + RECORD_COLUMN_UNIT + " TEXT, " +
            RECORD_COLUMN_GRADE + " TEXT, " + RECORD_COLUMN_SESSION + " INTEGER);";


    public MyContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.

        switch (mURIMatcher.match(uri)){
            case USER_ID:
                return USER_ID_MIME_TYPE;

            case RECORD_ID:
                return RECORD_ID_MIME_TYPE;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO: Implement this to handle requests to insert a new row.

        long id;
        switch (mURIMatcher.match(uri)){
            case USER_ID:
                values.remove(MyContentProvider.COLUMN_ROWID);
                id = database.insertOrThrow(MyContentProvider.USER_TABLE_NAME, null, values);
                break;

            case RECORD_ID:
                values.remove(MyContentProvider.RECORD_ROWID);
                id = database.insertOrThrow(MyContentProvider.RECORDS_TABLE_NAME, null, values);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public boolean onCreate() {
        // TODO: Implement this to initialize your content provider on startup.
        database = new DatabaseHelper(getContext()).getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        // TODO: Implement this to handle query requests from clients.

        //using the UriMatcher to see the query type and format the db query accordingly
        Cursor cursor;
        switch (mURIMatcher.match(uri)) {
            case USER_ID:
                cursor = database.query(MyContentProvider.USER_TABLE_NAME, null, null, null, null, null, null);
                if (cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                }
                break;

            case RECORD_ID:
                cursor = database.query(MyContentProvider.RECORDS_TABLE_NAME,
                        null, MyContentProvider.RECORD_COLUMN_SESSION + "= 1", null, null, null, null);
                if (cursor != null && cursor.getCount() > 0){
                    cursor.moveToFirst();
                }
                break;

            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.

        switch (mURIMatcher.match(uri)){
            case USER_ID:
                int count = database.update(MyContentProvider.USER_TABLE_NAME, values, MyContentProvider.COLUMN_ROWID +
                        " = 1", null);
                if (count > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count;

            case RECORD_ID:
                int count1 = database.update(MyContentProvider.RECORDS_TABLE_NAME, values, MyContentProvider.RECORD_ROWID +
                        " = " + CalculateCGPA.sessionValue, null);
                if (count1 > 0){
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return count1;

            default:
                throw new UnsupportedOperationException("Not yet implemented");
        }
    }

    private class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DATABASE_USERS_TABLE);
            db.execSQL(CREATE_DATABASE_RECORD_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    public static final String USER_ID_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.com.pendragon.xavier.cgpacalculator.users";
    public static final String RECORD_ID_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.com.pendragon.xavier.cgpacalculator.records";

    //Uri matcher stuff
    private static final int USER_ID = 1;
    private static final int RECORD_ID = 2;
    private static final UriMatcher mURIMatcher = buildUriMatcher();


    private static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_AUTHORITY_NAME, "users", USER_ID);
        uriMatcher.addURI(PROVIDER_AUTHORITY_NAME, "records", RECORD_ID);
        return uriMatcher;
    }

}

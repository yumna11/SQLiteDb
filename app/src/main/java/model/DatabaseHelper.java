package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by YumnaAsim on 2/2/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "record.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TAG = "Database";

    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String table1 = "CREATE TABLE "+ DatabaseSchema.PATIENT_TABLE_NAME+
                " ("+ DatabaseSchema.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseSchema.PATIENT_NAME+" TEXT,"
                + DatabaseSchema.PATIENT_AGE+" TEXT);";

        db.execSQL(table1);
        Log.v(TAG,table1);

        String table2 = "CREATE TABLE "+ DatabaseSchema.REPORT_TABLE_NAME+
                " ("+ DatabaseSchema.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseSchema.REPORT_COLLECTOR_NAME+" TEXT,"
                + DatabaseSchema.PATIENT_ID+" INTEGER,"
                + " FOREIGN KEY ("+DatabaseSchema.PATIENT_ID+") REFERENCES "+ DatabaseSchema.PATIENT_TABLE_NAME+" ("+DatabaseSchema.ID+") ON UPDATE CASCADE" //FK to patient table
                + ");";

    /*    String table2 = "CREATE TABLE "+ DatabaseSchema.REPORT_TABLE_NAME+
                " ("+ DatabaseSchema.ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseSchema.REPORT_COLLECTOR_NAME+" TEXT,"
                + DatabaseSchema.PATIENT_ID+" INTEGER NOT NULL CONSTRAINT "+DatabaseSchema.PATIENT_ID+" REFERENCES "+ DatabaseSchema.PATIENT_TABLE_NAME+"("+DatabaseSchema.ID+")"+" ON DELETE CASCADE" //FK to patient table
                + ");";*/
        db.execSQL(table2);

        Log.v(TAG,table2);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

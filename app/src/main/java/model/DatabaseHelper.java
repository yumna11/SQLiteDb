package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by YumnaAsim on 2/2/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "record.db";
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE "+ DatabaseSchema.Patient.PATIENT_TABLE_NAME+
                " ("+ DatabaseSchema.Patient._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + DatabaseSchema.Patient.PATIENT_NAME+" TEXT"
                + DatabaseSchema.Patient.PATIENT_AGE+" TEXT"
                + ");");

        db.execSQL("CREATE TABLE "+ DatabaseSchema.Report.REPORT_TABLE_NAME+
                " ("+ DatabaseSchema.Report._ID+" INTEGER PRIMARY KEY AUTOINCREMENT ,"
                + DatabaseSchema.Report.REPORT_COLLECTOR_NAME+" TEXT"
                + DatabaseSchema.Report.PATIENT_ID+" INTEGER" //FK to patient table
                + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}

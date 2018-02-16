package com.example.yumnaasim.sqlitedb;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import model.DatabaseHelper;
import model.DatabaseSchema;

public class MainActivity extends AppCompatActivity {

    public static final String TAG="MainScreen";
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;
    private static final int REQUEST_CODE = 0x11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        database = databaseHelper.getWritableDatabase();

        handleUserInput();

        String[] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE"};
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE); // without sdk version check


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // save file
            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION_DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void handleUserInput() {

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = (EditText) findViewById(R.id.editText);
                EditText editText2 = (EditText) findViewById(R.id.editText2);
                EditText editText3 = (EditText) findViewById(R.id.editText3);

                String patientName = editText.getText().toString();
                String patAge = editText2.getText().toString();

                String collectorName = editText3.getText().toString();
                insertData(patientName,patAge,collectorName);
            }
        });

        Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayData();
            }
        });

        Button button2 = (Button) findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB();
            }
        });

    }

    private void exportDB() {
            SQLiteDatabase sqldb = databaseHelper.getReadableDatabase(); //My Database class
            Cursor c = null;
        //main code begins here
                try {
                    c = sqldb.rawQuery("select * from "+DatabaseSchema.PATIENT_TABLE_NAME, null);
                    int rowcount = 0;
                    int colcount = 0;
                    File sdCardDir = Environment.getExternalStorageDirectory();
                    String filename = "MyBackUp.csv";
                    // the name of the file to export with
                    File saveFile = new File(sdCardDir, filename);
                    FileWriter fw = new FileWriter(saveFile);

                    BufferedWriter bw = new BufferedWriter(fw);
                    rowcount = c.getCount();
                    colcount = c.getColumnCount();
                    if (rowcount > 0) {
                        c.moveToFirst();

                        for (int i = 0; i < colcount; i++) {
                            if (i != colcount - 1) {

                                bw.write(c.getColumnName(i) + ",");

                            } else {

                                bw.write(c.getColumnName(i));

                            }
                        }
                        bw.newLine();

                        for (int i = 0; i < rowcount; i++) {
                            c.moveToPosition(i);

                            for (int j = 0; j < colcount; j++) {
                                if (j != colcount - 1)
                                    bw.write(c.getString(j) + ",");
                                else
                                    bw.write(c.getString(j));
                            }
                            bw.newLine();
                        }
                        bw.flush();
                        Log.i(TAG,"Exported Successfully.");
                    }
                } catch (Exception ex) {
                    if (sqldb.isOpen()) {
                        sqldb.close();
                        Log.i(TAG,"Error: "+ex.getMessage().toString());
                    }

                } finally {

                }

            }

    private void displayData() {
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor =  database.query(DatabaseSchema.REPORT_TABLE_NAME,null,null,null,null,null,null);

        Log.i(TAG, "**** Cursor Begins *** "+ " Results: "+ cursor.getCount()+" Columns: "+cursor.getColumnCount());

        //print columns
        String rowHeaders = "|| ";
        for (int i=0;i<cursor.getColumnCount();i++)
        {
            rowHeaders = rowHeaders.concat(cursor.getColumnName(i));
        }

        Log.i(TAG,"Columns "+rowHeaders);

        //print records
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            String rowResults = "|| ";
            for (int i=0;i<cursor.getColumnCount();i++)
            {
                rowResults = rowResults.concat(cursor.getString(i)+ " || ");
            }
            Log.i(TAG,"Row "+cursor.getPosition()+": "+rowResults);
            cursor.moveToNext();
        }
        Log.i(TAG," **** Cursor End **** ");


    }

    private void insertData(String patientName, String patientAge, String collectorName) {


        /*final String CREATE_TRIGGER_ADD = "CREATE TRIGGER fk_insert_patient"
                +" BEFORE INSERT ON "+ DatabaseSchema.REPORT_TABLE_NAME
                +" FOR EACH ROW"
                +" BEGIN"
                +" SELECT RAISE(ROLLBACK,'insert on table \"Report\" violates foreign key constraint \"fk_patientid\"') WHERE (SELECT "+ DatabaseSchema.ID+" FROM "+ DatabaseSchema.PATIENT_TABLE_NAME+" WHERE "+ DatabaseSchema.ID+" = NEW."+DatabaseSchema.PATIENT_ID+") IS NULL; END;";

        database.execSQL(CREATE_TRIGGER_ADD);
        Log.v(TAG,CREATE_TRIGGER_ADD);*/

       ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseSchema.PATIENT_AGE,patientAge);
        contentValues.put(DatabaseSchema.PATIENT_NAME,patientName);

        long patientID = database.insert(DatabaseSchema.PATIENT_TABLE_NAME,null,contentValues);



        ContentValues contentValues1 = new ContentValues();
        contentValues1.put(DatabaseSchema.REPORT_COLLECTOR_NAME,collectorName);
        contentValues1.put(DatabaseSchema.PATIENT_ID,patientID);

        long result1 = database.insert(DatabaseSchema.REPORT_TABLE_NAME,null,contentValues1);

        Log.v(TAG," Insertion 1: "+patientID);
        Log.v(TAG,"Insertion 2: "+result1);
    }


}

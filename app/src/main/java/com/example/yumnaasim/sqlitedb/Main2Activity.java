package com.example.yumnaasim.sqlitedb;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int READ_SMS_PERMISSION_REQUEST = 1;
    ArrayList<String> smsMessageList = new ArrayList<>();
    ListView messages;
    ArrayAdapter arrayAdapter;
    private static final String TAG = Main2Activity.class.getSimpleName();
    String yesterdayDate;
    double value;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Calendar calendar = Calendar.getInstance();
        value = 0;
        calendar.add(Calendar.DATE,-1);
        Date date = calendar.getTime();
        String[] yesterdayDateParts = date.toString().split(" ");
        for (int i=0;i<yesterdayDateParts.length;i++)
            Log.v(TAG,"Value at "+i+" is "+yesterdayDateParts[i]);
        Log.v(TAG,"Yesterday date is : "+date.toString());

        yesterdayDate = yesterdayDateParts[2]+yesterdayDateParts[1];

        Log.v(TAG,"Yesterday date by logic "+yesterdayDate);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        messages = (ListView) findViewById(R.id.list);
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);
        messages.setAdapter(arrayAdapter);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                getPermissionToReadSMS();
            }
        }
        else {
            refreshSmsInbox();
        }

        //refreshSmsInbox();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_SMS)) {
                Toast.makeText(this, "Please Allow permission!", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, READ_SMS_PERMISSION_REQUEST);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == READ_SMS_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Read SMS permission granted", Toast.LENGTH_SHORT).show();
                // refreshSmsInbox();
            } else {
                Toast.makeText(this, "Read SMS permission denied", Toast.LENGTH_SHORT).show();
            }
        } else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void refreshSmsInbox() {
        ContentResolver contentResolver = getContentResolver();
        Cursor smsInboxcursor = contentResolver.query(Uri.parse("content://sms/inbox"), null, null, null, null);
        int indexBody = smsInboxcursor.getColumnIndex("body");
        int indexAddress = smsInboxcursor.getColumnIndex("address");
        String i = "+923362503565";
        Log.v("hel" ,"msg" +i);
        //    String check = "00923362503565";

        if (indexBody < 0 || !smsInboxcursor.moveToFirst())
            return;
        arrayAdapter.clear();
        //  if(i.equals(indexAddress)) {
        do {
            String number = smsInboxcursor.getString(indexAddress);
            if (number.equals(i)) {

                int dateIndex = smsInboxcursor.getColumnIndex("date");
                Log.v("dateIndex",""+dateIndex);

                String date =  smsInboxcursor.getString(smsInboxcursor.getColumnIndex("date"));
                Long timestamp = Long.parseLong(date);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(timestamp);

                Date finaldate = calendar.getTime();
                String smsDate = finaldate.toString();

                Log.v(TAG,"SMS date is "+smsDate);


                String []  datte= smsDate.split(" ");
                Log.v("Day",""+datte[0]);
                Log.v("Month",""+datte[1]);
                Log.v("Date",""+datte[2]);
                Log.v("Time",""+datte[3]);

                String da = datte[2].concat(datte[1]);

                if (da.equals(yesterdayDate))
                {
                    String msg = smsInboxcursor.getString(indexBody);
                    Log.v(TAG,"Yesterday msg: "+msg);
                    String[] msgParts = msg.split(": ");
                    Log.v(TAG, "Energy value of yesterday msg: "+msgParts[1]);
                    double energyValue = Double.parseDouble(msgParts[1].toString());
                    value = value+energyValue;
                }
                Log.v(TAG,""+smsDate);
                String str = "SMS From: " + smsInboxcursor.getString(indexAddress) +
                        "\n" + smsInboxcursor.getString(indexBody) + "\n" + "date: " +da +"\n"+"Time: "+datte[3]+"\n" ;

                arrayAdapter.add(str);

            }   }
        while (smsInboxcursor.moveToNext());
        Log.v(TAG,"Sum of energy is "+Math.round(value));
        //   }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

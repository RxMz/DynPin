package com.todaysfuture.dynpin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.app.admin.DevicePolicyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity{

    int k=0;
    Calendar c = Calendar.getInstance();
    int hour=c.get(Calendar.HOUR_OF_DAY);
    int minute=c.get(Calendar.MINUTE);
    Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button refresh=(Button)findViewById(R.id.btn1);
        displayer();
         refresh.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 displayer();
                 Intent intent=new Intent(MainActivity.this,DPM.class);
                 startActivity(intent);
             }
         });
    }



    private void displayer() {
        TextView Tv=(TextView)findViewById(R.id.TV);
        TextView Tv2=(TextView)findViewById(R.id.Tv2);
        String date = sdf.format(calendar.getTime());
        Tv.setText("The current time is "+date);
        String str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
        Tv2.setText("So the password will be " + str);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflat the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

}

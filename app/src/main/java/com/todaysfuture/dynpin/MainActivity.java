package com.todaysfuture.dynpin;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private final static String LOG_TAG="DevicePolicyAdmin";
    public static DevicePolicyManager minochaDevicePolicyManager;
    public static ComponentName minochaDevicePolicyAdmin;
    private CheckBox minochaAdminEnabled;
    protected static final int REQUEST_ENABLE=1;
    protected static final int SET_PASSWORD=2;
    Calendar c = Calendar.getInstance();
    int hour=c.get(Calendar.HOUR_OF_DAY);
    int minute=c.get(Calendar.MINUTE);
    Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
    String date = sdf.format(calendar.getTime());
    String str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private TextView TimeText;
    private TextView PinText;
    private Button RefreshButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button activater=(Button)findViewById(R.id.btnActivate);
        activater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this,MyABService.class);
                PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent2, 0);
                AlarmManager alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm_manager.setRepeating(AlarmManager.RTC, c.getTimeInMillis(), 60*1000,  pintent);
            }
        });
        Button deactivator=(Button)findViewById(R.id.btnDeactivate);
        deactivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,
                        "Deactivated the changer! The pin is now 9999.", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(MainActivity.this,MyABService.class);
                PendingIntent pintent = PendingIntent.getService(MainActivity.this, 0, intent2, 0);
                AlarmManager alarm_manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarm_manager.cancel(pintent);
                minochaDevicePolicyManager.resetPassword("9999",0);
            }
        });

        minochaDevicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        minochaDevicePolicyAdmin=new ComponentName(this,MyDevicePolicyReceiver.class);
        minochaAdminEnabled=(CheckBox)findViewById(R.id.checkBox1);
        RefreshButton=(Button)findViewById(R.id.btn1);
        PinText=(TextView)findViewById(R.id.tvPIN);
        TimeText=(TextView)findViewById(R.id.tvTIME);
        displayer();
         RefreshButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 displayer();
             }
         });

    }
    public void displayer() {
        Calendar c = Calendar.getInstance();
        hour=c.get(Calendar.HOUR_OF_DAY);
        minute=c.get(Calendar.MINUTE);
        Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        date = sdf.format(calendar.getTime());
        TimeText.setText("The current time is "+date);
        str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
        PinText.setText("So the password will be " + str);
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(isMyDevicePolicyRecieverActive()){
            minochaAdminEnabled.setChecked(true);
        } else{
            minochaAdminEnabled.setChecked(false);
        }
        minochaAdminEnabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, minochaDevicePolicyAdmin);
                    startActivityForResult(intent, REQUEST_ENABLE);

                } else {
                    minochaDevicePolicyManager.removeActiveAdmin(minochaDevicePolicyAdmin);

                }
            }
        });
    }
      //Here is where the password is set
    protected void onActivityResult(int requestCode,int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_ENABLE:
                    Log.v(LOG_TAG, "Enabling policies");
                    break;
            }
        }
    }
    private boolean isMyDevicePolicyRecieverActive(){
        return minochaDevicePolicyManager.isAdminActive(minochaDevicePolicyAdmin);
    }


    //Another class starts here ------------------------------------------------------------
//0922

    public static class MyDevicePolicyReceiver extends DeviceAdminReceiver {
        @Override
        public void onDisabled(Context context,Intent intent){
            Toast.makeText(context, "DynPin is not an admin anymore", Toast.LENGTH_SHORT).show();
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
            return disableRequestedSeq;
        }

        public void onPasswordChanged(Context context, Intent intent) {
            Toast.makeText(context, "Device password is now changed",
                    Toast.LENGTH_LONG).show();
            DevicePolicyManager localDPM = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            ComponentName localComponent = new ComponentName(context,
                    MyDevicePolicyReceiver.class);
            localDPM.setPasswordExpirationTimeout(localComponent,0);
        }
        public void onPasswordFailed(Context context,Intent intent){
            Toast.makeText(context,"Password Failed",Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPasswordSucceeded(Context context, Intent intent) {
            Toast.makeText(context, "Access Granted", Toast.LENGTH_SHORT)
                    .show();
        }

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

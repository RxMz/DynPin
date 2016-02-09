package com.todaysfuture.dynpin;

import android.app.admin.DeviceAdminReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.admin.DevicePolicyManager;
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
    DevicePolicyManager minochaDevicePolicyManager;
    ComponentName minochaDevicePolicyAdmin;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        minochaDevicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        minochaDevicePolicyAdmin=new ComponentName(this,MyDevicePolicyReceiver.class);
        minochaAdminEnabled=(CheckBox)findViewById(R.id.checkBox1);
        final Button refresh=(Button)findViewById(R.id.btn1);
        displayer();
         refresh.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 TextView Tv=(TextView)findViewById(R.id.TV);
                 TextView Tv2=(TextView)findViewById(R.id.Tv2);
                 String date = sdf.format(calendar.getTime());
                 Tv.setText("The current time is "+date);
                 String str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
                 Tv2.setText("So the password will be " + str);
             }
         });
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
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            switch (requestCode){
                case REQUEST_ENABLE:
                    Log.v(LOG_TAG, "Enabling policies...");
                    minochaDevicePolicyManager.resetPassword(str,0);
                    break;
            }
        }
    }
    private boolean isMyDevicePolicyRecieverActive(){
        return  minochaDevicePolicyManager.isAdminActive(minochaDevicePolicyAdmin);
    }
    public static class MyDevicePolicyReceiver extends DeviceAdminReceiver{
        @Override
        public void onDisabled(Context context,Intent intent){
            Toast.makeText(context,"DynPin is now an admin",Toast.LENGTH_SHORT).show();
        }

        @Override
        public CharSequence onDisableRequested(Context context, Intent intent) {
            CharSequence disableRequestedSeq = "Requesting to disable Device Admin";
            return disableRequestedSeq;
        }

        public void onPasswordChanged(Context context, Intent intent) {
            Toast.makeText(context, "Device password is now changed",
                    Toast.LENGTH_SHORT).show();
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

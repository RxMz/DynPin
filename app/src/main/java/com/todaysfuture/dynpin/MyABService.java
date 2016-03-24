package com.todaysfuture.dynpin;

import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by rishabh on 22/3/16.
 */
public class MyABService extends Service {

    public static DevicePolicyManager minochaDevicePolicyManager;
    public static ComponentName minochaDevicePolicyAdmin;
    private static final String TAG = "MyService";

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();


        minochaDevicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        minochaDevicePolicyAdmin=new ComponentName(this,MainActivity.MyDevicePolicyReceiver.class);


        Log.d("Testing", "Service got created");
        Toast.makeText(this, "gps_back_process,onCreate();", Toast.LENGTH_LONG).show();

        Calendar c = Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(calendar.getTime());
        String str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
        String LOG_TAG="DevicePolicyAdmin";
        Log.v(LOG_TAG, "Service Started");
        minochaDevicePolicyManager.resetPassword(str, 0);
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent arg0) {

        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }


    @Override
    public void onStart(Intent intent, int startid)
    {


        minochaDevicePolicyManager=(DevicePolicyManager)getSystemService(Context.DEVICE_POLICY_SERVICE);
        minochaDevicePolicyAdmin=new ComponentName(this,MainActivity.MyDevicePolicyReceiver.class);



        Log.d("Testing", "Service got created");

        Calendar c = Calendar.getInstance();
        int hour=c.get(Calendar.HOUR_OF_DAY);
        int minute=c.get(Calendar.MINUTE);
        Calendar calendar = new GregorianCalendar(1990, 1, 1, hour, minute);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
        String date = sdf.format(calendar.getTime());
        String str=date.charAt(0)+""+date.charAt(1)+""+date.charAt(3)+""+date.charAt(4);
        String LOG_TAG="DevicePolicyAdmin";
        Log.v(LOG_TAG, "Service Started");
        minochaDevicePolicyManager.resetPassword(str, 0);
        stopSelf();
    }
}


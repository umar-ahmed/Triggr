package com.example.umar.trggr;

/**
 * Created by Umar on 2/6/2016.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class Background_service extends Service implements SensorEventListener
{
    boolean flag=false;
    private long lastUpdate;
    SensorManager sensorManager;
    int count=0;
    final static int cameraData = 0;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    private Uri fileUri;
    ImageView iv;
    Intent I;

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }
    public void onCreate()
    {
        flag=true;
        //Log.d(MainShake.TAG, "onCreate");
        super.onCreate();
    }

    public void onDestroy()
    {
        flag=false;
        //Log.d(MainShake.TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

        return START_STICKY;
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    private void getAccelerometer(SensorEvent event)
    {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float[] values = event.values;
            // Movement
            float x = values[0];
            float y = values[1];
            float z = values[2];

            float accelationSquareRoot = (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
            long actualTime = System.currentTimeMillis();
            if (accelationSquareRoot >= 2.5)
            {
                if (actualTime - lastUpdate < 2000)
                {
                    count++;
                    return;
                }
                //   Context context = getApplicationContext();
                //   CharSequence text = "Please fill all the field...!";

                Intent notificationIntent = new Intent(this, MainActivity.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(notificationIntent);

                /*try {
                    Thread.sleep(1000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                } */

            }
        }
    }

    public void onSensorChanged(SensorEvent event)
    {
        getAccelerometer(event);
    }
    protected void onResume()
    {
        // register this class as a listener for the orientation and
        // accelerometer sensors
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause()
    {
        // unregister listener
        sensorManager.unregisterListener(this);
    }
}
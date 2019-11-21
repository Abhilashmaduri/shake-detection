package com.example.shakedetectionandchangingimages;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ShakeDetection implements SensorEventListener {



    interface OnShakeListener{
        void onShake(int count);
    }

    private static final float SHAKE_THRESHOLD_GRAVITY=2.7F;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener onShakeListener;
    private long mShakeTimestamp;
    private int mShakeCount;

    public void setOnShakeListener(OnShakeListener onShakeListener1)
    {
        onShakeListener=onShakeListener1;
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
     if (onShakeListener!=null)
     {
         float x=event.values[0];
         float y=event.values[1];
         float z=event.values[2];

         float gX = x / SensorManager.GRAVITY_EARTH;
         float gY = y / SensorManager.GRAVITY_EARTH;
         float gZ = z / SensorManager.GRAVITY_EARTH;

         float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

         if (gForce>SHAKE_THRESHOLD_GRAVITY)
         {
             final long now=System.currentTimeMillis();
             if (mShakeTimestamp+SHAKE_SLOP_TIME_MS>now)
             {
                 return;
             }
             if (mShakeTimestamp+SHAKE_COUNT_RESET_TIME_MS<now)
             {
                 mShakeCount=0;
             }
             mShakeTimestamp=now;
             mShakeCount++;
             onShakeListener.onShake(mShakeCount);
         }
     }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}

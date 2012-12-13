package com.game.mrnomgame.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerHandler implements SensorEventListener {
	float accel_x;
	float accel_y;
	float accel_z;
	
	public AccelerometerHandler(Context context) {
		SensorManager manager = (SensorManager) context.getSystemService(
													Context.SENSOR_SERVICE);
		if (manager.getSensorList(Sensor.TYPE_ACCELEROMETER).size() != 0) {
			Sensor accelerometer = manager.getSensorList(
									Sensor.TYPE_ACCELEROMETER).get(0);
			manager.registerListener(this,
									 accelerometer,
									 SensorManager.SENSOR_DELAY_GAME);
		}
	}
	
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Nothing to do here.
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		accel_x = event.values[0];
		accel_y = event.values[1];
		accel_z = event.values[2];
	}
	
	public float getAccelX() {
		return accel_x;
	}
	
	public float getAccelY() {
		return accel_y;
	}
	
	public float getAccelZ() {
		return accel_z;
	}
}
package com.example.unite;


import com.amap.api.location.LocationManagerProxy;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

/**
 *  ��ȡ��ǰ��λ��
 *  ͨ���ķ�����ͨ��GPS��ȡ��ͨ��wifi��ȡ�������߲𿪽��в�������
 **/

public class LocationActivity extends Activity {
	// ��¼һ��Ϊֹ�Ľṹ��
	public static class locationStruct
	{
		// ����
		double latitude;
		// γ��
		double longtitude;
		// ����
		double altitude;
	};
	
	private static String ACTIVITY_TAG = "locatelog";
	
	private Context context = null;
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
       // ����������ذ���
       if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
       {    
    	   // Do you want to do.
    	// ֹͣ��ǰ��activity
    	   finish();
           return true;
        } 
       else // ������������ذ�����������Ӧ  
       {
            return super.onKeyDown(keyCode, event);
       }
    }
	
	/**
	 * �ж�GPS�Ƿ�����GPS����AGPS����һ������Ϊ�ǿ�����
	 * @param context
	 * @return true ��ʾ����
	 */
	public static final boolean isGpsOPen(final Context context) {
		if (context == null)
		{
			Log.e(ACTIVITY_TAG, "the context is null " + context);
			return false;
		}
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// ͨ��GPS���Ƕ�λ����λ������Ծ�ȷ���֣�ͨ��24�����Ƕ�λ��������Ϳտ��ĵط���λ׼ȷ���ٶȿ죩
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// ͨ��WLAN���ƶ�����(3G/2G)ȷ����λ�ã�Ҳ����AGPS������GPS��λ����Ҫ���������ڻ��ڸ������Ⱥ��ï�ܵ����ֵȣ��ܼ��ĵط���λ��
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}
	
	/**
	 * ǿ�ư��û���GPS
	 * @param context
	 */
	public static final void openGPS(Context context) {
		Intent GPSIntent = new Intent();
		GPSIntent.setClassName("com.android.settings",
				"com.android.settings.widget.SettingsAppWidgetProvider");
		GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
		GPSIntent.setData(Uri.parse("custom:3"));
		try {
			PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
		} catch (CanceledException e) {
			e.printStackTrace();
		}
	}
    
    /**
     * ��ȡ���յĶ�λ
     * �������Ϊ Location���Ͳ������������ѡ����������Ϊֹ��ѡ��
     **/
    public locationStruct getLocateByAppoint(Location location)
    {
    	locationStruct locateInfo = new locationStruct();
    	locateInfo.latitude = location.getLatitude();
    	locateInfo.altitude = location.getAltitude();
    	locateInfo.longtitude = location.getLongitude();
    	Log.d(ACTIVITY_TAG, "the string is, " + locateInfo.toString());
    	return locateInfo;
    }
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		TextView locationShow = (TextView)findViewById(R.id.locationview);
		context = this;
		
		// find location show
		locationStruct locateInfo = getLocationInfo();
		locationShow.setText("����λ����Ϣ:\n" + "����: " + locateInfo.longtitude + "\nγ��: " + locateInfo.altitude + "\n����: " + locateInfo.latitude);
	}
    
    /**
     *  ���ݵ�ǰ��״�����������ʺϵ���Ϣ
     * **/
    public locationStruct getLocationInfo()
    {
    	// ���û�д�GPS����ôǿ�ư��û���
    	if (!isGpsOPen(this.context))
    	{
    		openGPS(this.context);
    	}
    	
    	LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
    	Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Location locationNet = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        long GPSLocationTime = 0;
        if (null != locationGPS) { 
        	GPSLocationTime = locationGPS.getTime(); 
        }

        long NetLocationTime = 0;

        if (null != locationNet) {
            NetLocationTime = locationNet.getTime();
        }

        if ( 0 < GPSLocationTime - NetLocationTime ) {
            return getLocateByAppoint(locationGPS);
        }
        else {
            return getLocateByAppoint(locationNet);
        }
    }
}

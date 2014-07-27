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
 *  获取当前的位置
 *  通常的方案有通过GPS获取和通过wifi获取，将两者拆开进行操作即可
 **/

public class LocationActivity extends Activity {
	// 记录一个为止的结构体
	public static class locationStruct
	{
		// 经度
		double latitude;
		// 纬度
		double longtitude;
		// 海拔
		double altitude;
	};
	
	private static String ACTIVITY_TAG = "locatelog";
	
	private Context context = null;
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
       // 如果是物理返回按键
       if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
       {    
    	   // Do you want to do.
    	// 停止当前的activity
    	   finish();
           return true;
        } 
       else // 如果不是物理返回按键则正常响应  
       {
            return super.onKeyDown(keyCode, event);
       }
    }
	
	/**
	 * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
	 * @param context
	 * @return true 表示开启
	 */
	public static final boolean isGpsOPen(final Context context) {
		if (context == null)
		{
			Log.e(ACTIVITY_TAG, "the context is null " + context);
			return false;
		}
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}
	
	/**
	 * 强制帮用户打开GPS
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
     * 获取最终的定位
     * 输入参数为 Location类型参数，根据最佳选择来做地理为止的选择
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
		locationShow.setText("地理位置信息:\n" + "经度: " + locateInfo.longtitude + "\n纬度: " + locateInfo.altitude + "\n海拔: " + locateInfo.latitude);
	}
    
    /**
     *  根据当前的状况来返回最适合的信息
     * **/
    public locationStruct getLocationInfo()
    {
    	// 如果没有打开GPS，那么强制帮用户打开
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

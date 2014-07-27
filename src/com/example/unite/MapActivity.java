package com.example.unite;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import android.app.Activity;
import android.os.Bundle;

/**
 *  map的操作类，目前使用高德地图
 * 
 * **/
public class MapActivity extends Activity {
	private MapView mapView;
	private AMap aMap;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		// R 需要引用包import com.amapv2.apis.R; 
		setContentView(R.layout.activity_map); 
		mapView = (MapView) findViewById(R.id.map); 
		mapView.onCreate(savedInstanceState);
		
		// 必须要写 
		init(); 
		} 
	
		/** * 初始化AMap对象 */ 
		private void init() {
			if (aMap == null) { 
				aMap = mapView.getMap();
			} 
		}
		
		/** 
		 *  方法必须重写 
		 *  */ 
		@Override 
		protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState); 
			mapView.onSaveInstanceState(outState);
		}
		
		/** * 此方法需要有 */ 
		@Override 
		protected void onResume() { 
			super.onResume(); 
			mapView.onResume(); 
		} 
		
		/** * 此方法需要有 */ 
		@Override 
		protected void onPause() { 
			super.onPause(); 
			mapView.onPause(); 
		} 
		
		/** * 此方法需要有 */ 
		@Override 
		protected void onDestroy() { 
			super.onDestroy(); 
			mapView.onDestroy(); 
		}
		
		
}

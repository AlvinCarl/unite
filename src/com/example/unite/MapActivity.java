package com.example.unite;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.MapView;

import android.app.Activity;
import android.os.Bundle;

/**
 *  map�Ĳ����࣬Ŀǰʹ�øߵµ�ͼ
 * 
 * **/
public class MapActivity extends Activity {
	private MapView mapView;
	private AMap aMap;
	
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		
		// R ��Ҫ���ð�import com.amapv2.apis.R; 
		setContentView(R.layout.activity_map); 
		mapView = (MapView) findViewById(R.id.map); 
		mapView.onCreate(savedInstanceState);
		
		// ����Ҫд 
		init(); 
		} 
	
		/** * ��ʼ��AMap���� */ 
		private void init() {
			if (aMap == null) { 
				aMap = mapView.getMap();
			} 
		}
		
		/** 
		 *  ����������д 
		 *  */ 
		@Override 
		protected void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState); 
			mapView.onSaveInstanceState(outState);
		}
		
		/** * �˷�����Ҫ�� */ 
		@Override 
		protected void onResume() { 
			super.onResume(); 
			mapView.onResume(); 
		} 
		
		/** * �˷�����Ҫ�� */ 
		@Override 
		protected void onPause() { 
			super.onPause(); 
			mapView.onPause(); 
		} 
		
		/** * �˷�����Ҫ�� */ 
		@Override 
		protected void onDestroy() { 
			super.onDestroy(); 
			mapView.onDestroy(); 
		}
		
		
}

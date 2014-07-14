package com.example.unite;

import java.io.InputStream;	 
import java.util.ArrayList;	 
import java.util.HashMap;
import java.util.Map;

import com.example.unite.FullscreenActivity;
  
import android.R.integer;
import android.app.ListActivity;  
import android.content.ContentResolver;	 
import android.content.ContentUris;	 
import android.content.Context;	 
import android.content.Intent;	
import android.database.Cursor;	 
import android.graphics.Bitmap;	 
import android.graphics.BitmapFactory;	
import android.net.Uri;	 
import android.os.Bundle;  
import android.provider.ContactsContract;  
import android.provider.ContactsContract.CommonDataKinds.Phone;	 
import android.provider.ContactsContract.CommonDataKinds.Photo;	 
import android.text.TextUtils;	
import android.view.LayoutInflater;	 
import android.view.View;  
import android.view.ViewGroup;	
import android.widget.AdapterView;	
import android.widget.BaseAdapter;	
import android.widget.CheckBox;
import android.widget.ImageView;  
import android.widget.ListView;	 
import android.widget.TextView;	 
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;	
 

/***  基础类，用于获取联系
 *		  1. 机器本地的联系人
 *		  2. 微信的联系人
 *		  3. 待添加
  ***/

public class ContactorActivity extends ListActivity {
	
	Context mContext = null;  
  
	/**获取库Phone表字段**/  
	private static final String[] PHONES_PROJECTION = new String[] {
		Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };  
 
	/**联系人显示名称**/  
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;	 
  
	/**电话号码**/	
	private static final int PHONES_NUMBER_INDEX = 1;  
	 
	/**头像ID**/	
	private static final int PHONES_PHOTO_ID_INDEX = 2;	 
	 
	/**联系人的ID**/  
	private static final int PHONES_CONTACT_ID_INDEX = 3;  
	 
	/**联系人名称**/	 
	private ArrayList<String> mContactsName = new ArrayList<String>();	
  
	/**联系人头像**/	 
	private ArrayList<String> mContactsNumber = new ArrayList<String>();  
  
	/**联系人头像**/	 
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	/** 被选中的联系人信息，存储为联系人名字与号码(微信号码或者电话号码) **/
	private ArrayList<Map> mContactorsList = new ArrayList<Map>();
	
	ListView mListView = null;	
	MyListAdapter myAdapter = null;	 
  
	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		mContext = this;  
		mListView = this.getListView();
		
		/**得到手机通讯录联系人信息**/	
		getPhoneContacts();	 
		
		myAdapter = new MyListAdapter(this);  
		setListAdapter(myAdapter);
		
		// 获取选中的联系人信息，并存储到一个公有的结构体中，存储为key-value格式
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override  
			public void onItemClick(AdapterView<?> adapterView, View view,	
				int position, long id) {
				
				// 记录信息
				Map<String, String> contactInfoMap = new HashMap<String, String>();
				contactInfoMap.put(mContactsName.get(position), mContactsNumber.get(position));
				mContactorsList.add(contactInfoMap);
				Toast.makeText(ContactorActivity.this, "name is " + mContactsName.get(position) + "\t phone number is, " + mContactsNumber.get(position), 
			    		Toast.LENGTH_LONG).show();
			}  
		});	 
	  
		super.onCreate(savedInstanceState);	 
	} 
  
	/** 得到手机通讯录联系人信息 **/ 
	private void getPhoneContacts() {  
		ContentResolver resolver = mContext.getContentResolver();  
  
		// 获取手机联系人	
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);	 
  
		if (phoneCursor != null) {	
			while (phoneCursor.moveToNext()) {	
				//得到手机号码  
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				//当手机号码为空的或者为空字段 跳过当前循环	 
				if (TextUtils.isEmpty(phoneNumber))	 
					continue;  
  
				//得到联系人名称  
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);	  
				//得到联系人ID  
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);	  
				//得到联系人头像ID	 
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);	
				//得到联系人头像Bitamp	 
				Bitmap contactPhoto = null;	 
  
				//photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的	
				if (photoid > 0)
				{  
					Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
					contactPhoto = BitmapFactory.decodeStream(input);  
				}
				else
				{
					contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);  
				}
			  
				mContactsName.add(contactName);	 
				mContactsNumber.add(phoneNumber);  
				mContactsPhonto.add(contactPhoto);	
			}
  
			phoneCursor.close();
		}
	}  
	  
	/**得到手机SIM卡联系人人信息**/  
	@SuppressWarnings("unused")
	private void getSimContacts() {	 
		ContentResolver resolver = mContext.getContentResolver();  
		// 获取SIM卡联系人  
		Uri uri = Uri.parse("content://icc/adn");  
			Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);	

		if (phoneCursor != null) 
		{  
			while (phoneCursor.moveToNext()) 
			{  
				// 得到手机号码  
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				// 当手机号码为空的或者为空字段 跳过当前循环  
				if (TextUtils.isEmpty(phoneNumber))	 
				continue;  
				// 得到联系人名称	
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);	
	
				//Sim卡中没有联系人头像
				mContactsName.add(contactName);	 
				mContactsNumber.add(phoneNumber);  
			 }
			phoneCursor.close();  
		}
	}
	
	/* 预留微信获取联系人的接口 */
	@SuppressWarnings("unused")
	private void getWeChatContacts() {
		return ;
	}
	
	/** 外部调用该接口获取到被选中的联系人 **/
	public ArrayList getChooseContact()
	{
		// 返回之前设置的值
		return mContactorsList;
	}
	
	class MyListAdapter extends BaseAdapter { 
		// 存储被选中的值，选中的值为id：position
		private ArrayList chooseItemList = new ArrayList();
		
		public MyListAdapter(Context context) {	 
			mContext = context;	 
		}
  
		public int getCount() {
			//设置绘制数量  
			return mContactsName.size();  
		}  
	  
		@Override  
		public boolean areAllItemsEnabled() {  
			return false;  
		}  
	  
		public Object getItem(int position) {  
			return position;  
		}  
	  
		public long getItemId(int position) {
			return position;  
		}
		
		public ArrayList getChooseItemList()
		{
			return chooseItemList;
		}
		
		// 进行展现的代码，用于展示当前的内容
		public View getView(int position, View convertView, ViewGroup parent) {	 
			ImageView iamge = null;	 
			TextView title = null;	
			CheckBox checkbox = null;
			final int recordpos = position;
			
			//TextView text = null;  
			if (convertView == null || position < mContactsNumber.size()) 
			{  
				convertView = LayoutInflater.from(mContext).inflate(R.layout.activity_contactscreen, null);	
				checkbox = (CheckBox)convertView.findViewById(R.id.choosebox);
				iamge = (ImageView) convertView.findViewById(R.id.color_image);	 
				title = (TextView) convertView.findViewById(R.id.color_title);
				//text = (TextView) convertView.findViewById(R.id.color_text);  
				
				// 记录了click事件，如果这个复选框被选中，那么认为是选中了
				checkbox.setOnClickListener(new View.OnClickListener() {
					
					@SuppressWarnings("unchecked")
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						// 判断是否第一次选中，如果不是第一次被选中，那么是用户取消选定的行为，则从map中去掉
						CheckBox cb = (CheckBox) v;
						// 如果当前checkbox已经选中
						if (cb.isChecked())
						{
							chooseItemList.add(recordpos);
						}
						else
						{
							// 这里的判断方式性能较差，建议好好思考一下
							if (chooseItemList.contains(recordpos))
							{
								// 如果发现一个item还在record里面，那么删除再说
								chooseItemList.remove(recordpos);
							}
						}
					}
				});
			}
			
			//绘制联系人名称  
			title.setText(mContactsName.get(position));	 
			//绘制联系人号码  
			//text.setText(mContactsNumber.get(position));	
			//绘制联系人头像  
			iamge.setImageBitmap(mContactsPhonto.get(position));  
			return convertView; 
		}
	}
}
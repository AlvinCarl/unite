package com.example.unite;

import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.text.TextUtils;

public class LocalContactorInfo {
	
	Context mContext = null; 
	
	/**��ȡ��Phone���ֶ�**/  
	private static final String[] PHONES_PROJECTION = new String[] {
		Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID,Phone.CONTACT_ID };  
 
	/**��ϵ����ʾ����**/  
	private static final int PHONES_DISPLAY_NAME_INDEX = 0;	 
  
	/**�绰����**/	
	private static final int PHONES_NUMBER_INDEX = 1;  
	 
	/**ͷ��ID**/	
	private static final int PHONES_PHOTO_ID_INDEX = 2;	 
	 
	/**��ϵ�˵�ID**/  
	private static final int PHONES_CONTACT_ID_INDEX = 3;  
	 
	/**��ϵ������**/	 
	public ArrayList<String> mContactsName = new ArrayList<String>();	
  
	/**��ϵ��ͷ��**/	 
	public ArrayList<String> mContactsNumber = new ArrayList<String>();  
  
	/**��ϵ��ͷ��**/	 
	public ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	public LocalContactorInfo(Context context)
	{
		mContext = context;
	}
	
	/** �õ��ֻ�ͨѶ¼��ϵ����Ϣ **/ 
	public void getPhoneContacts() {
		ContentResolver resolver = mContext.getContentResolver();  
		// ��ȡ�ֻ���ϵ��	
		Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);	 
  
		if (phoneCursor != null) {	
			while (phoneCursor.moveToNext()) {	
				//�õ��ֻ�����  
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				//���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��	 
				if (TextUtils.isEmpty(phoneNumber))	 
					continue;  
  
				//�õ���ϵ������  
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);	  
				//�õ���ϵ��ID  
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);	  
				//�õ���ϵ��ͷ��ID	 
				Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);	
				//�õ���ϵ��ͷ��Bitamp	 
				Bitmap contactPhoto = null;	 
  
				//photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�	
				if (photoid > 0)
				{  
					Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);  
					InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);  
					contactPhoto = BitmapFactory.decodeStream(input);  
				}
				else
				{
					contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher);  
				}
			  
				mContactsName.add(contactName);	 
				mContactsNumber.add(phoneNumber);  
				mContactsPhonto.add(contactPhoto);	
			}
  
			phoneCursor.close();
		}
	}  
	  
	/**�õ��ֻ�SIM����ϵ������Ϣ**/  
	private void getSimContacts() {	 
		ContentResolver resolver = mContext.getContentResolver();  
		// ��ȡSIM����ϵ��  
		Uri uri = Uri.parse("content://icc/adn");  
			Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null, null);	

		if (phoneCursor != null) 
		{  
			while (phoneCursor.moveToNext()) 
			{  
				// �õ��ֻ�����  
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);  
				// ���ֻ�����Ϊ�յĻ���Ϊ���ֶ� ������ǰѭ��  
				if (TextUtils.isEmpty(phoneNumber))	 
				continue;  
				// �õ���ϵ������	
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);	
	
				//Sim����û����ϵ��ͷ��
				mContactsName.add(contactName);	 
				mContactsNumber.add(phoneNumber);  
			 }
			phoneCursor.close();  
		}
	}

}

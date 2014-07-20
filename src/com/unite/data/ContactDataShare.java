package com.unite.data;
import java.util.HashMap;
import java.util.Map;

import com.example.unite.ContactAdapter;

import android.app.Application;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Alvin
 * 这个类用于进行contact的数据共享
 * 主要用于不同的activity之间互相共享数据
 */

public class ContactDataShare extends Application {
	protected static String ACTIVITY_TAG="contactdatalog";  
	// 这里是被选中的联系人的信息
	private HashMap<String, String> contactinfo;
	
	/**
	 * 填充联系人信息
	 * */
	public void setContactInfo(HashMap contact)
	{
		Log.d(ContactDataShare.ACTIVITY_TAG, "set contact info is, " + contact.toString());
		contactinfo = contact;
	}
	
	/**
	 * 获取联系人的信息 
	 **/
	public HashMap getContactInfo()
	{
		return contactinfo;
	}
}

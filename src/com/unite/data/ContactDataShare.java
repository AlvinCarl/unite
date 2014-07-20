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
 * ��������ڽ���contact�����ݹ���
 * ��Ҫ���ڲ�ͬ��activity֮�以�๲������
 */

public class ContactDataShare extends Application {
	protected static String ACTIVITY_TAG="contactdatalog";  
	// �����Ǳ�ѡ�е���ϵ�˵���Ϣ
	private HashMap<String, String> contactinfo;
	
	/**
	 * �����ϵ����Ϣ
	 * */
	public void setContactInfo(HashMap contact)
	{
		Log.d(ContactDataShare.ACTIVITY_TAG, "set contact info is, " + contact.toString());
		contactinfo = contact;
	}
	
	/**
	 * ��ȡ��ϵ�˵���Ϣ 
	 **/
	public HashMap getContactInfo()
	{
		return contactinfo;
	}
}

package com.example.unite;

import java.io.InputStream;	 
import java.util.ArrayList;	 
import java.util.HashMap;
import java.util.Map;

import com.example.unite.FullscreenActivity;
  
import android.R.integer;
import android.app.Activity;
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
import android.text.InputFilter.LengthFilter;
import android.text.TextUtils;	
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;	 
import android.view.View;  
import android.view.View.OnClickListener;
import android.view.ViewGroup;	
import android.widget.AdapterView;	
import android.widget.BaseAdapter;	
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;  
import android.widget.ListView;	 
import android.widget.TextView;	 
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;	

import com.example.unite.ContactAdapter;
import com.example.unite.ContactAdapter.ViewHolder;
import com.example.unite.LocalContactorInfo;
import com.unite.data.ContactDataShare;
 

/***  基础类，用于获取联系
 *		  1. 机器本地的联系人
 *		  2. 微信的联系人
 *		  3. 待添加
  ***/

public class ContactorActivity extends Activity {
	protected static final String ACTIVITY_TAG="contactactivelog";  
	Context mContext = null;  
	private LocalContactorInfo localContactor = null;	
	private ListView mListView = null;	
	@SuppressWarnings("rawtypes")
	private ArrayList mContactorsList = null;
	private ContactAdapter myAdapter = null;
	// 三个按钮
	private Button btGetall;
	private Button btCancel;
	private Button btSubmit;
	// 已经选定的数量
	private int iChooseNum;
	// 显示已经选定了多少人
	private TextView tvShow;
	// 已经选定的人，做一个两层的结构来做
	private HashMap contactmap;
	
	// 刷新listview和TextView的显示
    private void dataViewChanged() {
        // 通知listView刷新
    	myAdapter.notifyDataSetChanged();
        // TextView显示最新的选中数目
    	tvShow.setText("已选中" + iChooseNum + "项");
    }
    
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
  
	@Override  
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 设置当前界面为主界面
		setContentView(R.layout.activity_contactmain);
		contactmap = new HashMap();
		
		/** 实例化各个控件 **/
		mListView = (ListView)findViewById(R.id.contactListView);
		btGetall = (Button)findViewById(R.id.selectall);
		btCancel = (Button)findViewById(R.id.cancel);
		btSubmit = (Button)findViewById(R.id.sumbit);
		tvShow = (TextView)findViewById(R.id.chooseview);
		
		mContext = this; 
		localContactor = new LocalContactorInfo(mContext);
		mContactorsList = new ArrayList();
		
		/**得到手机通讯录联系人信息**/	
		localContactor.getPhoneContacts();
		myAdapter = new ContactAdapter(this, 
																		localContactor.mContactsName, 
																		localContactor.mContactsNumber, 
																		localContactor.mContactsPhonto); 
		
		final int contactLength = localContactor.mContactsName.size();
		
		// 全选按钮绑定的消息事件
		btGetall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将MyAdapter中的map值全部设为true
                for (int index = 0; index < contactLength; index++) 
                {
                	myAdapter.getIsSelected().put(index, true);
                }
                // 数量设为list的长度
                iChooseNum = contactLength;
                // 刷新listview和TextView的显示
                dataViewChanged();
            }
        });
		
		// 提交当前已经选择的联系人，这里将现有的联系人提交到application里面保留
        btSubmit.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v)
        	{
        		// 这里需要注意，用的应该是全局的变量，而不应该是new出来的
        		ContactDataShare contactdata = (ContactDataShare)getApplication();
        		contactdata.setContactInfo(contactmap);
        		
        		// 然后退出当前的activity
        		finish();
        	} 
        });
        
        // 取消按钮的回调接口
        btCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 遍历list的长度，将已选的设为未选
                for (int index = 0; index < contactLength; index++) {
                    if (myAdapter.getIsSelected().get(index)) {
                    	myAdapter.getIsSelected().put(index, false);
                        --iChooseNum;
                    }
                    /*	这里是将未选的替换成了已选
                    else 
                    {
                        myAdapter.getIsSelected().put(index, true);
                        ++iChooseNum;
                    }
                    */
                }
                // 刷新listview和TextView的显示
                dataViewChanged();
            }
        });
		
		// 这里是列表的空间绑定的消息事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@SuppressWarnings("unchecked")
			@Override  
			public void onItemClick(AdapterView<?> adapterView, View view,	
				int position, long id) {
				
				// 记录信息
				Map<String, String> contactInfoMap = new HashMap<String, String>();
				String username = localContactor.mContactsName.get(position);
				String userphone = localContactor.mContactsNumber.get(position);
				contactInfoMap.put(username, userphone);
				
				mContactorsList.add(contactInfoMap);
				
				// 获取当前的view
				ViewHolder holder = (ViewHolder) view.getTag();
				Log.e(ContactAdapter.ACTIVITY_TAG, "the holder is, " + holder);
				holder.checkbox.toggle();
				myAdapter.getIsSelected().put(position, holder.checkbox.isSelected());
				
				if (holder.checkbox.isChecked())
				{
					iChooseNum++;
					contactmap.put(username, userphone);
				}
				else
				{
					// 准备删除contanctlist中已经存在的值
					if (contactmap.containsKey(username))
					{
						// 目前没有考虑多线程操作，所以采用的方式很简单，多线程的时候注意这里
						contactInfoMap.remove(username);
					}
					iChooseNum--;
				}
				
				tvShow.setText("已选中" + iChooseNum + "项");
				
			}  
		});

		mListView.setAdapter(myAdapter);
		super.onCreate(savedInstanceState);	 
	} 
	
	/* 预留微信获取联系人的接口，从另外的类获取 */
	@SuppressWarnings("unused")
	private void getWeChatContacts() {
		return ;
	}
	
}
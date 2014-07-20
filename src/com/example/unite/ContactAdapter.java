package com.example.unite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ContactAdapter  extends BaseAdapter {
	
	// 设置一个静态的view holder
	static class ViewHolder {
		  TextView title;
		  TextView phone;
		  ImageView image;
		  CheckBox checkbox;
		  int position;
		}
	
	protected static final String ACTIVITY_TAG="contactlog";  
	
	// 用来控制CheckBox的选中状况
    private static HashMap<Integer,Boolean> isSelected;
	
	// 存储被选中的值，选中的值为id：position
	private ArrayList chooseItemList = new ArrayList();
	// 上下文
	private Context mContext = null;
	// 用来导入布局
	private LayoutInflater mInflater = null;
	
	/**联系人名称**/	 
	private ArrayList<String> mContactsName = new ArrayList<String>();	
	/**联系人头像**/	 
	private ArrayList<String> mContactsNumber = new ArrayList<String>();  
	/**联系人头像**/	 
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	// 构造函数传入值
	public ContactAdapter(Context context,  
												ArrayList<String> contactsName, 
												ArrayList<String> contactsNumber,
												ArrayList<Bitmap> contactsPhone) {	 
			mContext = context;
			mContactsName = contactsName;
			mContactsNumber = contactsNumber;
			mContactsPhonto = contactsPhone;
			mInflater = LayoutInflater.from(mContext);
			isSelected = new HashMap<Integer, Boolean>();
			// 初始化数据
			initDate();
	}
	
	// 初始化isSelected的数据
    private void initDate(){
        for(int i=0; i<mContactsName.size();i++) {
            getIsSelected().put(i,false);
        }
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
		ViewHolder vHolder = null;
		vHolder = new ViewHolder();
		final int recordpos = position;
		
		//TextView text = null;  
		if (convertView == null || position < mContactsNumber.size()) 
		{  
			convertView = mInflater.inflate(R.layout.activity_contactscreen, null);
			vHolder.checkbox = (CheckBox)convertView.findViewById(R.id.choosebox);
			vHolder.image = (ImageView) convertView.findViewById(R.id.color_image);	 
			vHolder.title = (TextView) convertView.findViewById(R.id.color_title);
			//text = (TextView) convertView.findViewById(R.id.color_text);  
			convertView.setTag(vHolder);
			//绘制联系人名称  
			vHolder.title.setText(mContactsName.get(position));
			//绘制联系人号码  
			//text.setText(mContactsNumber.get(position));	
			//绘制联系人头像  
			vHolder.image.setImageBitmap(mContactsPhonto.get(position)); 
			try {
				vHolder.checkbox.setChecked(getIsSelected().get(position));
			} catch (Exception e) {
				// TODO: handle exception
				Log.e(ContactAdapter.ACTIVITY_TAG, "the selected value is, " + isSelected);
			}
			//vHolder.checkbox.setChecked(getIsSelected().get(position));
		}
		else 
		{
			Log.e(ContactAdapter.ACTIVITY_TAG, "with empty view and get tag!");
			vHolder = (ViewHolder) convertView.getTag();
		}
		
		return convertView;
	}
		
	public HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer,Boolean> isSelected) {
    	ContactAdapter.isSelected = isSelected;
    }
}

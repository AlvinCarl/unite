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
	
	// ����һ����̬��view holder
	static class ViewHolder {
		  TextView title;
		  TextView phone;
		  ImageView image;
		  CheckBox checkbox;
		  int position;
		}
	
	protected static final String ACTIVITY_TAG="contactlog";  
	
	// ��������CheckBox��ѡ��״��
    private static HashMap<Integer,Boolean> isSelected;
	
	// �洢��ѡ�е�ֵ��ѡ�е�ֵΪid��position
	private ArrayList chooseItemList = new ArrayList();
	// ������
	private Context mContext = null;
	// �������벼��
	private LayoutInflater mInflater = null;
	
	/**��ϵ������**/	 
	private ArrayList<String> mContactsName = new ArrayList<String>();	
	/**��ϵ��ͷ��**/	 
	private ArrayList<String> mContactsNumber = new ArrayList<String>();  
	/**��ϵ��ͷ��**/	 
	private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();
	
	// ���캯������ֵ
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
			// ��ʼ������
			initDate();
	}
	
	// ��ʼ��isSelected������
    private void initDate(){
        for(int i=0; i<mContactsName.size();i++) {
            getIsSelected().put(i,false);
        }
    }
  
	public int getCount() {
		//���û�������  
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
		
	// ����չ�ֵĴ��룬����չʾ��ǰ������
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
			//������ϵ������  
			vHolder.title.setText(mContactsName.get(position));
			//������ϵ�˺���  
			//text.setText(mContactsNumber.get(position));	
			//������ϵ��ͷ��  
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

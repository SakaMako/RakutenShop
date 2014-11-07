package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;

import com.android.volley.toolbox.NetworkImageView;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseItemAdapter extends ArrayAdapter<Item> {
	
	protected BaseFragment mFragment = null;
	
	protected BaseItemAdapter(Context context, int resource,
			BaseFragment fragment, ArrayList<Item> myItem) {
		super(context, resource,myItem);
		mFragment = fragment;
	}
	
	public final void resetFragment(BaseFragment fragment){
		mFragment = fragment;
	}
	
	public final void setVisiblePosition(int pos){
		mFragment.setSelection(pos);
	}

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {
		Item item = this.getItem(position);
		//Log.d("BaseItemAdapter","type="+mFragment.getType());
		switch(mFragment.getType()){
		case BaseFragment.TYPE_GRID:
			return getViewGrid(item,convertView,parent);
		case BaseFragment.TYPE_LIST:
			return getViewList(item,convertView,parent);
		}
		return null;
	}
	
	public View getViewList(Item item, View convertView, ViewGroup parent) {
		// ビューを受け取る   
		LinearLayout view = (LinearLayout) convertView;   
		if (view == null) {
			// 受け取ったビューがnullなら新しくビューを生成   
			view = (LinearLayout)((LayoutInflater)App.getAppContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.home_list_item, null);
		}

		// 表示すべきデータの取得   
		if (item != null) {   
			//LineListener listener = new LineListener(position,item);
			TextView screenName1 = (TextView)view.findViewById(R.id.recent_item_text);   
			screenName1.setTypeface(Typeface.DEFAULT_BOLD);   
			screenName1.setText(item.getName());
			
			TextView priceText = (TextView)view.findViewById(R.id.recent_item_price);
			priceText.setText(item.getPriceString());
			
			NetworkImageView urlImageView = (NetworkImageView)view.findViewById(R.id.recent_item_icon);
    		urlImageView.setImageUrl(item.getImage(), App.getImageLoader());
    		urlImageView.setLayoutParams(new LinearLayout.LayoutParams(Item.ITEM_SIZE_LIST,Item.ITEM_SIZE_LIST));
    		urlImageView.setPadding(0, 0, 0, 0);
            urlImageView.setErrorImageResId(R.drawable.ic_action_remove);

		}
		return view;
	}
	
    public View getViewGrid(Item item,View convertView,ViewGroup parent){
    	//Log.d("BaseItemAdapter", "getViewGrid");
       	NetworkImageView imageView = null;
       	if(convertView == null ){
       		imageView = (NetworkImageView) ((LayoutInflater)App.getAppContext()
       				.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
       				.inflate(R.layout.home_grid_item, null);
       		imageView.setLayoutParams(
       			new GridView.LayoutParams(Item.ITEM_SIZE_LIST,Item.ITEM_SIZE_LIST)
       		);		
       		imageView.setPadding(0, 0, 0, 0);
       	}
       	else{
    		imageView = (NetworkImageView) convertView;
       	}
       	
       	imageView.setImageUrl(item.getItemListImage(), App.getImageLoader());
        imageView.setErrorImageResId(R.drawable.ic_action_remove);
      	 
       	return imageView;
    }


	public abstract String getTitle() ;
	
}

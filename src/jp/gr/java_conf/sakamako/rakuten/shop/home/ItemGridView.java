package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalItemSelectedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.Scrollable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

public class ItemGridView extends GridView
implements BaseListView
{

	public ItemGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void onAttachedFragment(BaseFragment fragment) {
		setOnItemClickListener(fragment.getAdapter());
		
		if(fragment.getAdapter() instanceof Scrollable){
			setOnScrollListener((OnScrollListener) fragment.getAdapter());
		}
		
		setOnItemClickListener(fragment.getAdapter());
	}
	
	@Override
	   public View getView(Item item,View convertView,ViewGroup parent){
	    	//Log.d(this.getClass().getSimpleName(), "getView="+item.getName());
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
}

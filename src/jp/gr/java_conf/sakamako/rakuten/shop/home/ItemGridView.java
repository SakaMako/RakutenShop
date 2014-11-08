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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
		
		// ビューを受け取る   
		FrameLayout view = (FrameLayout) convertView;
		if(view == null){
			// 受け取ったビューがnullなら新しくビューを生成   
			view = (FrameLayout)((LayoutInflater)App.getAppContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.home_grid_item, null);
		}
		
		if(item != null){
			NetworkImageView imageView = (NetworkImageView)view.findViewById(R.id.item_image);
       		imageView.setLayoutParams(
	       			new FrameLayout.LayoutParams(Item.ITEM_SIZE_LIST,Item.ITEM_SIZE_LIST)
	       		);		
       		imageView.setPadding(0, 0, 0, 0);
	       	
	       	imageView.setImageUrl(item.getItemListImage(), App.getImageLoader());
	        imageView.setErrorImageResId(R.drawable.ic_action_remove);
	        
            ImageView avaliabilyIcon = (ImageView)view.findViewById(R.id.icon_avaliability);
            if(item.getIsAvailability() != Item.AVALIABILITY_OK){
            	avaliabilyIcon.setVisibility(View.VISIBLE);
            }
            else{
            	avaliabilyIcon.setVisibility(View.GONE);
            }
	    }
	    return view;
	}
}

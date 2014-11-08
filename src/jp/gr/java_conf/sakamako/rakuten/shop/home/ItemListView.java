package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.android.volley.toolbox.NetworkImageView;
import com.squareup.otto.Subscribe;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalItemSelectedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.Scrollable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.view.SortableListView;

public class ItemListView extends SortableListView
implements BaseListView{

	public ItemListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void onAttachedFragment(BaseFragment fragment){
		// フラグメントの種類によって、順番きりかえ可能か設定する
		setOnItemClickListener(fragment.getAdapter());
		if(fragment instanceof DragListener){
			setSortable(true);
			setDragListener((DragListener)fragment);
		}		
		else{
			setSortable(false);
		}
		
		if(fragment.getAdapter() instanceof Scrollable){
			setOnScrollListener((OnScrollListener) fragment.getAdapter());
		}
		
		setOnItemClickListener(fragment.getAdapter());
	}
	
	@Override
	public View getView(Item item, View convertView, ViewGroup parent) {
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
    		urlImageView.setLayoutParams(new FrameLayout.LayoutParams(Item.ITEM_SIZE_LIST,Item.ITEM_SIZE_LIST));
    		urlImageView.setPadding(0, 0, 0, 0);
            urlImageView.setErrorImageResId(R.drawable.ic_action_remove);
            
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

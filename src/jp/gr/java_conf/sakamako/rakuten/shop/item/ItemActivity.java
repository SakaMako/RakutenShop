package jp.gr.java_conf.sakamako.rakuten.shop.item;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
//import jp.gr.java_conf.noappnolife.rakuten.shop.item.ItemHorizontalAdapter.OnFinishListener;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.ItemActivityFinishEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.MakeToastEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.view.CustomViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

public class ItemActivity extends BaseActivity
//implements OnFinishListener
{
	
	private ItemHorizontalAdapter mHorizontalAdapter = null;
	//private CustomViewPager mHorizontalPager = null;
	
	protected final boolean isActionBar(){
		return true;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	    EventHolder.register(mHorizontalAdapter);
	}

	@Override
	public void onPause() {
	    EventHolder.unregister(mHorizontalAdapter);
	    super.onPause();
	}
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
        Item pItem = (Item)getIntent().getSerializableExtra("item");
        Log.d(this.getClass().getSimpleName(),"onCreate-"+pItem.getName());
        
        BaseItemAdapter baseItemAdapter = App.getCurrentAdapter();
			
		setContentView(R.layout.item);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setTitle(baseItemAdapter.getTitle());
		
		mHorizontalAdapter = new ItemHorizontalAdapter(
				getSupportFragmentManager()
				,pItem
				);

		CustomViewPager mHorizontalPager = (CustomViewPager) findViewById(R.id.item_detail_pager);
	    mHorizontalPager.setAdapter(mHorizontalAdapter);
	    mHorizontalPager.setCurrentItem(1);
		mHorizontalPager.setOnPageChangeListener(mHorizontalAdapter);
		mHorizontalPager.setOffscreenPageLimit(4);
		mHorizontalPager.setScrollDurationFactor(5);
		
		mHorizontalAdapter.setPager(mHorizontalPager);
		
		Log.d("ItemActivity","onCreate end-----------------------");
	}
	
	@Override
	// WebView が表示されている場合は、ItemDetail に戻る
	// BackButton は一つずつ戻る
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK){
			int pos = mHorizontalAdapter.getCurrentItem();
			if(pos >= 2){
				mHorizontalAdapter.setCurrentItem(pos-1);
				return true;
			}
			if(pos == 1){
				mHorizontalAdapter.setCurrentItem(0);
				return true;
			}
			return super.onKeyDown(keyCode, event);
			
	    }
	    return false;
	  }
	
	@Override
	// WebView が表示されている場合は、ItemDetail に戻る
	// 左上のバックは一気に ItemDetail に戻る
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			int pos = mHorizontalAdapter.getCurrentItem();
			if(pos >= 2){
				mHorizontalAdapter.setCurrentItem(1);
				return true;
			}
			if(pos == 1){
				mHorizontalAdapter.setCurrentItem(0);
				return true;
			}
			return super.onOptionsItemSelected(item);
		default:
			break;
		}
		return true;
	}
	
	@Subscribe
	public void finishItemActivity(ItemActivityFinishEvent event){
		finish();
	}
	
	@Subscribe
	public void makeToast(MakeToastEvent event){
		super.makeToast(event);
	}
}

package jp.gr.java_conf.sakamako.rakuten.shop.item;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.ItemActivityFinishEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.MakeToastEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.home.HomeActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemVerticalAdapter.OnVerticalPageSelected;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.view.CustomViewPager;
import android.os.Bundle;
import android.support.v4.view.DirectionalViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;

/**
 * ItemActivity
 * +-CustomViewPager
 *   +-ItemHorizontalAdapter
 * @author makoto
 *
 */
public class ItemActivity extends BaseActivity {
	
	private CustomViewPager mHorizontalPager = null;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.item);
		getActionBar().setTitle(HomeActivity.itemAdapter.getTitle());
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mHorizontalPager = (CustomViewPager) findViewById(R.id.item_detail_pager);
		
		Log.d(this.getClass().getSimpleName(),"onCreate end-----------------------");
	}
	
	@Override
	public void onStart(){
		super.onStart();
        Item pItem = (Item)getIntent().getSerializableExtra("item");
        Log.d(this.getClass().getSimpleName(),"onCreate-"+pItem.getName());
        ItemHorizontalAdapter horizontalAdapter = new ItemHorizontalAdapter(
				getSupportFragmentManager()
				,pItem
				);
		mHorizontalPager.setAdapter(horizontalAdapter);
		mHorizontalPager.setOnPageChangeListener(horizontalAdapter);
	    mHorizontalPager.setCurrentItem(1);
	}

	// ItemVerticalFragment.onActivityCreated からこれを読んで
	// ItemVerticalFragment配下の VerticalPager,VerticalAdapter を初期化する
	public void onFragmentCreated(DirectionalViewPager verticalPager, ItemVerticalAdapter verticalAdapter) {
        Item pItem = (Item)getIntent().getSerializableExtra("item");
        BaseItemAdapter baseItemAdapter = HomeActivity.itemAdapter;
		int pos = baseItemAdapter.getPosition(pItem);
		if(pos < 0){
			Log.d(this.getClass().getSimpleName(),"pos="+pos+","+pItem.getCode()
					+ "," + pItem.getName());
		}
		verticalAdapter.setVerticalAdapter(baseItemAdapter);
        verticalAdapter.setVerticalPageSelectedListener((OnVerticalPageSelected) mHorizontalPager.getAdapter());
        verticalPager.setCurrentItem(pos);
	}
	
	@Override
	public void onResume() {
		super.onResume();
	    EventHolder.register(mHorizontalPager.getAdapter());
	}

	@Override
	public void onPause() {
	    EventHolder.unregister(mHorizontalPager.getAdapter());
	    super.onPause();
	}
	
	//----------------------------------------------------------------------------------
	
	@Override
	// WebView が表示されている場合は、ItemDetail に戻る
	// BackButton は一つずつ戻る
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode==KeyEvent.KEYCODE_BACK){
			int pos = mHorizontalPager.getCurrentItem();
			if(pos >= 2){
				mHorizontalPager.setCurrentItem(pos-1);
				return true;
			}
			if(pos == 1){
				mHorizontalPager.setCurrentItem(0);
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
			int pos = mHorizontalPager.getCurrentItem();
			if(pos >= 2){
				mHorizontalPager.setCurrentItem(1);
				return true;
			}
			if(pos == 1){
				mHorizontalPager.setCurrentItem(0);
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

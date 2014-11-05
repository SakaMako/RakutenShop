package jp.gr.java_conf.sakamako.rakuten.shop.item;

import java.util.ArrayList;
import java.util.List;

import com.squareup.otto.Subscribe;

//import jp.gr.java_conf.noappnolife.rakuten.shop.item.ItemVerticalAdapter.OnVerticalPageChangeListener;
//import jp.gr.java_conf.noappnolife.rakuten.shop.item.ItemWebFragment.OnNewListener;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.BusHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NewWebFragmentEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalFragmentCreated;
import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalItemSelectedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;



public class ItemHorizontalAdapter extends FragmentStatePagerAdapter
implements OnPageChangeListener 
{
	
	//----------------------------------------------------------
	
	private List<ItemBaseFragment> mList = null;
	private ViewPager mPager = null;
	private Item mItem = null;
	private ItemWebFragment webFragment = null;
	private ItemVerticalFragment mVerticalFragment = null;
	
	//----------------------------------------------------------

	public ItemHorizontalAdapter(FragmentManager fm,Item item) {
		super(fm);
		
		Log.d("ItemDetailPager","start-----------------------");

		webFragment = new ItemWebFragment();
		
		mList = new ArrayList<ItemBaseFragment>();
		mList.add(new ItemBlankFragment());
		mVerticalFragment = new ItemVerticalFragment();
		mList.add(mVerticalFragment);

		//mList.add(webFragment);
		mItem = item;
		
		Log.d("ItemDetailPager","end-----------------------");
	}

	public void setPager(ViewPager pager) {
		mPager  = pager;
	}

	@Subscribe
	public void onPostCreateView(VerticalFragmentCreated event) {
		
		ItemVerticalAdapter itemVerticalAdapter = event.getVerticalAdapter();
		DirectionalViewPager verticalPager = event.getVerticalPager();
		
		BaseItemAdapter baseItemAdapter = App.getCurrentAdapter();
		int pos = baseItemAdapter.getPosition(mItem);
		if(pos < 0){
			Log.d(this.getClass().getSimpleName(),"pos="+pos+","+mItem.getCode()
					+ "," + mItem.getName());
		}
		
		itemVerticalAdapter.setVerticalAdapter(baseItemAdapter);
		if(verticalPager.getAdapter()==null){
			verticalPager.setAdapter(itemVerticalAdapter);
		}
		verticalPager.setOnPageChangeListener(itemVerticalAdapter);
        verticalPager.setCurrentItem(pos);
        //BusHolder.get().post(new VerticalItemSelectedEvent(pos,mItem));
        BusHolder.get().post(new VerticalItemSelectedEvent(mItem));

        
	}
	//----------------------------------------------------------
	
	public int getCurrentItem(){
		return mPager.getCurrentItem();
	}
	
	public void setCurrentItem(int pos){
		mPager.setCurrentItem(pos);
	}

	@Override
	public Fragment getItem(int i) {
		return mList.get(i);
	}

	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public int getItemPosition(Object obj){
		if(((ItemBaseFragment)obj).isDeleted()){
			Log.d(this.getClass().getSimpleName(),"getItemPosition:NONE:"+obj.getClass().getSimpleName());
			obj = null;
			return POSITION_NONE;
		}
		Log.d(this.getClass().getSimpleName(),"getItemPosition:UNCHANGED"+obj.getClass().getSimpleName());
		return POSITION_UNCHANGED;
	}
	//----------------------------------------------------------
	  @Subscribe
	  public void onVerticalPageSelected(VerticalItemSelectedEvent event) {
		  //int pos = event.getPosition();
		Log.d(this.getClass().getSimpleName(),"onVertincalPageSelected="+event.getItem().getName());
			//Log.d(this.getClass().getSimpleName(),"ItemWebFragmentの作り直し");
		//webFragment = null;
		//webFragment = new ItemWebFragment();
		
		//mItem  = App.getCurrentAdapter().getItem(pos);
		  mItem = event.getItem();
		if(mItem.getIsAvailability() == Item.AVALIABILITY_OK){
			if(mList.size() == 2){
				Log.d(this.getClass().getSimpleName(),"ItemWebFragmentの生成");
				//webFragment.setDeleted(false);
				webFragment = null;
				webFragment = new ItemWebFragment();
				mList.add(webFragment);
			}
			webFragment.reset();
		}
		else{
			Log.d(this.getClass().getSimpleName(),"在庫なし");
			if(mList.size()>=3){
				Log.d(this.getClass().getSimpleName(),"在庫なし="+mList.size());
				//if(!webFragment.isDeleted()){
					Log.d(this.getClass().getSimpleName(),"在庫なしで削除");
					webFragment.setDeleted(true);
					mList.remove(2);
				//}
			}
		}
		this.notifyDataSetChanged();
	}
	  
		@Subscribe
		public void onNewWebFragment(NewWebFragmentEvent event) {
			Log.d(this.getClass().getSimpleName(),(mList.size()+1) + "のwebを追加="+event.getUrl());
			ItemWebFragment fragment = new ItemWebFragment();
			mList.add(fragment);
			this.notifyDataSetChanged();
			mPager.setCurrentItem(mList.size(),true);
			fragment.loadWeb(event.getUrl());
		}
	//------------------------------------------------------
	//Pager の制御
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		//Log.d(this.getClass().getSimpleName(),arg0 + "," + arg1+","+arg2);
		// 一番左にきたら終わり
		if(arg0 == 0 && arg1 < 0.6){
			Log.d(this.getClass().getSimpleName(),"finish");
			BusHolder.get().post(new FinishItemActivity());
		}
		// ItemDetail をちょっとでも右に動かしたらWebView を読み込み開始
		else if(arg0 == 1 && arg1 > 0.1){
			//Log.d(this.getClass().getSimpleName(),"loadWeb");
			if(getCount() > 2 && !((ItemWebFragment)mList.get(2)).isLoadStarted()){
				Log.d(this.getClass().getSimpleName(),"onPageScrolled.loadWeb="+mItem.getName());
				((ItemWebFragment)mList.get(2)).loadWeb(mItem);
			}
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		//何もしない
	}
	
	@Override
	public void onPageSelected(int arg0) {
		Log.d(this.getClass().getSimpleName(),"onPageSelected pos="+arg0);
		// ItemDetail に来たら
		if(arg0 == 1){
			Log.d(this.getClass().getSimpleName(),"count="+this.getCount());
			if(this.getCount() >= 3){
				Log.d(this.getClass().getSimpleName(),"isLoadStarted="+((ItemWebFragment)this.getItem(2)).isLoadStarted());
				//商品Webはリセット
				if(((ItemWebFragment)this.getItem(2)).isLoadStarted()){
					((ItemWebFragment)this.getItem(2)).reset();
				}
			}
		}
		if(arg0 >= 2){
			Log.d(this.getClass().getSimpleName(),"onPageSelected...cnt="+this.getCount());
			//それ以降の商品は全て削除
			for(int i=this.getCount()-1;arg0 < i;i--){
				Log.d(this.getClass().getSimpleName(),"oPageSelected...delete="+i);
				mList.get(i).setDeleted(true);	
				mList.remove(this.getItem(i));
			}
			this.notifyDataSetChanged();	
		}
	}




}

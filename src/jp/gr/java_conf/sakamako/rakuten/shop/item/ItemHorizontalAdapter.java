package jp.gr.java_conf.sakamako.rakuten.shop.item;

import java.util.ArrayList;
import java.util.List;

import com.squareup.otto.Subscribe;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NewWebFragmentEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemVerticalAdapter.OnVerticalPageSelected;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

/**
 * ItemActivity
 * +-ItemHorizontalAdapter
 * @author makoto
 *
 */

public class ItemHorizontalAdapter extends FragmentStatePagerAdapter
implements OnPageChangeListener ,OnVerticalPageSelected
{
	
	//----------------------------------------------------------
	
	private List<ItemBaseFragment> mFragmentList = null;
	private ViewPager mPager = null;
	//private ItemWebFragment webFragment = null;
	//private ItemVerticalFragment mVerticalFragment = null;
	
	//----------------------------------------------------------

	public ItemHorizontalAdapter(FragmentManager fm,Item item) {
		super(fm);
		
		Log.d("ItemDetailPager","start-----------------------");

		//webFragment = new ItemWebFragment();
		
		mFragmentList = new ArrayList<ItemBaseFragment>();
		mFragmentList.add(new ItemBlankFragment());
		ItemVerticalFragment verticalFragment = new ItemVerticalFragment();
		mFragmentList.add(verticalFragment);
		//mItem = item;
		
		Log.d("ItemDetailPager","end-----------------------");
	}

	//----------------------------------------------------------
	
	public int getCurrentItem(){
		return mPager.getCurrentItem();
	}

	@Override
	public Fragment getItem(int i) {
		return mFragmentList.get(i);
	}

	@Override
	public int getCount() {
		return mFragmentList.size();
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
	
	@Override
	public void onVerticalPageSelected(Item pItem) {
		Log.d(this.getClass().getSimpleName(),"onVertincalPageSelected="+pItem.getName());
		
		// 購入可能なら
		if(pItem.getIsAvailability() == Item.AVALIABILITY_OK){
			ItemWebFragment webFragment = null;
			if(mFragmentList.size() == 2){
				Log.d(this.getClass().getSimpleName(),"ItemWebFragmentの生成");
				//webFragment = null;
				//webFragment = new ItemWebFragment();
				webFragment = new ItemWebFragment();
				mFragmentList.add(webFragment);
			}
			else{
				webFragment = (ItemWebFragment) mFragmentList.get(2);
			}
			webFragment.reset();
		}
		// 購入不可なら
		else{
			Log.d(this.getClass().getSimpleName(),"在庫なし");
			if(mFragmentList.size()>=3){
				Log.d(this.getClass().getSimpleName(),"在庫なし="+mFragmentList.size());
				mFragmentList.get(2).setDeleted(true);
				((ItemWebFragment)mFragmentList.get(2)).reset();
				//webFragment.setDeleted(true);
				mFragmentList.remove(2);
			}
		}
		this.notifyDataSetChanged();
	}
	  
	//ブラウザ代わりに WebView を追加していく。ItemWebFragment.WebClinet から呼ばれる
	@Subscribe
	public void onNewWebFragment(NewWebFragmentEvent event) {
		Log.d(this.getClass().getSimpleName(),(mFragmentList.size()+1) + "のwebを追加="+event.getUrl());
		ItemWebFragment fragment = new ItemWebFragment();
		mFragmentList.add(fragment);
		this.notifyDataSetChanged();
		mPager.setCurrentItem(mFragmentList.size(),true);
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
			EventHolder.finishItemActivity();
		}
		// ItemDetail をちょっとでも右に動かしたらWebView を読み込み開始
		else if(arg0 == 1 && arg1 > 0.1){
			//Log.d(this.getClass().getSimpleName(),"loadWeb");
			if(getCount() > 2 && !((ItemWebFragment)mFragmentList.get(2)).isLoadStarted()){
				Item pItem = ((ItemVerticalFragment)mFragmentList.get(1)).getItem();
				Log.d(this.getClass().getSimpleName(),"onPageScrolled.loadWeb="+pItem.getName());
				((ItemWebFragment)mFragmentList.get(2)).loadWeb(pItem);
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
				mFragmentList.get(i).setDeleted(true);	
				mFragmentList.remove(this.getItem(i));
			}
			this.notifyDataSetChanged();	
		}
	}





}

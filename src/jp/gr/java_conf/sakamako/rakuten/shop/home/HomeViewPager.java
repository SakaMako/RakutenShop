package jp.gr.java_conf.sakamako.rakuten.shop.home;

import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import android.content.Context;
import android.graphics.Color;
import android.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.util.AttributeSet;
import android.util.Log;

public class HomeViewPager extends ViewPager implements TabListener {

	public HomeViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		super.setOnPageChangeListener(new OnPageListener());
		super.setBackgroundColor(Color.WHITE);
		super.setOffscreenPageLimit(10);
		 
		if(getAdapter() == null){
			setAdapter(new HomeAdapter(this));
		}
	}
	
	// 現在表示されているフラグメントを取得
	public BaseFragment getCurrentFragment(){
		return ((HomeAdapter)getAdapter()).getItem(getCurrentItem());
	}
	
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		// 選択したタブのpositionに合わせてpageを切り替え
		Log.d(this.getClass().getSimpleName(),"onTabSelected="+tab.getPosition());
		this.setCurrentItem(tab.getPosition());        
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction arg1) {
		Log.d(this.getClass().getSimpleName(),"onTabReselected="+tab.getPosition());
		((HomeAdapter)getAdapter()).getItem(tab.getPosition()).setSelection(0);
	}

	private class OnPageListener implements OnPageChangeListener{
		@Override
		public void onPageScrollStateChanged(int pos) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int pos) {
			Log.d(this.getClass().getSimpleName(),"onPageScrollState="+pos);
			EventHolder.changeTab(pos);
		}
	}
}

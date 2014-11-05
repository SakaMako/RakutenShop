package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.DeleteCategoryDialog.DeleteCategoryListener;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.NewCategoryDialog.OnNewCategoryListener;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import android.util.Log;

public class HomeAdapter extends FragmentStatePagerAdapter
implements TabListener
,DeleteCategoryListener, OnNewCategoryListener


{
	
	private ArrayList<BaseFragment> mList = null;
	private ViewPager mViewPager = null;
	private ActionBar actionBar;
	//private SearchFragment mSearch = null;
	private HomeActivity mActivity = null;
	private static int SEARCH_POSITION = 1;
	
    public HomeAdapter(HomeActivity activity,ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mActivity = activity;
        mList = new ArrayList<BaseFragment>();
        actionBar = mActivity.getSupportActionBar();
        mViewPager = pager;
        int sp=0;
        
        if(App.isRanking()){
            RankingFragment ranking = new RankingFragment();
            mList.add(ranking);
            Tab rtab = actionBar.newTab();
            rtab.setText("ランキング");
            rtab.setTabListener(this);
            actionBar.addTab(rtab);
            sp++;
        }

        SearchFragment search = new SearchFragment();
        mList.add(search);
        Tab stab = actionBar.newTab();
        stab.setText("探す");
        stab.setTabListener(this);
        actionBar.addTab(stab);
        SEARCH_POSITION = sp;
        
        List<Category> list = MyCategory.getInstance().getList();
        for(Iterator<Category> i=list.iterator();i.hasNext();){
        	Category cat = i.next();
        	BaseFragment fragment = new MyItemFragment(cat);
        	mList.add(fragment);
			Tab tab = actionBar.newTab();
			tab.setText(cat.getLabel());
			tab.setTabListener(this);
			actionBar.addTab(tab);
		}
        

		 mViewPager.setAdapter(this);
		 mViewPager.setOnPageChangeListener(mActivity);
		 mViewPager.setBackgroundColor(Color.WHITE);
		 //mViewPager.setPageTransformer(true , new PageTransformer());
		 mViewPager.setOffscreenPageLimit(10);
        
		 mViewPager.setCurrentItem(0);

    }
    
    public void search(SearchParams searchParams){
        ((SearchFragment)mList.get(SEARCH_POSITION)).search(searchParams);
        mViewPager.setCurrentItem(SEARCH_POSITION);
    }
    
	@Override
	public void onTabSelected(Tab tab, FragmentTransaction arg1) {
		Log.d("TopActivity","onTabSelected"+tab.getPosition());
		// 選択したタブのpositionに合わせてpageを切り替え
		if(mViewPager != null){
			this.notifyDataSetChanged();
			mViewPager.setCurrentItem(tab.getPosition());
		}        
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		getCurrentFragment().setSelection(0);
	}

	//カテゴリの追加
	public boolean onNewCategory(String text) {	
		Category cat = MyCategory.getInstance().add(text);
		if(cat == null){
			Log.d("TabFragmentAdapter","カテゴリの重複"+text);
			return false;
		}
		
		mList.add(new MyItemFragment(cat));
		Tab tab = actionBar.newTab();
		tab.setText(cat.getLabel());
		tab.setTabListener(this);
		actionBar.addTab(tab);
		this.notifyDataSetChanged();
		mViewPager.setCurrentItem(mList.size());
		return true;
	}
	
	public BaseFragment getCurrentFragment(){
		return this.getItem(this.getCurrentPosition());
	}
	
	@Override
	public BaseFragment getItem(int arg0) {
		return mList.get(arg0);
	}
	
	public int getCurrentPosition(){
		return mViewPager.getCurrentItem();
	}
	
	public void setCurrentPosition(int i) {
		mViewPager.setCurrentItem(i);
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	// カテゴリの削除
	public void onDeleteCategory() {
		int currentItem = mViewPager.getCurrentItem();
		MyItemFragment fragment = (MyItemFragment) mList.get(currentItem);
		Log.d("TagFragmentAdapter","カテゴリの削除 = " +currentItem
				+ ","+fragment.getCategory().getLabel()
				+ ","+fragment.getCategory().getId());
		
		// データの削除
		MyCategory.getInstance().remove(fragment.getCategory());
		
		// フラグメントの削除
		mList.remove(currentItem);
		notifyDataSetChanged();
		mActivity.getSupportActionBar().removeTabAt(currentItem);

		mViewPager.setCurrentItem(currentItem-1);
	}
	
	public int replace(){
		Log.d("TagFragmentAdapter","replace start ----------------------------");
		int pos = mViewPager.getCurrentItem();
		Log.d("TagFragmentAdapter","replace pos=" + pos);
		BaseFragment oldFragment = (BaseFragment) this.getItem(pos);
		BaseFragment newFragment = (BaseFragment) oldFragment.replace();
		
		newFragment.setInitPosition(oldFragment.getVisiblePosition());
		
		FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.commit();
   
        mList.set(pos, newFragment);
		notifyDataSetChanged();
		
		Log.d("TagFragmentAdapter","replace end ----------------------------");
		return newFragment.getType();
	}
	
	@Override
	public int getItemPosition(Object object) {		
		int i = mList.indexOf(object);
		if(i < 0){
			return POSITION_NONE;
		}
		return POSITION_UNCHANGED;
	}


}

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
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class HomeAdapter extends FragmentStatePagerAdapter
implements 
DeleteCategoryListener, OnNewCategoryListener


{
	
	private ArrayList<BaseFragment> mList = null;
	private HomeViewPager mViewPager = null;
	private ActionBar actionBar;
	private ActionBarActivity mActivity = null;
	private static int SEARCH_POSITION = 1;
	
    public HomeAdapter(ActionBarActivity activity,HomeViewPager pager) {
        super(activity.getSupportFragmentManager());
        mActivity = activity;
        actionBar = mActivity.getSupportActionBar();
        mViewPager = pager;
        
        initFragment();
        for(Iterator<BaseFragment> it = mList.iterator();it.hasNext();){
        	BaseFragment fragment = it.next();
            Tab rtab = actionBar.newTab();
            rtab.setText(fragment.getTabTitle());
            rtab.setTabListener(mViewPager);
            actionBar.addTab(rtab);
        }
    }
    
    private void initFragment(){
    	int sp = 0;

    	mList = new ArrayList<BaseFragment>();
    	if(App.isRanking()){
    		RankingFragment ranking = new RankingFragment();
    		mList.add(ranking);
    		sp++;
    	}

    	SearchFragment search = new SearchFragment();
    	mList.add(search);
    	SEARCH_POSITION = sp;
    
    	List<Category> list = MyCategory.getInstance().getList();
    	for(Iterator<Category> i=list.iterator();i.hasNext();){
    		Category cat = i.next();
    		BaseFragment fragment = new MyItemFragment(cat);
    		mList.add(fragment);
    	}
    }
    
    public void search(SearchParams searchParams){
        ((SearchFragment)mList.get(SEARCH_POSITION)).search(searchParams);
        mViewPager.setCurrentItem(SEARCH_POSITION);
    }
	
	@Override
	public BaseFragment getItem(int arg0) {
		return mList.get(arg0);
	}

	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public int getItemPosition(Object object) {		
		int i = mList.indexOf(object);
		if(i < 0){
			return POSITION_NONE;
		}
		return POSITION_UNCHANGED;
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
		tab.setTabListener(mViewPager);
		actionBar.addTab(tab);
		this.notifyDataSetChanged();
		mViewPager.setCurrentItem(mList.size());
		return true;
	}

	@Override
	// カテゴリの削除
	public void onDeleteCategory() {
		int currentItem = mViewPager.getCurrentItem();
		MyItemFragment fragment = (MyItemFragment) mList.get(currentItem);
		Log.d(this.getClass().getSimpleName(),"カテゴリの削除 = " +currentItem
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
		Log.d(this.getClass().getSimpleName(),"replace start ----------------------------");
		int pos = mViewPager.getCurrentItem();
		Log.d(this.getClass().getSimpleName(),"replace pos=" + pos);
		BaseFragment oldFragment = (BaseFragment) this.getItem(pos);
		
		Log.d(this.getClass().getSimpleName(),"replace type=" + oldFragment.getType());
		oldFragment.setType(oldFragment.getReverseType());
		Log.d(this.getClass().getSimpleName(),"replace type=" + oldFragment.getType());
        mActivity.getSupportFragmentManager().beginTransaction()
        .detach(oldFragment)
        .attach(oldFragment)
        .commit();
        BaseFragment newFragment = oldFragment;
        
/**
		BaseFragment newFragment = (BaseFragment) oldFragment.replace();
		newFragment.setInitPosition(oldFragment.getVisiblePosition());
		FragmentManager manager = mActivity.getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.remove(oldFragment);
        transaction.commit();
        mList.set(pos, newFragment);
        */
		notifyDataSetChanged();
		
		Log.d(this.getClass().getSimpleName(),"replace end ----------------------------");
		return newFragment.getType();
	}
}

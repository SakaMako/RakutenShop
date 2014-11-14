package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.DeleteCategoryDialog.DeleteCategoryListener;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.NewCategoryDialog.OnNewCategoryListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.SearchPostEvent;
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
implements DeleteCategoryListener, OnNewCategoryListener {

	private ArrayList<BaseFragment> mList = null;
	private HomeViewPager mViewPager = null;
	private static int SEARCH_POSITION = 1;
	
    public HomeAdapter(HomeViewPager pager) {
        super(((ActionBarActivity)pager.getContext()).getSupportFragmentManager());
        mViewPager = pager;
        
        Log.d(this.getClass().getSimpleName(),"Constructor="+pager.getContext().getClass().getSimpleName());
        this.init();
    }
    
    private void init(){
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
    	
        for(Iterator<BaseFragment> it = mList.iterator();it.hasNext();){
        	BaseFragment fragment = it.next();
            Tab rtab = this.getActionBar().newTab();
            rtab.setText(fragment.getTabTitle());
            rtab.setTabListener(mViewPager);
            this.getActionBar().addTab(rtab);
        }
    }
    
    //-------------------------------------------------------------------
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
    //-------------------------------------------------------------------
	
    @Subscribe
    public void goToSearch(SearchPostEvent event){
    	Log.d(this.getClass().getSimpleName(),"goToSearch");
    	// ちらつき防止
    	if(mViewPager.getCurrentItem() != SEARCH_POSITION){
    		mViewPager.setCurrentItem(SEARCH_POSITION);
    	}
	    mViewPager.getCurrentFragment().setSelection(0);
    }

 	//カテゴリの追加
	public boolean onNewCategory(String text) {	
		Category cat = MyCategory.getInstance().add(text);
		if(cat == null){
			Log.d("TabFragmentAdapter","カテゴリの重複"+text);
			return false;
		}
		
		mList.add(new MyItemFragment(cat));
		Tab tab = this.getActionBar().newTab();
		tab.setText(cat.getLabel());
		tab.setTabListener(mViewPager);
		this.getActionBar().addTab(tab);
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
		this.getActionBar().removeTabAt(currentItem);
		mViewPager.setCurrentItem(currentItem-1);
	}
	
	public final int replace(){
		Log.d(this.getClass().getSimpleName(),"replace start ----------------------------");
		int pos = mViewPager.getCurrentItem();
		BaseFragment fragment = (BaseFragment) this.getItem(pos);
		fragment.setType(fragment.getReverseType());
		this.getFragmentManager().beginTransaction()
        .detach(fragment)
        .attach(fragment)
        .commit();
		notifyDataSetChanged();
		
		Log.d(this.getClass().getSimpleName(),"replace end ----------------------------");
		return fragment.getType();
	}
	
	private FragmentManager getFragmentManager(){
		return ((ActionBarActivity)mViewPager.getContext()).getSupportFragmentManager();
	}
	
	private ActionBar getActionBar(){
		return ((ActionBarActivity)mViewPager.getContext()).getSupportActionBar();
	}
}

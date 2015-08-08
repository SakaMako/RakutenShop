package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.SearchPostEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Category;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;

public class HomeAdapter extends FragmentStatePagerAdapter {

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
    		RankingFragment ranking = RankingFragment.getInstance();
    		mList.add(ranking);
    		sp++;
    	}

    	SearchFragment search = SearchFragment.getInstance();
    	mList.add(search);
    	SEARCH_POSITION = sp;
    
    	List<Category> list = MyCategory.getInstance().getList();
    	for(Iterator<Category> i=list.iterator();i.hasNext();){
    		Category cat = i.next();
    		BaseFragment fragment = MyItemFragment.getInstance(cat);
    		mList.add(fragment);
    	}
    	
    	getActionBar().removeAllTabs();
        for(Iterator<BaseFragment> it = mList.iterator();it.hasNext();){
        	BaseFragment fragment = it.next();
            Tab rtab = this.getActionBar().newTab();
            rtab.setText(fragment.getTabTitle());
            rtab.setIcon(fragment.getTabIcon());
            rtab.setTabListener(mViewPager);
            this.getActionBar().addTab(rtab);
        }
    }
    
    //-------------------------------------------------------------------
	@Override
	public BaseFragment getItem(int pos) {
		return mList.get(pos);
    }

	@Override
	public int getCount() {
		return mList.size();
	}
	
	@Override
	public int getItemPosition(Object object) {	
        return POSITION_NONE;
	}
	
	@Override
	public CharSequence getPageTitle (int position){
		return getItem(position).getTabTitle();
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
    
    public void addCategory(String categoryName){
    	Log.d(this.getClass().getSimpleName(),"deleteCategory="+categoryName);

    	MyCategory.getInstance().add(categoryName);
    	HomeAdapter adapter = new HomeAdapter(mViewPager);
    	mViewPager.setAdapter(adapter);
  	  
    	getActionBar().setSelectedNavigationItem(adapter.getCount()-1);
    }
     
    public void deleteCategory(String categoryName) {
		Log.d(this.getClass().getSimpleName(),"deleteCategory="+categoryName);
		Category cat = MyCategory.getInstance().getCategory(categoryName);
		int pos = MyCategory.getInstance().getList().indexOf(cat);
		MyCategory.getInstance().remove(cat);
		MyItemFragment.removeInstance(cat);
		
    	HomeAdapter adapter = new HomeAdapter(mViewPager);
    	mViewPager.setAdapter(adapter);
    	
    	// Search + Ranking + MyItem - 1
		getActionBar().setSelectedNavigationItem(pos+SEARCH_POSITION+1-1);
	}


	public void moveCategory(ArrayList<String> list) {
		
		MyCategory.getInstance().moveCategory(list);
    	HomeAdapter adapter = new HomeAdapter(mViewPager);
    	mViewPager.setAdapter(adapter);
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
		return ((ActionBarActivity)mViewPager.getContext()).getActionBar();
	}

}

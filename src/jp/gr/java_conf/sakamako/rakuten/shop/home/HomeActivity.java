package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.DeleteCategoryDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.NewCategoryDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.SearchPreEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.ShowItemDetailEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.TabChangedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import jp.gr.java_conf.sakamako.view.CustomSearchView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * 共通の Activity クラス
 * @author makoto.sakamoto
 * @param <mViewPager>
 *
 */


@SuppressLint("NewApi")
public class HomeActivity<mViewPager> extends BaseActivity 
	{
	
	protected static final int MENU_ID_ADD = 1;
	protected static final int MENU_ID_DELETE = 2;
	protected static final int MENU_ID_DISPLAY = 6;
	protected static final int MENU_ID_INVENTORY = 8;
	
	private DrawerLayout mDrawerLayout = null;
	private ListView mDrawerList = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private CustomSearchView mSearchView = null;
	private HomeViewPager mViewPager = null;
	private boolean isZaikoCheck = false;

	
	@Override
	protected final boolean isActionBar(){
		return true;
	}
		
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Log.d(this.getClass().getSimpleName(),"onCreate Start");    
		setContentView(R.layout.home);    
		
	    ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionBarView = getLayoutInflater().inflate(R.layout.custom_action_bar_top, null);
        actionBar.setCustomView(actionBarView);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);   
        actionBar.show();
        
        // onCreateOptionsMenu の前に呼ばないと setQuery がうまくいかない
        mSearchView = (CustomSearchView) actionBarView.findViewById(R.id.action_bar_search);
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	addMenu(menu,MENU_ID_DISPLAY,"切り替え",R.drawable.ic_action_view_as_grid);
    	addMenu(menu,MENU_ID_INVENTORY,"在庫なし含む",R.drawable.ic_action_location_searching);
    	addMenu(menu,MENU_ID_DELETE,"カテゴリ削除",R.drawable.ic_action_discard);
    	addMenu(menu,MENU_ID_ADD,"追加",R.drawable.ic_action_new_label);
    	
    	// 最初の１回だけでは強制的に０にしてメニューを初期化する
    	EventHolder.changeTab(0);
    	return true;
    }
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
        if(mDrawerLayout == null){
        	mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        	mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        }
        if(mDrawerToggle == null){
        	mDrawerToggle = new ActionBarDrawerToggle(
	                this,
	                mDrawerLayout,
	                R.drawable.ic_drawer,
	                R.string.open,
	                R.string.close
        			);
        	mDrawerToggle.setDrawerIndicatorEnabled(true);
        	mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
	    mDrawerToggle .syncState();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		// メンバ変数のものが null になっていたら再生成
		mViewPager  = (HomeViewPager) findViewById(R.id.viewpager);
		mViewPager.onAttachedActivity(this);

        if(mDrawerList == null){
        	// DrawerList
        	mDrawerList = (ListView) findViewById(R.id.left_drawer);
        	DrawerMenuAdapter adapter = new DrawerMenuAdapter();
        	mDrawerList.setAdapter(adapter);
        	mDrawerList.setOnItemClickListener(adapter);
        }
        
        EventHolder.register(mViewPager.getAdapter());
	}
	
	@Override
	public void onPause() {
	    EventHolder.unregister(mViewPager.getAdapter());
	    super.onPause();
	}
	
	//----------------------------------------------------------------------

	@Override
	// もしバックボタンを押されても左端で無ければ一旦左端に戻る
	public boolean dispatchKeyEvent(KeyEvent event) {
		HomeAdapter homeAdapter = (HomeAdapter) mViewPager.getAdapter();

	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	        switch (event.getKeyCode()) {
	        case KeyEvent.KEYCODE_BACK:
	        	if(mViewPager.getCurrentItem() != 0){
	        		mViewPager.setCurrentItem(0);
	        		return false;
	        	}
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	/** アプリケーションアイコン押下時の動作 */
	public boolean onOptionsItemSelected(MenuItem item){
		HomeAdapter homeAdapter = (HomeAdapter) mViewPager.getAdapter();

		switch(item.getItemId()){
		case android.R.id.home:
			if(mDrawerLayout.isDrawerOpen(mDrawerList)){
				mDrawerLayout.closeDrawer(mDrawerList);
			}
			else{
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;
		case MENU_ID_ADD:
			showDialog("カテゴリの追加",new NewCategoryDialog(homeAdapter));
            break;
		case MENU_ID_DELETE:
			int pos = mViewPager.getCurrentItem();
			MyItemFragment fragment = (MyItemFragment) homeAdapter.getItem(pos);
			showDialog("カテゴリの削除",
					new DeleteCategoryDialog(fragment.getCategory(),homeAdapter)
			);
			break;
		case MENU_ID_DISPLAY:
			int type = homeAdapter.replace();
			setListTypeIcon(type);
			break;
		case MENU_ID_INVENTORY:
			if(isZaikoCheck){
				isZaikoCheck = false;
			}
			else{
				isZaikoCheck = true;
			}
			mSearchView.onQueryTextSubmit(null);
			break;
		default:
			break;
		}
		return true;
	}
	
    // Glid or List アイコンの切り替え
	private void setListTypeIcon(int type){
		switch(type){
		case BaseFragment.TYPE_GRID:
			getMenuItem(MENU_ID_DISPLAY).setIcon(R.drawable.ic_action_view_as_grid);
			break;
		case BaseFragment.TYPE_LIST:
			getMenuItem(MENU_ID_DISPLAY).setIcon(R.drawable.ic_action_view_as_list);
			break;
		}
	}
	
	//----------------------------------------------------------------------
    
    @Subscribe
	// 検索フラグメントへの遷移
	public void onSearchItem(SearchPreEvent event){
    	SearchParams searchParams = event.getSearchParams();
		searchParams.setZaiko(isZaikoCheck);
		EventHolder.doSearchItem(searchParams);
    	if(!searchParams.getSearchString().isEmpty()){
    		mSearchView.setQuery(searchParams.getSearchString(), false);
    	}
		if(isZaikoCheck){
			getMenuItem(MENU_ID_INVENTORY).setIcon(R.drawable.ic_action_location_found);
		}
		else{
			getMenuItem(MENU_ID_INVENTORY).setIcon(R.drawable.ic_action_location_searching);
		}
    	mSearchView.clearFocus();
    	mViewPager.getCurrentFragment().requestListViewFocus();
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	
	@Subscribe
	public void onNetworkError(NetworkErrorEvent event){
		super.onNetworkError(event);
	}
	
	@Subscribe
	// ページがスワイプしたら
	public final void onTabChanged(TabChangedEvent event) {
		
		int pos = event.getPosition();
		Log.d(this.getClass().getSimpleName(),"onTabChanged="+pos);
		
		BaseFragment fragment = ((HomeAdapter)mViewPager.getAdapter()).getItem(pos);
		getMenuItem(MENU_ID_DELETE).setVisible(fragment.isDeletable());
		setListTypeIcon(fragment.getType());
		
		getMenuItem(MENU_ID_INVENTORY).setVisible((fragment instanceof SearchFragment));
		
		if(getSupportActionBar().getSelectedNavigationIndex() != pos){
			getSupportActionBar().setSelectedNavigationItem(pos);
		}
	}
	
	@Subscribe
	public final void onShowItemDetail(ShowItemDetailEvent event){
		App.setCurrentAdapter(mViewPager.getCurrentFragment().getAdapter());

		Intent intent = new Intent(getApplicationContext(),ItemActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("item",event.getmItem());
		startActivity(intent);
	}
}
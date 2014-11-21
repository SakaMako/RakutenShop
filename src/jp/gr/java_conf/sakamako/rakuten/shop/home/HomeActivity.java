package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.MakeToastEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.SearchPreEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.ShowItemDetailEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.TabChangedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import jp.gr.java_conf.sakamako.rakuten.shop.setting.SettingActivity;
import jp.gr.java_conf.sakamako.view.CustomSearchView;
import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
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

@SuppressLint("NewApi")
public class HomeActivity<mViewPager> extends BaseActivity 
	{
	private static final int MENU_ID_DISPLAY = 6;
	private static final int MENU_ID_INVENTORY = 8;
	private static final int MENU_ID_SETTING = 9;
	private static final int INTENT_START_SETTING = 10001;
	
	private DrawerLayout mDrawerLayout = null;
	private ListView mDrawerList = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private CustomSearchView mSearchView = null;
	private HomeViewPager mViewPager = null;
	private boolean isZaikoCheck = false;
	
	public static BaseItemAdapter itemAdapter = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		
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
    	SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo searchableInfo = searchManager.getSearchableInfo(getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);
    }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    
		Log.d(this.getClass().getSimpleName(),"onActivityResult");
		if(data == null) return;
	    Bundle bundle = data.getExtras();
	    switch (requestCode) {
	    	case INTENT_START_SETTING:
	    		Log.d(this.getClass().getSimpleName(),"INTENT_START_SETTING");

	    		if (resultCode == SettingActivity.RESULT_ADD) {
		    		Log.d(this.getClass().getSimpleName(),"RESULT_ADD");

	    			((HomeAdapter)mViewPager.getAdapter()).addCategory(bundle.getString("cat"));
	    	    }
	    		else if(resultCode == SettingActivity.RESULT_DELETE){
		    		Log.d(this.getClass().getSimpleName(),"RESULT_DELETE");

	    			((HomeAdapter)mViewPager.getAdapter()).deleteCategory(bundle.getString("cat"));
	    		}
	    		else if(resultCode == SettingActivity.RESULT_MOVE){
		    		Log.d(this.getClass().getSimpleName(),"RESULT_MOVE");
		    		
		    		ArrayList<String> list = (ArrayList<String>) bundle.getSerializable("list");
	    			((HomeAdapter)mViewPager.getAdapter()).moveCategory(list);
	    		}
	    		else if(resultCode == RESULT_CANCELED){
		    		Log.d(this.getClass().getSimpleName(),"RESULT_CANCELED");

	    		}
	    		break;
	    	default:
	    		break;
	    }
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
		addMenu(menu,MENU_ID_DISPLAY,"切り替え",R.drawable.ic_action_view_as_grid);
    	addMenu(menu,MENU_ID_INVENTORY,"在庫なし含む",R.drawable.ic_action_location_searching);
    	addMenu(menu,MENU_ID_SETTING,"設定",R.drawable.ic_action_settings);
    	
    	// 最初の１回だけでは強制的に０にしてメニューを初期化する
    	// Otto への register はこの後の onResume なので強制的にイベントを作成して呼び出す
    	// SelectedNavigationIndex() への設定は onPostCreate() で実装済み
    	onTabChanged(new TabChangedEvent(getSupportActionBar().getSelectedNavigationIndex()));

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
	    
		mViewPager  = (HomeViewPager) findViewById(R.id.viewpager);
		int init_tab_pos = 0;
		if(savedInstanceState!=null){
			init_tab_pos = savedInstanceState.getInt("TAB_NUMBER");
			Log.d(this.getClass().getSimpleName(),"savedInstanceState="+init_tab_pos);    
		}
		getSupportActionBar().setSelectedNavigationItem(init_tab_pos);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		
		Log.d(this.getClass().getSimpleName(),"onResume");    

		if(mDrawerList == null){
        	// DrawerList
        	mDrawerList = (ListView) findViewById(R.id.left_drawer);
        	DrawerMenuAdapter adapter = new DrawerMenuAdapter();
        	mDrawerList.setAdapter(adapter);
        	mDrawerList.setOnItemClickListener(adapter);
        }
        
        EventHolder.register(mViewPager.getAdapter());
        
        int pos = mViewPager.getCurrentItem();
        Log.d(this.getClass().getSimpleName(),"mViewPager.current="+pos);
	}
	
	@Override
	public void onPause() {
	    EventHolder.unregister(mViewPager.getAdapter());
	    super.onPause();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    Log.d(this.getClass().getSimpleName(),"onSaveInstanceState:pos="+mViewPager.getCurrentItem() );
	    outState.putInt("TAB_NUMBER", mViewPager.getCurrentItem() );
	}
	
	//----------------------------------------------------------------------

	@Override
	// もしバックボタンを押されても左端で無ければ一旦左端に戻る
	public boolean dispatchKeyEvent(KeyEvent event) {
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
		case MENU_ID_SETTING:
			Intent intent = new Intent(getApplicationContext(),SettingActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivityForResult(intent,INTENT_START_SETTING);
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
			getMenuItem(MENU_ID_DISPLAY).setIcon(android.R.drawable.ic_dialog_dialer);
			break;
		case BaseFragment.TYPE_GRID2:
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
		mDrawerLayout.closeDrawer(mDrawerList);
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
	}
	
	@Subscribe
	public void makeToast(MakeToastEvent event){
		super.makeToast(event);
	}
	
	@Subscribe
	// ページがスワイプしたら
	public final void onTabChanged(TabChangedEvent event) {
		
		int pos = event.getPosition();
		Log.d(this.getClass().getSimpleName(),"onTabChanged="+pos);
		
		BaseFragment fragment = ((HomeAdapter)mViewPager.getAdapter()).getItem(pos);
		setListTypeIcon(fragment.getType());
		
		getMenuItem(MENU_ID_INVENTORY).setVisible((fragment instanceof SearchFragment));
		
		if(getSupportActionBar().getSelectedNavigationIndex() != pos){
			getSupportActionBar().setSelectedNavigationItem(pos);
		}
	}
	
	@Subscribe
	public final void onShowItemDetail(ShowItemDetailEvent event){
		Intent intent = new Intent(getApplicationContext(),ItemActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("item",event.getmItem());
		itemAdapter = mViewPager.getCurrentFragment().getAdapter();
		startActivity(intent);
	}
}
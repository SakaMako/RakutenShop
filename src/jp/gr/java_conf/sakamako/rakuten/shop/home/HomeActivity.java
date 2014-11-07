package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Collection;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.DeleteCategoryDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.NewCategoryDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.event.BusHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import jp.gr.java_conf.sakamako.view.CustomSearchView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 共通の Activity クラス
 * @author makoto.sakamotoß
 * @param <mViewPager>
 *
 */


@SuppressLint("NewApi")
public class HomeActivity<mViewPager> extends BaseActivity 
	implements  OnItemClickListener
	,OnQueryTextListener
	//,OnCheckedChangeListener
	, OnFocusChangeListener
	,OnPageChangeListener
	{
	
	protected static final int MENU_ID_ADD = 1;
	protected static final int MENU_ID_DELETE = 2;
	protected static final int MENU_ID_DISPLAY = 6;
	protected static final int MENU_ID_INVENTORY = 8;
	
	private DrawerLayout mDrawerLayout = null;
	private ListView mDrawerList = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private HomeAdapter mHomeAdapter;
	private CustomSearchView mSearchView = null;
	
	@Override
	protected final boolean isActionBar(){
		return true;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		if(mHomeAdapter == null){
			 mHomeAdapter = new HomeAdapter(
					 this,(ViewPager) findViewById(R.id.viewpager)
					 );
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		Log.d("TopActivity","onCreate Start");    
		setContentView(R.layout.home);    
		
	    ActionBar actionBar = getSupportActionBar();

		actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM);
        View actionBarView = getLayoutInflater().inflate(R.layout.custom_action_bar_top, null);
        actionBar.setCustomView(actionBarView);
        mSearchView = (CustomSearchView) actionBarView.findViewById(R.id.action_bar_search);
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnFocusChangeListener(this);
        
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
        
        actionBar.show();
		/**
		 mHomeAdapter = new HomeAdapter(
				 this,(ViewPager) findViewById(R.id.viewpager)
				 );
		*/
	     // DrawerLayout
	     mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
	     mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
	     mDrawerToggle = new ActionBarDrawerToggle(
	                this,
	                mDrawerLayout,
	                R.drawable.ic_drawer,
	                R.string.open,
	                R.string.close
	     );
	     mDrawerToggle.setDrawerIndicatorEnabled(true);
	     mDrawerLayout.setDrawerListener(mDrawerToggle);
	     
	     // DrawerList
	     mDrawerList = (ListView) findViewById(R.id.left_drawer);
	     DrawerMenuAdapter adapter = new DrawerMenuAdapter(this);
	     mDrawerList.setAdapter(adapter);
	     mDrawerList.setOnItemClickListener(this);
	     
	}
	
	public void showItemDetail(Item item){
		App.setCurrentAdapter(mHomeAdapter.getCurrentFragment().getAdapter());
		super.showItemDetail(item);
	}

	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	    mDrawerToggle .syncState();
	}
	
	@Override
	// もしバックボタンを押されても左端で無ければ一旦左端に戻る
	public boolean dispatchKeyEvent(KeyEvent event) {
	    if (event.getAction()==KeyEvent.ACTION_DOWN) {
	        switch (event.getKeyCode()) {
	        case KeyEvent.KEYCODE_BACK:
	        	if(mHomeAdapter.getCurrentPosition() != 0){
	        		mHomeAdapter.setCurrentPosition(0);
	        		return false;
	        	}
	        }
	    }
	    return super.dispatchKeyEvent(event);
	}
	
	/** アプリケーションアイコン押下時の動作 */
	public boolean onOptionsItemSelected(MenuItem item){
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
			showDialog("カテゴリの追加",new NewCategoryDialog(mHomeAdapter));
            break;
		case MENU_ID_DELETE:
			int pos = mHomeAdapter.getCurrentPosition();
			MyItemFragment fragment = (MyItemFragment) mHomeAdapter.getItem(pos);
			showDialog("カテゴリの削除",
					new DeleteCategoryDialog(fragment.getCategory(),mHomeAdapter)
			);
			break;
		case MENU_ID_DISPLAY:
			int type = mHomeAdapter.replace();
			setListTypeIcon(type);
			break;
		case MENU_ID_INVENTORY:
			if(isZaikoCheck){
				isZaikoCheck = false;
			}
			else{
				isZaikoCheck = true;
			}
			onQueryTextSubmit(null);
			break;
		default:
			break;
		}
		return true;
	}
	
	private boolean isZaikoCheck = false;
	
    @SuppressWarnings("static-access")
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	addMenu(menu,MENU_ID_DISPLAY,"切り替え",R.drawable.ic_action_view_as_grid);
    	addMenu(menu,MENU_ID_INVENTORY,"在庫なし含む",R.drawable.ic_action_location_searching);
    	//addMenu(menu,MENU_ID_REFRESH,"再読み込み",R.drawable.ic_action_refresh);
    	addMenu(menu,MENU_ID_DELETE,"カテゴリ削除",R.drawable.ic_action_discard);
    	addMenu(menu,MENU_ID_ADD,"追加",R.drawable.ic_action_new_label);
 
    	onPageSelected(0);
    	return true;
    }
    
	// 検索フラグメントへの遷移
	public void search(SearchParams searchParams){
		searchParams.setZaiko(isZaikoCheck);

		mHomeAdapter.search(searchParams);
    	//mViewPager.setCurrentItem(0);
    	if(!searchParams.getSearchString().isEmpty()){
    		// 検索ワードを検索窓にセット
    		mSearchView.setQuery(searchParams.getSearchString(), false);
    	}
    	// 在庫条件をセット
    	//mZaikoSwitch.setChecked(searchParams.isZaiko());
    	
		if(isZaikoCheck){
			getMenuItem(MENU_ID_INVENTORY).setIcon(R.drawable.ic_action_location_found);
		}
		else{
			getMenuItem(MENU_ID_INVENTORY).setIcon(R.drawable.ic_action_location_searching);
		}
    	mSearchView.clearFocus();
    	mHomeAdapter.getCurrentFragment().requestListViewFocus();
    	
		//mTabFragmentAdapter.search(searchParams);
	}
    
	@Override
	// ドロワーメニューのアイテムがクリックされたら
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		String search = (String) parent.getAdapter().getItem(position);
		search(new SearchParams(search));
        //mTabFragmentAdapter.setCurrentPosition(0);
		mDrawerLayout.closeDrawer(mDrawerList);
	}
	
	@Override
	public boolean onQueryTextChange(String arg0) {
		// ×印でクリアされたら検索も初期化
		if(arg0.isEmpty()){
			search(new SearchParams(""));
		}
		
		// 何もしない
		return true;
	}
	
	@Override
	// 検索がサブミットされたら
	public boolean onQueryTextSubmit(String arg0) {
		String searchKeyword = mSearchView.getQuery().toString();
		SearchParams searchParams = new SearchParams(searchKeyword);

		//searchParams.setZaiko(mZaikoSwitch.isChecked());
		search(searchParams);
	
		return true;
	}
	
	/**
	@Override
	// 在庫チェックが呼ばれたら
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		onQueryTextSubmit(null);
	}
	*/
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}
	
	@Override
	// 各フラグメントが選択されたら
	public final void onPageSelected(int pos) {
		
		BaseFragment fragment = mHomeAdapter.getItem(pos);
		
		//getMenuItem(MENU_ID_REFRESH).setVisible(fragment.isReloadble());
		getMenuItem(MENU_ID_DELETE).setVisible(fragment.isDeletable());
		setListTypeIcon(fragment.getType());
		
		getMenuItem(MENU_ID_INVENTORY).setVisible((fragment instanceof SearchFragment));
		
		getSupportActionBar().setSelectedNavigationItem(pos);
	}
	
	
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

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
        // EditTextのフォーカスが外れた場合
        if (hasFocus == false) {
            // ソフトキーボードを非表示にする
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
	}
	
	@Subscribe
	public void onNetworkError(NetworkErrorEvent event){
		super.onNetworkError(event);
	}
	

}
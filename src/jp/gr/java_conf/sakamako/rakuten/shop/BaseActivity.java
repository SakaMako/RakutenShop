package jp.gr.java_conf.sakamako.rakuten.shop;

import java.util.HashMap;
import java.util.Locale;




//import jp.gr.java_conf.noappnolife.rakuami2.R;







import jp.gr.java_conf.sakamako.rakuten.shop.dialog.NewCategoryDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.event.BusHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseFragment;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
//import jp.gr.java_conf.noappnolife.rakuami2.nouse.ItemDetailActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends ActionBarActivity {
	

	private HashMap<Integer,MenuItem> menuHash = new HashMap<Integer,MenuItem>();
	
	@Override
	public void onResume() {
	    super.onResume();
	    BusHolder.get().register(this);
	}

	@Override
	public void onPause() {
	    BusHolder.get().unregister(this);
	    super.onPause();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		Locale.setDefault(Locale.JAPAN);		
		//ソフトKBDを表示させない
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
				| WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
 	    //アクションバーメニューを表示する
		if(isActionBar()){
			getWindow().requestFeature(Window.FEATURE_ACTION_BAR);	
		}
	}
	
	protected abstract boolean isActionBar();
	
	protected final void addMenu(Menu menu,int id,String text,int icon){
   	    // メニューの要素を追加して取得
	    MenuItem actionItem = menu.add(0, id, 0, text);
	    // SHOW_AS_ACTION_IF_ROOM:余裕があれば表示
	    actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	    // アイコンを設定
	    actionItem.setIcon(icon);
	    
	    menuHash.put(new Integer(id),actionItem);
	}
	
	protected final MenuItem getMenuItem(int id){
		return menuHash.get(new Integer(id));
	}
		
	public void showItemDetail(Item item){
		Intent intent = new Intent(getApplicationContext(),ItemActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("item",item);
		startActivity(intent);
	}
	
	// ダイアログの表示
	public final void showDialog(String title,DialogFragment dialog){
        FragmentManager manager = getSupportFragmentManager();
        dialog.show(manager, title);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			finish();
			break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}


}

package jp.gr.java_conf.sakamako.rakuten.shop;

import java.util.HashMap;
import java.util.Locale;
import com.squareup.otto.Subscribe;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
//import jp.gr.java_conf.noappnolife.rakuami2.nouse.ItemDetailActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public abstract class BaseActivity extends ActionBarActivity {
	

	private HashMap<Integer,MenuItem> menuHash = new HashMap<Integer,MenuItem>();
	
	@Override
	public void onPause() {
		EventHolder.unregister(this);
	    super.onPause();
	}
	
	public void onResume(){
		super.onResume();
		EventHolder.register(this);
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
	
	@Subscribe
	public void onNetworkError(NetworkErrorEvent event){
		Log.d(this.getClass().getSimpleName(),"onNetworkError");
		Toast.makeText(this, "ネットワークに接続できません", Toast.LENGTH_SHORT).show();
	}
}

package jp.gr.java_conf.sakamako.rakuten.shop.setting;

import com.squareup.otto.Subscribe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.MakeToastEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import jp.gr.java_conf.sakamako.rakuten.shop.setting.DeleteCategoryDialog.DeleteCategoryListener;
import jp.gr.java_conf.sakamako.rakuten.shop.setting.NewCategoryDialog.OnNewCategoryListener;

public class SettingActivity extends BaseActivity
implements OnClickListener,OnNewCategoryListener,OnItemClickListener,DeleteCategoryListener{
	
	public static final int RESULT_ADD = 90001;
	public static final int RESULT_DELETE = 90002;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.setting);
		
		LinearLayout layout = (LinearLayout)findViewById(R.id.add_category_layout);
		layout.setOnClickListener(this);
		
		getActionBar().setTitle("設定(カテゴリの追加・編集・削除)");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Log.d(this.getClass().getSimpleName(),"onCreate end-----------------------");
	}
	
	@Override
	public void onClick(View v) {
		showDialog("カテゴリの追加",new NewCategoryDialog(this));
	}
	
 	//カテゴリの追加
	@Override
	public boolean onNewCategory(String text) {	
		Category cat = MyCategory.getInstance().getCategory(text);
		if(cat != null){
			return false;
		}
		
	    Intent intent = new Intent();
	    Bundle bundle = new Bundle();
	    bundle.putString("cat", text);
	    intent.putExtras(bundle);
	    setResult(RESULT_ADD, intent);
	    
	    finish();
		return true;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3) {
		String name = (String) parent.getAdapter().getItem(pos);
		Category cat = MyCategory.getInstance().getCategory(name);
		showDialog("カテゴリの削除",new DeleteCategoryDialog(cat,this));
	}

	@Override
	// カテゴリの削除
	public void onDeleteCategory(Category cat) {
		Log.d(this.getClass().getSimpleName(),"onDeleteCategory="+cat.getLabel());
	    Intent intent = new Intent();
	    Bundle bundle = new Bundle();
	    bundle.putString("cat",cat.getLabel());
	    intent.putExtras(bundle);
	    setResult(RESULT_DELETE, intent);
	    
	    finish();
	}

	public void changeCategory(int from, int to) {
		// TODO Auto-generated method stub
	}

	@Subscribe
	public void makeToast(MakeToastEvent event){
		super.makeToast(event);
	}





}

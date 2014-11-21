package jp.gr.java_conf.sakamako.rakuten.shop.setting;

import java.util.ArrayList;
import java.util.List;

import com.squareup.otto.Subscribe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
implements OnNewCategoryListener,OnItemClickListener,DeleteCategoryListener{
	
	public static final int RESULT_ADD = 90001;
	public static final int RESULT_DELETE = 90002;
	public static final int RESULT_MOVE = 900003;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.setting);
		
		getActionBar().setTitle("ブックマークの追加・編集・削除");
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		final SettingActivity activity = this;
		
		Button addButton = (Button)findViewById(R.id.add);
		addButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showDialog("カテゴリの追加",new NewCategoryDialog(activity));
			}
			
		});
		
		Button okButton = (Button)findViewById(R.id.ok);
		okButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				CategoryListView view = (CategoryListView)findViewById(R.id.categoryList);
				ArrayAdapter<String> adapter = (ArrayAdapter<String>) view.getAdapter();
				ArrayList<String> list = new ArrayList<String>();

				for(int i=0;i<adapter.getCount();i++){
					list.add(adapter.getItem(i));
				}
				
			    Intent intent = new Intent();
			    Bundle bundle = new Bundle();
			    bundle.putSerializable("list",list);
			    intent.putExtras(bundle);
			    
			    setResult(RESULT_MOVE, intent);
			    finish();
			}
			
		});
		Button cancelButton = (Button)findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent();
			    setResult(RESULT_CANCELED, intent);
			    finish();
			}
			
		});

		Log.d(this.getClass().getSimpleName(),"onCreate end-----------------------");
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

	@Subscribe
	public void makeToast(MakeToastEvent event){
		super.makeToast(event);
	}
}

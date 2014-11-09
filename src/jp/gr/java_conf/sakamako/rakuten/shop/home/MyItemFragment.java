package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import jp.gr.java_conf.sakamako.view.SortableListView.DragListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyItemFragment extends BaseFragment 
implements DragListener
	{
	private Category mCat = null;
	
	public MyItemFragment() {
		super(TYPE_GRID);
	}
	
	public MyItemFragment(Category cat) {
		this(TYPE_GRID,cat,null);
	}

	private MyItemFragment(int type,Category cat,BaseItemAdapter adapter) {
		super(type);
		mCat = cat;
		super.setAdapter(adapter);
		Log.d("MyItemFragment",mCat.getLabel()+"の作成");
		Log.d("MyItemFragment","type="+type+","+this.getType());
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		if(mCat == null){
			Log.d(this.getClass().getSimpleName(),"mCatが無い");
			if(state != null){
				Log.d(this.getClass().getSimpleName(),"Bundleがある");
				String categoryName = state.getString("cat");
				mCat = MyCategory.getInstance().getCategory(categoryName);
			}
		}
		super.setAdapter(new MyItemAdapter(this,R.layout.home_list_item,mCat));
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"SavedInstanceState");
		state.putString("cat",mCat.getLabel());
	
	}
	
	public Category getCategory(){
		return mCat;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		// リスナーから削除しないと積み重なって呼ばれ続ける
		mCat.getItemList().removeOnChangedListener((MyItemAdapter)getAdapter());
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		mCat.getItemList().addOnChangedListener((MyItemAdapter)getAdapter());
	}

	//---------------------------------------------------------------
	//メニューの切り替え
	@Override
	public boolean isDeletable() {
		return true;
	}

	@Override
	public String getTabTitle() {
		return mCat.getLabel();
	}
	
	//---------------------------------------------------------------
	@Override
	public final boolean onStopDrag(int positionFrom, int positionTo) {
		Log.d("MyItemFragment","onStopDrag = " + positionFrom + " -> " + positionTo);
		if(positionFrom < 0)return false;
		mCat.getItemList().move(positionFrom,positionTo);
		return true;
	}
	@Override
	public final int onStartDrag(int position) {
		return position;
	}

	@Override
	public final int onDuringDrag(int positionFrom, int positionTo) {
		return positionFrom;
	}
	
	//---------------------------------------------------------------
	@Subscribe
	public void onFinishReload(FinishReloadEvent event){
		super.onFinishReload(event);
	}
}

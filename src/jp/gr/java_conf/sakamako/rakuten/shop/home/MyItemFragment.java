package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.LinkedHashMap;
import java.util.Map;
import com.squareup.otto.Subscribe;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import jp.gr.java_conf.sakamako.view.SortableListView.DragListener;
import android.os.Bundle;
import android.util.Log;

public class MyItemFragment extends BaseFragment 
implements DragListener
	{
	private static Map<Category,MyItemFragment> fragmentList = null;
	private Category mCat = null;
	
	public static MyItemFragment getInstance(Category cat){
		if(fragmentList == null){
			fragmentList = new LinkedHashMap<Category,MyItemFragment>();
		}
		MyItemFragment fragment = fragmentList.get(cat);
		if(fragment == null){
			fragment = new MyItemFragment();
			fragment.setCategory(cat);
			fragmentList.put(cat, fragment);
		}
		return fragment;
		
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
		super.setAdapter(new MyItemAdapter(this,mCat));
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"onSavedInstanceState");
		state.putString("cat",mCat.getLabel());
	}
	
	public void setCategory(Category cat){
		mCat = cat;
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
		Log.d(this.getClass().getSimpleName(),"onStopDrag = " + positionFrom + " -> " + positionTo);
		if(positionFrom < 0)return false;
		mCat.getItemList().move(positionFrom,positionTo);
		super.setSwipeEnabled(true);

		return true;
	}
	@Override
	public final int onStartDrag(int position) {
		super.setSwipeEnabled(false);
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

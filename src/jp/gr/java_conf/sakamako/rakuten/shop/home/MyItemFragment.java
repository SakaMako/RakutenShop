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
		Log.d(MyItemFragment.class.getSimpleName(),"フラグメントの取得="+cat.getLabel());

		MyItemFragment fragment = getInstanceFromList(cat);
		if(fragment == null){
			Log.d(MyItemFragment.class.getSimpleName(),"フラグメントの作成="+cat.getLabel());

			fragment = new MyItemFragment();
            Bundle args = new Bundle();  
            args.putString("cat", cat.getLabel());
            fragment.setArguments(args);
			fragmentList.put(cat, fragment);
		}
		return fragment;
	}
	
	public static void removeInstance(Category cat){
		fragmentList.remove(cat);
	}
	
	private static MyItemFragment getInstanceFromList(Category cat){
		if(fragmentList == null){
			fragmentList = new LinkedHashMap<Category,MyItemFragment>();
		}
		return fragmentList.get(cat);
	}
	
	public MyItemFragment(){
		super();
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
			else{
				mCat = getCategory();
			}
		}
		MyItemFragment fragment = getInstanceFromList(mCat);
		if(fragment == null){
			fragmentList.put(mCat, this);
		}
		
		super.setAdapter(new MyItemAdapter(this,mCat));
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"onSavedInstanceState");
		state.putString("cat",mCat.getLabel());
	}
	
	public Category getCategory(){
		if(mCat == null){
			Bundle bundle = getArguments();
			String categoryName = bundle.getString("cat");
			return MyCategory.getInstance().getCategory(categoryName);
		}
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

	@Override
	public String getTabTitle() {
		return getCategory().getLabel();
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

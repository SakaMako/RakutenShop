package jp.gr.java_conf.sakamako.rakuten.shop.home;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseFragment.Dragable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MyItemFragment extends BaseFragment 
implements Dragable
	{
	private Category mCat = null;
	
	public MyItemFragment() {
	}
	
	public MyItemFragment(Category cat) {
		this(TYPE_GRID,cat,null);
	}

	private MyItemFragment(int type,Category cat,BaseItemAdapter adapter) {
		super(type);
		mCat = cat;
		mAdapter = adapter;
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
		mAdapter = new MyItemAdapter(this,R.layout.home_list_item,mCat);
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
	public MyItemFragment replace() {
		int type = super.getReverseType();
		MyItemFragment fragment = new MyItemFragment(type,mCat,mAdapter);
		//fragment.setInitPosition(this.getVisiblePosition());
		return fragment;
	}
	

	
	@Override
	public void onDestroy(){
		super.onDestroy();
		// リスナーから削除しないと積み重なって呼ばれ続ける
		mCat.getItemList().removeOnChangedListener((MyItemAdapter)mAdapter);
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		mCat.getItemList().addOnChangedListener((MyItemAdapter)mAdapter);
	}

	@Override
	public final boolean onStopDrag(int positionFrom, int positionTo) {
		Log.d("MyItemFragment","onStopDrag = " + positionFrom + " -> " + positionTo);
		if(positionFrom < 0)return false;
		mCat.getItemList().move(positionFrom,positionTo);
		return true;
	}

	//---------------------------------------------------------------
	//メニューの切り替え
	@Override
	public boolean isDeletable() {
		return true;
	}
}

package jp.gr.java_conf.sakamako.rakuten.shop.home;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

public abstract class BaseFragment extends Fragment
	{
	
	//--------------------------------------------------------------------
	//public abstract BaseFragment replace() ;
	public abstract boolean isDeletable();
	public abstract String getTabTitle(); 

	//--------------------------------------------------------------------
	public static final int TYPE_GRID = 1;
	public static final int TYPE_LIST = 2;
	private int mType = -1;
	private AbsListView mView = null;
	private BaseItemAdapter mAdapter = null;
	private int mInitPosition = -1;
	private SwipeRefreshLayout mSwipeRefreshLayout =null;
	//--------------------------------------------------------------------
	
	public BaseFragment(int type) {
		super();
		mType = type;
	}
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		// Type の復活
		if(mType == -1 && state != null){
			mType = state.getInt("type");
		}
	}
	
	@Override
	public final View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
		Log.d(this.getClass().getSimpleName(),"onCreateView");
		View v = null;
		switch(this.getType()){
		case TYPE_GRID:
				Log.d(this.getClass().getSimpleName(),"onCreateViewByGrid");
				v = inflater.inflate(R.layout.home_grid, container, false);
				break;
		case TYPE_LIST:
				Log.d(this.getClass().getSimpleName(),"onCreateViewByList");
				v = inflater.inflate(R.layout.home_list, container, false);	
				break;
		default:
				break;
		}
	    mView = (AbsListView)v.findViewById(R.id.list);

		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_swipe);

		
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		((AbsListView)mView).setAdapter(mAdapter);        
	    ((BaseListView)mView).onAttachedFragment(this);
        
	    // Type が変わった場合に備えて入れ替え
        //mAdapter.resetFragment(this);
        
    	Log.d(this.getClass().getSimpleName(),"initPosition="+mInitPosition);
		if(mInitPosition >= 0){
			this.setSelection(mInitPosition);
		}
		
		if(mAdapter instanceof ReloadbleListener){
			mSwipeRefreshLayout.setOnRefreshListener((ReloadbleListener)mAdapter);
			mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);
		}
		else{
			mSwipeRefreshLayout.setEnabled(false);
		}
		Log.d(this.getClass().getSimpleName(),"end-onActivityCreated");
	}
	
	@Override
	public void onResume(){
		super.onResume();
	    EventHolder.register(this);
	    //EventHolder.register(mView);
	}
	
	@Override
	public void onPause(){
	    //EventHolder.unregister(mView);
	    EventHolder.unregister(this);
		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"SavedInstanceState");
		state.putInt("type",mType);
	
	}
	//--------------------------------------------------------------------
	public void setType(int type) {
		mType = type;
	}
	
	public final int getType() {
		return mType;
	}
	
	public BaseListView getListView() {
		return (BaseListView) mView;
	}

	public final BaseItemAdapter getAdapter() {
		return mAdapter;
	}
	protected final void setAdapter(BaseItemAdapter adapter) {
		mAdapter = adapter;
		
	}
	//---------------------------------------------------------------------
	protected final int getReverseType(){
		int type = mType;
		switch(mType){
		case TYPE_GRID:
			type = TYPE_LIST;
			break;
		case TYPE_LIST:
			type = TYPE_GRID;
			break;
		}
		Log.d(this.getClass().getSimpleName(),mType + "->" + type);
		return type;
	}
	
	public final int getVisiblePosition(){
		return mView.getFirstVisiblePosition();
	}

	public final void setInitPosition(int vpos) {
		mInitPosition = vpos;
	}
	
	// ItemVerticalPager 用、後で戻ってきたときに問題無いようにスクロールしておく
	public final void setSelection(int pos){
		if(mView != null){
			mView.setSelection(pos);
		}
	}
	
	public final void requestListViewFocus() {
		mView.requestFocus();
	}
	
	protected void onFinishReload(FinishReloadEvent event){
		this.setInitPosition(0);
		mSwipeRefreshLayout.setRefreshing(false);
	}


}

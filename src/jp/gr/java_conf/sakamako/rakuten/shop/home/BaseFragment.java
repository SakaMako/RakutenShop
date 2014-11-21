package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.Countable;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment	{
	
	//--------------------------------------------------------------------
	public abstract String getTabTitle(); 
	public abstract int getTabIcon();
	//--------------------------------------------------------------------
	public static final int TYPE_GRID = 1;
	public static final int TYPE_GRID2 = 2;
	public static final int TYPE_LIST = 3;

	private int mType = TYPE_GRID;
	private AbsListView mView = null;
	private BaseItemAdapter mAdapter = null;
//	private int mInitPosition = -1;
	private SwipeRefreshLayout mSwipeRefreshLayout =null;
	private TextView mCntView = null;
	//--------------------------------------------------------------------
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		// Type の復活
		if(state != null){
			mType = state.getInt("type");
		}
	}
	
	@Override
	public final View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
		Log.d(this.getClass().getSimpleName(),"onCreateView");
		View v = null;
		switch(this.getType()){
		case TYPE_GRID:
				Log.d(this.getClass().getSimpleName(),"TYPE_GRID");
				v = inflater.inflate(R.layout.home_grid, container, false);
				break;
		case TYPE_LIST:
				Log.d(this.getClass().getSimpleName(),"TYPE_LIST");
				v = inflater.inflate(R.layout.home_list, container, false);	
				break;
		case TYPE_GRID2:
			Log.d(this.getClass().getSimpleName(),"TYPE_GRID2");
			v = inflater.inflate(R.layout.home_grid2, container, false);	
			break;
		default:
				break;
		}
	    mView = (AbsListView)v.findViewById(R.id.list);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_swipe);
		mCntView = (TextView)v.findViewById(R.id.cnt);
		
		return v;
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		((AbsListView)mView).setAdapter(mAdapter);        
	    ((BaseListView)mView).onAttachedFragment(this);
		
		if(mAdapter instanceof ReloadbleListener){
			mSwipeRefreshLayout.setOnRefreshListener((ReloadbleListener)mAdapter);
			mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);
		}
		else{
			mSwipeRefreshLayout.setEnabled(false);
		}
		
		if(mCntView != null){
			if(mAdapter instanceof Countable){
				mCntView.setVisibility(View.VISIBLE);
			}
			else{
				mCntView.setVisibility(View.INVISIBLE);
			}
		}
		
		
		Log.d(this.getClass().getSimpleName(),"end-onActivityCreated");
	}
	
	@Override
	public void onResume(){
		super.onResume();
	    EventHolder.register(this);
	    EventHolder.register(this.getAdapter());
	}
	
	@Override
	public void onPause(){
	    EventHolder.unregister(this.getAdapter());
	    EventHolder.unregister(this);
		super.onPause();
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"super.onSavedInstanceState");
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
	
	protected final void setSwipeEnabled(boolean b){
		mSwipeRefreshLayout.setEnabled(b);
	}
	
	//---------------------------------------------------------------------
	protected final int getReverseType(){
		int type = mType;
		type++;
		if(type > 3){
			type=1;
		}
		Log.d(this.getClass().getSimpleName(),mType + "->" + type);
		return type;
	}
	
	public final int getVisiblePosition(){
		return mView.getFirstVisiblePosition();
	}

	//private final void setInitPosition(int vpos) {
	//	mInitPosition = vpos;
	//}
	
	// ItemVerticalPager 用、後で戻ってきたときに問題無いようにスクロールしておく
	public final void setSelection(int pos){
		if(mView != null){
			mView.setSelection(pos);
		}
	}
	
	@Subscribe
	protected void onFinishReload(FinishReloadEvent event){
		//this.setInitPosition(0);
		this.setSelection(0);
		mSwipeRefreshLayout.setRefreshing(false);
	}
	
	public void updateCount(int lastVisiblePosition, int allCount) {
		
		Log.d(this.getClass().getSimpleName(),"updateCount="+lastVisiblePosition+"/"+allCount);
		
		String last = String.format("%1$,5d", lastVisiblePosition);
		String all = String.format("%1$,5d", allCount);

		if(mCntView != null ){
			mCntView.setText(last + "/" + all);
		}
	}

}

package jp.gr.java_conf.sakamako.rakuten.shop.home;


import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadableAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.view.SortableListView;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

public abstract class BaseFragment extends Fragment
	implements OnItemClickListener, OnScrollListener,SortableListView.DragListener
	{
	
	//--------------------------------------------------------------------
	
	public interface Dragable {
		public boolean onStopDrag(int positionFrom, int positionTo);
	}
	public interface Scrollable {
		public void readNext(int arg0);
	}
	public abstract BaseFragment replace() ;
	public abstract boolean isDeletable();
	public abstract String getTabTitle(); 

	//--------------------------------------------------------------------
	public static final int TYPE_GRID = 1;
	public static final int TYPE_LIST = 2;
	protected int mType = -1;
	protected AbsListView mView = null;
	protected BaseItemAdapter mAdapter = null;
	private int mInitPosition = -1;
	private SwipeRefreshLayout mSwipeRefreshLayout =null;
	//--------------------------------------------------------------------
	public BaseFragment(){
		
	}
	
	public BaseFragment(int type) {
		super();
		mType = type;
	}
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		if(mType == -1){
			if(state != null){
				mType = state.getInt("type");
			}
		}
	}
	
	@Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		((AbsListView)mView).setAdapter(mAdapter);
        ((AbsListView)mView).setOnItemClickListener(this);
        
        mAdapter.resetFragment(this);
        
    	Log.d(this.getClass().getSimpleName(),"initPosition="+mInitPosition);
		if(mInitPosition >= 0){
			this.setSelection(mInitPosition);
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"SavedInstanceState");
		state.putInt("type",mType);
	
	}
	//--------------------------------------------------------------------
	
	public int getType() {
		return mType;
	}

	public BaseItemAdapter getAdapter() {
		return mAdapter;
	}
	
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
	

	
	@Override
	public final View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
		Log.d(this.getClass().getSimpleName(),"onCreateView");
		View v = null;
		switch(this.getType()){
		case TYPE_GRID:
				Log.d(this.getClass().getSimpleName(),"onCreateViewByGrid");
				v = onCreateViewByGrid(inflater, container, saveInstanceState);
				break;
		case TYPE_LIST:
				Log.d(this.getClass().getSimpleName(),"onCreateViewByList");
				v =  onCreateViewByList(inflater, container, saveInstanceState);
				break;
		default:
				break;
		}
		if(this instanceof ReloadbleListener){
			mSwipeRefreshLayout.setOnRefreshListener((ReloadbleListener)this);
			mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);
		}
		else{
			mSwipeRefreshLayout.setEnabled(false);
		}
		
		if(getAdapter() instanceof Scrollable){
			((AbsListView)mView).setOnScrollListener(this);
		}
		
		return v;
	}

	protected final View onCreateViewByList(LayoutInflater inflater,ViewGroup container, Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.home_list, container, false);	     
		mView = (AbsListView)v.findViewById(R.id.recent_list);

		((SortableListView)mView).setOnItemClickListener(this);
		if(this instanceof Dragable){
			((SortableListView)mView).setSortable(true);
			((SortableListView)mView).setDragListener(this);
		}		
		else{
			((SortableListView)mView).setSortable(false);
		}
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_list);
		return v;
	}
	
	public final View onCreateViewByGrid(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.home_grid, container, false);
	    mView = (AbsListView)v.findViewById(R.id.gallery1);
	    ((GridView)mView).setOnItemClickListener(this);
		mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh_grid);
	    return v;
	}



	public final int getVisiblePosition(){
		return mView.getFirstVisiblePosition();
	}

	public void setInitPosition(int vpos) {
		mInitPosition = vpos;
	}
	
	// ItemVerticalPager 用、後で戻ってきたときに問題無いようにスクロールしておく
	public void setSelection(int pos){
		if(mView != null){
			mView.setSelection(pos);
		}
	}
	
	public void requestListViewFocus() {
		mView.requestFocus();
	}
	
	//---------------------------------------------------------------------
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	Item item = (Item)((BaseItemAdapter)parent.getAdapter()).getItem(position);
    	EventHolder.showItemDetail(item);
    	
    	((BaseActivity)getActivity()).showItemDetail(item);
	}
	//---------------------------------------------------------------------
	// ScrollableListView 用の順番入れ替え用リスナー
	// 順番が変更できる Fragment は Dragable を implements に onStopDrag を実装する
	@Override
	public final int onStartDrag(int position) {
		return position;
	}

	@Override
	public final int onDuringDrag(int positionFrom, int positionTo) {
		return positionFrom;
	}
	
	@Override
	public boolean onStopDrag(int positionFrom, int positionTo) {
		return false;
	}
	//---------------------------------------------------------------------
	// 追加読み込み用の実装
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		// 見える範囲で最後に到達したら
		if (totalItemCount == 0 || totalItemCount  <= firstVisibleItem + visibleItemCount) {
			//mSwipeRefreshLayout .setRefreshing(true);
			((Scrollable)mAdapter).readNext(totalItemCount);
			//mSwipeRefreshLayout .setRefreshing(false);
		}
	}

	@Override
	public final void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	//---------------------------------------------------------------------
	//PullToSwipe用の実装
	//ReloadableListener を implements している Fragment はこれが呼ばれる
	public final void onRefresh() {
		Log.d(this.getClass().getSimpleName(),"reload start ----------------------------");
		ReloadAsyncTask asyncTask = new ReloadAsyncTask((ReloadbleListener)this);
		asyncTask.execute();
		Log.d(this.getClass().getSimpleName(),"reload end ----------------------------");
	}
	
	public final List<Item> onReload() throws Exception{
		if(getAdapter() instanceof ReloadableAdapter){
			return ((ReloadableAdapter)getAdapter()).onReload();
		}
		return null;
	}
	
	public final void onPostReload(List<Item>result){
		this.getAdapter().clear();
		if(result != null){
			this.getAdapter().addAll(result);
		}
		this.getAdapter().notifyDataSetChanged();
		this.setInitPosition(0);
		mSwipeRefreshLayout .setRefreshing(false);	
	}
	//---------------------------------------------------------------------

}

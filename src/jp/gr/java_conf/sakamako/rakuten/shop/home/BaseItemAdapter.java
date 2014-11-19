package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;

public abstract class BaseItemAdapter extends ArrayAdapter<Item>
implements OnScrollListener,OnItemClickListener{
	
	private BaseFragment mFragment = null;

	protected BaseItemAdapter(Context context, int resource,
			BaseFragment fragment, ArrayList<Item> myItem) {
		super(context, resource,myItem);
		mFragment = fragment;
	}

	// タブに表示するラベル
	public abstract String getTitle() ;
	
	public final void setVisiblePosition(int pos){
		mFragment.setSelection(pos);
	}

	@Override
	// 子ビューの生成は各リストビューに任せる
	public final View getView(int position, View convertView, ViewGroup parent) {
		//Log.d(this.getClass().getSimpleName(),"getView="+position);
		Item item = this.getItem(position);
		return mFragment.getListView().getView(item,convertView,parent);
	}

    //-----------------------------------------------------------------------------
	@Override
	public final void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	Item item = (Item)this.getItem(position);
    	EventHolder.showItemDetail(item);
	}
	
    //-----------------------------------------------------------------------------
	// 一番下に来たら追加読み込みする実装
	
	public interface Scrollable extends OnScrollListener{
		// まだ読み込み可能かの判定、これを見て AsyncTask を動かすか決める
		public boolean isMoreScrollable();
		// 次のページ読み込む実装（実際には各サブクラスでは無く、ここでやってしまっている
		// 他クラスから呼び出すためにインターフェース実装をしている
		public void onNextPage(boolean isReload);
	}
	
	// Async を使ったローでリング中かのフラグ
	private boolean isLoading = false;
	
	@Override
	// 一番最後に来たかの判定
	public final void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (totalItemCount - 30<= firstVisibleItem + visibleItemCount) {
    		//Log.d(this.getClass().getSimpleName(),"onScroll.onNextPage.total="+totalItemCount);
    		//Log.d(this.getClass().getSimpleName(),"onScroll.onNextPage.first="+firstVisibleItem);
    		//Log.d(this.getClass().getSimpleName(),"onScroll.onNextPage.visible="+visibleItemCount);
			onNextPage(false);
		}
    	if(this instanceof Countable){
    		Log.d(this.getClass().getSimpleName(),"onScroll="+((Countable)this).getAllCount());
    		mFragment.updateCount(
    				mFragment.getListView().getLastVisiblePosition()+1
    				,((Countable)this).getAllCount()
    				);
    	}
	}
	
	// 追加読み込みにおける AsyncTask の実行
	// ItemActivity の vertical pager の方からも呼ばれる
	public final void  onNextPage(boolean isReload){
		
		if(App.isNetworkError()){
			Log.d(this.getClass().getSimpleName(),"onNextPage - isNetworkError");
			return;
		}
		
		Log.d(this.getClass().getSimpleName(),"onNextPage");
		// 他タスクで読み込み中なら一旦あきらめる
		if(isLoading) return;
		
		//isLoading = true;
		// 念のため syncronized で isLoading チェックをすり抜けるものを止める
		synchronized(this){
			// 既に最終ページ到達済みであれば止める
			if(((Scrollable)this).isMoreScrollable()){
				isLoading = true;
				ReloadAsyncTask asyncTask = new ReloadAsyncTask(isReload,(ReloadbleListener)this);
				asyncTask.execute();
			}
		}
	}

	@Override
	public final void onScrollStateChanged(AbsListView view, int scrollState) {
		// 何もしない
	}
	    
	public interface Countable{
		public int getAllCount();
	}
	
	//---------------------------------------------------------------------
	//リロード用のPullToSwipe用の制御
	
	public interface ReloadbleListener extends OnRefreshListener{
		public List<Item> onReload() throws Exception;
		public List<Item> onSearch() throws Exception;
		public void onPostReload(boolean isReload,List<Item> result);
		public void completeReload(boolean isReload);

	}
	
	// OnRefreshListener のインターフェース
	//ReloadableListener を implements している Adapter はこれが呼ばれる
	public final void onRefresh() {
		Log.d(this.getClass().getSimpleName(),"reload start ----------------------------");
		isLoading = true;
		ReloadAsyncTask asyncTask = new ReloadAsyncTask(true,(ReloadbleListener)this);
		asyncTask.execute();
		Log.d(this.getClass().getSimpleName(),"reload end ----------------------------");
	}
	
	// 読み込み完了後の実装
	// 各サブクラスでは無く、ここでやってしまう
	public void onPostReload(boolean isReload,List<Item>result){
		if(isReload){
			Log.d(this.getClass().getSimpleName(),"onPostReload-clear");
			this.clear();
		}
		if(result != null){
			Log.d(this.getClass().getSimpleName(),"onPostReload-add="+result.size());
			this.addAll(result);
		}
		if(isReload && (result == null || result.size() == 0)){
			EventHolder.makeToast("該当件数は0件です。");
		}
		
		this.notifyDataSetChanged();
	}
	
	public final void completeReload(boolean isReload){

		if(isReload){
			Log.d(this.getClass().getSimpleName(),"onPostReload-finishReload");
			EventHolder.finishReload();
		}
		isLoading = false;
		
	}
	//---------------------------------------------------------------------
	// AsyncTask
	
	private class ReloadAsyncTask extends AsyncTask<Void, Void, List<Item>>  {
		
		private ReloadbleListener mListener = null;
		private Exception ex = null;
		private boolean mIsReload = true;;
		
		private ReloadAsyncTask(boolean isReload,ReloadbleListener listener){
			mListener = listener;
			mIsReload = isReload;
		}
		

		@Override
		protected List<Item> doInBackground(Void... params) {
			try{
				if(mIsReload){
					return mListener.onReload();
				}
				else{
					return mListener.onSearch();
				}
			}
				
			catch(Exception e){
				ex = e;
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(List<Item> result) {
		    Log.d(this.getClass().getSimpleName(), "onPostExecute");
		    if(ex != null){
		    	Log.d(this.getClass().getSimpleName(), "onPostExecute-Error");
		    	ex.printStackTrace();
	    		EventHolder.makeToast("ネットワークに接続できません。");
	    		App.setIsNetworkError(true);
		    }
		    mListener.onPostReload(mIsReload ,result);
		    mListener.completeReload(mIsReload);

		}
		

	}
}

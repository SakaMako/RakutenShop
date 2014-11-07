package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.toolbox.NetworkImageView;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class BaseItemAdapter extends ArrayAdapter<Item>
implements OnScrollListener,OnItemClickListener{
	
	private BaseFragment mFragment = null;
	
	protected BaseItemAdapter(Context context, int resource,
			BaseFragment fragment, ArrayList<Item> myItem) {
		super(context, resource,myItem);
		mFragment = fragment;
	}

	public abstract String getTitle() ;
	
	/**
	public final void resetFragment(BaseFragment fragment){
		mFragment = fragment;
	}
	*/
	
	public final void setVisiblePosition(int pos){
		mFragment.setSelection(pos);
	}

	@Override  
	public View getView(int position, View convertView, ViewGroup parent) {
		Log.d(this.getClass().getSimpleName(),"getView="+position);
		Item item = this.getItem(position);
		return mFragment.getListView().getView(item,convertView,parent);
	}

    //-----------------------------------------------------------------------------
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
    	Item item = (Item)this.getItem(position);
    	EventHolder.showItemDetail(item);
	}
	
	@Override
	public final void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		// 見える範囲で最後に到達したら
		if (totalItemCount == 0 || totalItemCount  <= firstVisibleItem + visibleItemCount) {
			((Scrollable)this).readNext(totalItemCount);
		}
	}

	@Override
	public final void onScrollStateChanged(AbsListView view, int scrollState) {
	}
	
	public interface Scrollable extends OnScrollListener{
		public void readNext(int arg0);
	}
	//---------------------------------------------------------------------
	//PullToSwipe用の実装
	//ReloadableListener を implements している Adapter はこれが呼ばれる
	public final void onRefresh() {
		Log.d(this.getClass().getSimpleName(),"reload start ----------------------------");
		ReloadAsyncTask asyncTask = new ReloadAsyncTask((ReloadbleListener)this);
		asyncTask.execute();
		Log.d(this.getClass().getSimpleName(),"reload end ----------------------------");
	}
	
	public final void onPostReload(List<Item>result){
		this.clear();
		if(result != null){
			this.addAll(result);
		}
		this.notifyDataSetChanged();
		
		EventHolder.finishReload();
	}

	
}

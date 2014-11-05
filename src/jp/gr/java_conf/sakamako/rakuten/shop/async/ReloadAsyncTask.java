package jp.gr.java_conf.sakamako.rakuten.shop.async;

import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;

public class ReloadAsyncTask extends AsyncTask<Void, Void, List<Item>>  {
	
	private ReloadbleListener mFragment = null;
	
	public ReloadAsyncTask(ReloadbleListener fragment){
		mFragment = fragment;
	}
	

	@Override
	protected List<Item> doInBackground(Void... params) {
		return mFragment.onReload();
	}
	
	@Override
	protected void onPostExecute(List<Item> result) {
	    Log.d(this.getClass().getSimpleName(), "onPostExecute");
	    mFragment.onPostReload(result);
	    
	}
	
	public interface ReloadbleListener extends OnRefreshListener{

		public List<Item> onReload();
		public void onPostReload(List<Item> result);

	}
	
	public interface ReloadableAdapter{
		public List<Item> onReload();
	}

}

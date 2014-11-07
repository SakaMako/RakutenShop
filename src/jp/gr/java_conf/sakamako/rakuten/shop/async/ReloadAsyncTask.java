package jp.gr.java_conf.sakamako.rakuten.shop.async;

import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;

public class ReloadAsyncTask extends AsyncTask<Void, Void, List<Item>>  {
	
	private ReloadbleListener mFragment = null;
	private Exception ex = null;
	
	public ReloadAsyncTask(ReloadbleListener fragment){
		mFragment = fragment;
	}
	

	@Override
	protected List<Item> doInBackground(Void... params) {
		try{
			return mFragment.onReload();
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
    		EventHolder.networkError(ex);
	    }
	    mFragment.onPostReload(result);
	}
	
	public interface ReloadbleListener extends OnRefreshListener{

		public List<Item> onReload() throws Exception;
		public void onPostReload(List<Item> result);

	}
	
	public interface ReloadableAdapter{
		public List<Item> onReload() throws Exception;
	}

}

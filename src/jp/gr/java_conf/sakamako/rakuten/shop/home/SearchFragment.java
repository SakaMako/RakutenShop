package jp.gr.java_conf.sakamako.rakuten.shop.home;
import android.os.Bundle;
import android.util.Log;
import com.squareup.otto.Subscribe;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;

public class SearchFragment extends BaseFragment 
{	
	
	private static SearchFragment own = null;
	
	public static SearchFragment getInstance(){
		if(own != null) return own;
		own =  new SearchFragment();
		return own;
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
    	Log.d(this.getClass().getSimpleName(),"onCreate");

		if(getAdapter()==null){
			Log.d(this.getClass().getSimpleName(),"SearchAdapterが無い");
			SearchParams searchParams = null;
			if(state != null){
				Log.d(this.getClass().getSimpleName(),"Bundleがある");
				searchParams = (SearchParams) state.getSerializable("searchParams");
			}
			else{
				searchParams = new SearchParams("");
			}
			
	    	Log.d(this.getClass().getSimpleName(),"Adapterの作成");
			this.setAdapter(new SearchAdapter(this,searchParams));
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"onSavedInstanceState");
		state.putSerializable("searchParams",((SearchAdapter)getAdapter()).getSearchParams());
	}
	
	//---------------------------------------------------------------
	@Override
	public boolean isDeletable() {
		return false;
	}

	@Override
	public String getTabTitle() {
		return "探す";
	}
	
	@Subscribe
	public void onFinishReload(FinishReloadEvent event){
		super.onFinishReload(event);
	}
}

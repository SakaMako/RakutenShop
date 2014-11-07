package jp.gr.java_conf.sakamako.rakuten.shop.home;
import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.event.FinisheReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.util.Log;

public class SearchFragment extends BaseFragment 
{	
	public SearchFragment(){
		this(TYPE_GRID);
	}

	private SearchFragment(int type){
		super(type);
		super.setAdapter(new SearchAdapter(this,new SearchParams("")));
	}
	
	private SearchFragment(int type,BaseItemAdapter adapter){
		super(type);
		super.setAdapter(adapter);
	}
	
	/**
	@Override
	public SearchFragment replace() {
		int type = this.getReverseType();
		SearchFragment fragment = new SearchFragment(type,getAdapter());
		return fragment;
	}
	*/
	
 	public void search(SearchParams searchParams){
 		Log.d("SearchFragment","search = " + searchParams.getSearchString());
 		
		BaseItemAdapter adapter = new SearchAdapter(this,searchParams);
		super.setAdapter(adapter);
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
	public void onFinishReload(FinisheReloadEvent event){
		super.onFinishReload(event);
	}
}

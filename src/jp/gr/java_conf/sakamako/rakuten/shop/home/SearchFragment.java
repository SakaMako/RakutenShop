package jp.gr.java_conf.sakamako.rakuten.shop.home;
import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;

public class SearchFragment extends BaseFragment 
{	
	public SearchFragment(){
		this(TYPE_GRID);
	}

	private SearchFragment(int type){
		super(type);
		super.setAdapter(new SearchAdapter(this,new SearchParams("")));
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

package jp.gr.java_conf.sakamako.rakuten.shop.home;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.util.Log;
import android.widget.AbsListView;

public class SearchFragment extends BaseFragment 
implements ReloadbleListener
{	
	public SearchFragment(){
		this(TYPE_GRID);
	}

	private SearchFragment(int type){
		super(type);
		mAdapter = new SearchAdapter(this,new SearchParams(""));
	}
	
	private SearchFragment(int type,BaseItemAdapter adapter){
		super(type);
		mAdapter = adapter;
	}
	
	@Override
	public SearchFragment replace() {
		int type = this.getReverseType();
		SearchFragment fragment = new SearchFragment(type,this.mAdapter);
		//fragment.setInitPosition(this.getVisiblePosition());
		return fragment;
	}
	
 	public void search(SearchParams searchParams){
 		Log.d("SearchFragment","search = " + searchParams.getSearchString());
		mAdapter = new SearchAdapter(this,searchParams);
		((AbsListView)mView).setAdapter(mAdapter);
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


}

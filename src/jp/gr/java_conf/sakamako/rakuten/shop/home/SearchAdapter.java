package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.SearchDoEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI.ResultHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 商品一覧を制御する
 * @author makoto.sakamoto
 */
public class SearchAdapter extends BaseItemAdapter 
implements BaseItemAdapter.Scrollable,BaseItemAdapter.ReloadbleListener,BaseItemAdapter.Countable
{

	    private int mCount = 0;
	    private int maxCount = 100; //楽天WEBサービスの上限値
	    private SearchParams mSearchParams = null;
	    private int allCount = 0;

		public SearchAdapter(BaseFragment fragment,SearchParams searchParams){
	       	super(App.getAppContext()
	       			, R.layout.home_grid_item
	       			,fragment
	       			,new ArrayList<Item>());
	       	Log.d("SearchAdpter","インスタンスの生成");
	       	this.mSearchParams = searchParams;
	       	initSearch();
	    }		
		
		public SearchParams getSearchParams() {
			return mSearchParams;
		}

		public void setSearchParams(SearchParams searchParams) {
			this.mSearchParams = searchParams;
		}
		
		@Override
		public int getAllCount(){
			return allCount;
		}
		
		@Subscribe
		//　他から呼ばれた用
		public void doSearch(SearchDoEvent event){
			Log.d(this.getClass().getSimpleName(),"doSearch="+event.getSearchParams().getSearchString());
	 		setSearchParams(event.getSearchParams());
	 		initSearch();
		}
		
		// 初期検索
		private void initSearch() {
		    mCount = 0;
		    maxCount = 100; //楽天WEBサービスの上限値
		    onNextPage(true);
		}
		
		@Override
		public List<Item> onReload() throws Exception {
		    mCount = 0;
		    maxCount = 100; //楽天WEBサービスの上限値
		    return onSearch();
		}
		
		@Override
		public List<Item> onSearch() throws Exception{
			List<Item> list = null;
		    	
		    if(mCount < maxCount){
		    	ResultHolder ret = ItemAPI.getItemList(mCount+1, mSearchParams);
		    	allCount = ret.getCnt();
		    	list = ret.getList();
		    	int i = list.size();
		    	// もう無い場合最終ページをアップデートする
		    	if(i<=0){
		    		Log.d(this.getClass().getSimpleName(),"最後のページに到達="+ mCount);
		    		maxCount = mCount;
		    	}
		       	else{
		       		mCount++;
		       	}
			}
		    return list;
		}
		
		@Override
		public boolean isMoreScrollable() {
			return (mCount < maxCount);
		}

		@Override
		public String getTitle() {
			return String.format("[%s]%sの検索結果"
			                      ,(mSearchParams.getSearchString().isEmpty())?"-":mSearchParams.getSearchString()
			                      ,(mSearchParams.isZaiko())?"在庫あり":"全て"
			                      );
			
		}
}

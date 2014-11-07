package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentException;
import org.xml.sax.SAXException;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * 商品一覧を制御する
 * @author makoto.sakamoto
 */
public class SearchAdapter extends BaseItemAdapter 
implements BaseItemAdapter.Scrollable,ReloadbleListener
{

	    private int mCount = 0;
	    private int maxCount = 100; //楽天WEBサービスの上限値
	    private SearchParams mSearchParams = null;

		public SearchAdapter(BaseFragment fragment,SearchParams searchParams){
	       	super(App.getAppContext()
	       			, R.layout.home_grid_item
	       			,fragment
	       			,new ArrayList<Item>());
	       	Log.d("SearchAdpter","インスタンスの生成");
	       	this.mSearchParams = searchParams;
	       	readNext(0);
	    }		
		
		public SearchParams getSearchParams() {
			return mSearchParams;
		}

		public void setSearchParams(SearchParams searchParams) {
			this.mSearchParams = searchParams;
		}
		
		@Override
		public List<Item> onReload() throws Exception{
			try {
				mCount=1;
				maxCount=100;
				return ItemAPI.getItemList(1, mSearchParams);
			} catch (Exception e) {
				throw e;
			}
		}
	    	
		@Override
	    public void readNext(int total_cnt){
	    	
	    	if(total_cnt < mCount) return;
	       	if(mCount < maxCount){
	       		int i = this.readPage(mCount+1);
	       		// もう無い場合最終ページをアップデートする
	       		if(i<=0){
	       			Log.d(this.getClass().getSimpleName(),"最後のページに到達="+ mCount);
	       			maxCount = mCount;
	       			return;
	       		}
	       		else{
	       			mCount++;
	       		}
	       	}
	    }
	    
	    /**
	     * 指定された page を読み込む
	     * @param page
	     * @return
	     */
	    @SuppressLint("NewApi")
		private int readPage(int page){
	    	Log.d(this.getClass().getSimpleName(),page+"ページの読み込み");
	    	try{
	       		//if(page == 1){
	       		//	this.clear();
	       		//}
	    		List<Item>list = ItemAPI.getItemList(page, mSearchParams);
	    		// 大元のリストにも入れておく
	    		Log.d(this.getClass().getSimpleName(),list.size() + "件の list - start");
	    		for(Iterator<Item> i=list.iterator();i.hasNext();){
	    			this.add(i.next());
	    		}
	    		Log.d(this.getClass().getSimpleName(),super.getCount() + "件の list - end");
	       		return list.size();
	    	}
	    	catch(Exception e){
	    		EventHolder.networkError(e);
	    		return -1;
	    	}
	    	finally{
	    		notifyDataSetChanged();
	    	}
	    }

		@Override
		public String getTitle() {
			return String.format("[%s]%sの検索結果"
			                      ,(mSearchParams.getSearchString().isEmpty())?"-":mSearchParams.getSearchString()
			                      ,(mSearchParams.isZaiko())?"在庫あり":"全て"
			                      );
			
		}
}

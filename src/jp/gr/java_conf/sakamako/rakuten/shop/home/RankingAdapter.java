package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import android.widget.AbsListView;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NetworkErrorEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;

public class RankingAdapter extends BaseItemAdapter
implements BaseItemAdapter.Scrollable,BaseItemAdapter.ReloadbleListener
{
	
	private int mCount = 0;
	private int maxCount = 34;

	public RankingAdapter(BaseFragment fragment) {
		   	super(App.getAppContext()
       			, R.layout.home_grid_item
       			,fragment
       			,new ArrayList<Item>());
		   	
		   	onNextPage(true);
	}
	
	@Override
	public List<Item> onReload() throws Exception {
	    mCount = 0;
	    maxCount = 34; //楽天WEBサービスの上限値
	    return onSearch();
	}
	
	@Override
	public List<Item> onSearch() throws Exception {
		Log.d(this.getClass().getSimpleName(),"onSearch="+mCount);
		List<Item> list = null;
       	if(mCount < maxCount){
       		list = ItemAPI.getRankingList(mCount+1);
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
		return "ランキング";
	}
}

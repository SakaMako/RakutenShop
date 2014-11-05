package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.util.Log;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadableAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseFragment.Scrollable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;

public class RankingAdapter extends BaseItemAdapter
implements ReloadableAdapter
,Scrollable
{
	
	private int mCount = 0;
	private int maxCount = 34;

	public RankingAdapter(BaseFragment fragment) {
		   	super(App.getAppContext()
       			, R.layout.home_grid_item
       			,fragment
       			,new ArrayList<Item>());
		   	
		   	for(int i=0;i<4&&this.getCount()<20;i++){
		   		readNext(i);
		   	}
    }
	
	@Override
	public List<Item> onReload() {
		try {
			mCount = 1;
			maxCount = 34;
			return ItemAPI.getRankingList(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void readNext(int total_cnt) {
		// もう既に読み込み済みの場合はおしまい
    	if(total_cnt < mCount) return;
       	if(mCount < maxCount){
       		int i = this.readPage(mCount+1);
       		// もう無い場合最終ページをアップデートする
       		if(i<=0){
       			maxCount = mCount;
       			return;
       		}
       		else{
       			mCount++;
       		}
       	}
	}
	
	private int readPage(int page){
   		try{
   			List<Item> list = ItemAPI.getRankingList(page);
   			for(Iterator<Item>it=list.iterator();it.hasNext();){
   				Item item = it.next();
   				this.add(item);
   			}
   			return list.size();
   		}
   		catch(Exception e){
   			Log.e(this.getClass().getSimpleName(),"エラーが発生しました",e);	
   			return -1;
   		}
   		finally{
   			notifyDataSetChanged();
   		}
		
	}

	@Override
	public String getTitle() {
		return "ランキング";
	}
}

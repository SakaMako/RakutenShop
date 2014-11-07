package jp.gr.java_conf.sakamako.rakuten.shop.item;
import java.util.LinkedHashMap;
import java.util.Map;

import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.Scrollable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;

public class ItemVerticalAdapter  extends FragmentStatePagerAdapter implements OnPageChangeListener {
	
	
	//------------------------------------------------------------------
	
	private BaseItemAdapter mAdapter = null;	// 縦方向用のアダプター
	private Map<Item,ItemDetailFragment> mList = null;
			
	//------------------------------------------------------------------

	public ItemVerticalAdapter(FragmentManager fm) {
		super(fm);
		Log.d(this.getClass().getSimpleName(),"コンストラクタ生成");
		mList = new LinkedHashMap<Item,ItemDetailFragment>(10){
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean removeEldestEntry(Map.Entry<Item,ItemDetailFragment> eldest){
				if(size() > 10){
					Log.d(this.getClass().getSimpleName(),"removeEldestEntry,size="+size());
					eldest = null;
					return true;
				}
				return false;
				
			}
		};

	}

	public void setVerticalAdapter(BaseItemAdapter adapter) {
		mAdapter = adapter;
	}
	
	@Override
	// 縦スクロールをスムーズにさせるために HashMap でキャッシュ化
	public Fragment getItem(int position) {
		Item item = mAdapter.getItem(position);
		
	    ItemDetailFragment fragment = mList.get(item);
	    if(fragment == null){
	    	fragment = new ItemDetailFragment(item);
	    	mList.put(item, fragment);
    	}
	    return fragment;
	}
    @Override
    public int getCount() {
    	if(mAdapter == null) return 0;
    	return mAdapter.getCount();
    }
    
	//------------------------------------------------------------------
    @Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int pos) {
		Log.d(this.getClass().getSimpleName(),"onPageSelected="+pos+","+mAdapter.getCount());
		// インデックスとサイズの差を埋めるために＋１
		if(pos + 1 == mAdapter.getCount()){
			Log.d(this.getClass().getSimpleName(),"最大数到達="+pos+","+mAdapter.getCount());
			if(mAdapter instanceof Scrollable){
				((Scrollable)mAdapter).readNext(pos);
			}
		}
		EventHolder.selectVerticalItem(mAdapter.getItem(pos));		
		mAdapter.setVisiblePosition(pos);
	}
}

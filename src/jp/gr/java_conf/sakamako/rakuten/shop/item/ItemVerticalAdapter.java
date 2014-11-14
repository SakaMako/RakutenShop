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
	
	private BaseItemAdapter mItemAdapter = null;	// 縦方向用のアダプター
	private Map<Item,ItemDetailFragment> mFragmentList = null;
	private int mPos = 0;
			
	//------------------------------------------------------------------

	public ItemVerticalAdapter(FragmentManager fm) {
		super(fm);
		Log.d(this.getClass().getSimpleName(),"コンストラクタ生成");
		mFragmentList = new LinkedHashMap<Item,ItemDetailFragment>(10){
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
		mItemAdapter = adapter;
	}
	
	@Override
	// 縦スクロールをスムーズにさせるために HashMap でキャッシュ化
	public Fragment getItem(int position) {
		Item item = mItemAdapter.getItem(position);
		
	    ItemDetailFragment fragment = mFragmentList.get(item);
	    if(fragment == null){
	    	fragment = new ItemDetailFragment(item);
	    	mFragmentList.put(item, fragment);
    	}
	    return fragment;
	}
    @Override
    public int getCount() {
    	if(mItemAdapter == null) return 0;
    	return mItemAdapter.getCount();
    }
    
	public ItemDetailFragment getCurrentItem() {
		return (ItemDetailFragment) this.getItem(mPos);
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
		Log.d(this.getClass().getSimpleName(),"onPageSelected="+pos+","+mItemAdapter.getCount());
		// インデックスとサイズの差を埋めるために＋１
		if(pos + 1 == mItemAdapter.getCount()){
			Log.d(this.getClass().getSimpleName(),"最大数到達="+pos+","+mItemAdapter.getCount());
			if(mItemAdapter instanceof Scrollable){
				((Scrollable)mItemAdapter).onNextPage(false);
			}
		}
		EventHolder.selectVerticalItem(mItemAdapter.getItem(pos));		
		mItemAdapter.setVisiblePosition(pos);
		
		mPos = pos;
	}

}

package jp.gr.java_conf.sakamako.rakuten.shop.item;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.os.Bundle;
import android.support.v4.view.DirectionalViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemVerticalFragment extends ItemBaseFragment 
{
	private ItemVerticalAdapter mVerticalAdapter = null;
	private DirectionalViewPager mVerticalPager = null;
	private static ItemVerticalFragment own = null;
	
	public static ItemVerticalFragment getInstance(){
		if(own != null) return own;
		return new ItemVerticalFragment();
	}

	public ItemVerticalFragment(){
		super();
		own  = this;

	}
	
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    	Log.d(this.getClass().getSimpleName(),"onCreateView-start");
    	
        View view = inflater.inflate(R.layout.item_detail_vertical, null);
        mVerticalPager = (DirectionalViewPager) view.findViewById(R.id.item_vertical_pager);
        mVerticalPager.setSaveEnabled(false); 
        
    	mVerticalAdapter = new ItemVerticalAdapter(getChildFragmentManager());
        
		if(mVerticalPager.getAdapter()==null){
			mVerticalPager.setAdapter(mVerticalAdapter);
		}
		mVerticalPager.setOnPageChangeListener(mVerticalAdapter);

        Log.d(this.getClass().getSimpleName(),"onCreateView-end");
        return view;
    }
    
	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);
		
		((ItemActivity)getActivity()).onFragmentCreated(mVerticalPager,mVerticalAdapter);
	}
	
	public Item getItem(){
		int pos = mVerticalPager.getCurrentItem();
		Log.d(this.getClass().getSimpleName(),"getItem.pos="+pos);
		ItemDetailFragment fragment = (ItemDetailFragment) mVerticalAdapter.getCurrentItem();
		return fragment.getItem();
	}
}

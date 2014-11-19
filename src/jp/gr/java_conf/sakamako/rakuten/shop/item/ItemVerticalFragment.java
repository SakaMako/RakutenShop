package jp.gr.java_conf.sakamako.rakuten.shop.item;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter.Countable;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.os.Bundle;
import android.support.v4.view.DirectionalViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ItemVerticalFragment extends ItemBaseFragment 
{
	private ItemVerticalAdapter mVerticalAdapter = null;
	private DirectionalViewPager mVerticalPager = null;
	private TextView mCntView = null;
	
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
		
		mCntView = (TextView)view.findViewById(R.id.cnt);
		mCntView.setVisibility(View.INVISIBLE);

        Log.d(this.getClass().getSimpleName(),"onCreateView-end");
        return view;
    }
    
	@Override
	public void onActivityCreated(Bundle state){
		super.onActivityCreated(state);
		
		((ItemActivity)getActivity()).onFragmentCreated(mVerticalPager,mVerticalAdapter);
		
		if(mCntView != null){
			if(mVerticalAdapter.getVerticalAdapter() instanceof Countable){
				Log.d(this.getClass().getSimpleName(),"VISIBLE");

				mCntView.setVisibility(View.VISIBLE);
			}
			else{
				Log.d(this.getClass().getSimpleName(),"INVISIBLE");

				mCntView.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	public ItemVerticalAdapter getVerticalAdapter(){
		return mVerticalAdapter;
	}

	public void updateCount(int pos) {
		if(mVerticalAdapter.getVerticalAdapter() instanceof Countable){
			String last = String.format("%1$,5d", pos);
			String all = String.format("%1$,5d", ((Countable)mVerticalAdapter.getVerticalAdapter()).getAllCount());
			mCntView.setText(last + "/" + all);
		}
	}
}

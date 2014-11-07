package jp.gr.java_conf.sakamako.rakuten.shop.item;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalFragmentCreated;
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

	public ItemVerticalFragment(){
		super();
	}
	
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
    	Log.d(this.getClass().getSimpleName(),"onCreateView-start");
    	
    	mVerticalAdapter = new ItemVerticalAdapter(getChildFragmentManager());
        View view = inflater.inflate(R.layout.item_detail_vertical, null);
        mVerticalPager = (DirectionalViewPager) view.findViewById(R.id.item_vertical_pager);

        Log.d(this.getClass().getSimpleName(),"onCreateView-end");
        return view;
    }
    @Override
	public void onActivityCreated(Bundle saveInstanceState){
		super.onActivityCreated(saveInstanceState);
		EventHolder.createVirticalFragment(mVerticalPager,mVerticalAdapter);
    }
    
    
    
}

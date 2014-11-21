package jp.gr.java_conf.sakamako.rakuten.shop.home;

import android.os.Bundle;
import android.util.Log;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;

public class RankingFragment extends BaseFragment{
	
	private static RankingFragment own = null;
	
	public static RankingFragment getInstance(){
		if(own != null) return own;
		return new RankingFragment();
	}
	
	public RankingFragment(){
		super();
		own = this;
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
    	Log.d(this.getClass().getSimpleName(),"onCreate");

		if(getAdapter()==null){
	    	Log.d(this.getClass().getSimpleName(),"Adapterの作成");
			this.setAdapter(new RankingAdapter(this));
		}
		
	}
	
	//---------------------------------------------------------------
	
	@Subscribe
	public void onFinishReload(FinishReloadEvent event){
		super.onFinishReload(event);
	}
		
	//---------------------------------------------------------------

	@Override
	public String getTabTitle() {
		return "ランキング";
	}
	
	@Override
	public int getTabIcon() {
		return R.drawable.ic_star_outline_black_18dp;
	}
}

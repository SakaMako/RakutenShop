package jp.gr.java_conf.sakamako.rakuten.shop.home;

import android.os.Bundle;
import android.util.Log;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;

public class RankingFragment extends BaseFragment{
	
	private static RankingFragment own = null;
	
	public static RankingFragment getInstance(){
		if(own == null){
			own = new RankingFragment();
		}
		return own;
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
	//メニューの切り替え
	@Override
	public boolean isDeletable() {
		return false;
	}

	@Override
	public String getTabTitle() {
		return "ランキング";
	}
	
	//---------------------------------------------------------------
	
	@Subscribe
	public void onFinishReload(FinishReloadEvent event){
		super.onFinishReload(event);
	}

}

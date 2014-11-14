package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.squareup.otto.Subscribe;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinishReloadEvent;

public class RankingFragment extends BaseFragment{
	
	public RankingFragment(){
		this(TYPE_GRID);
	}

	public RankingFragment(int type) {
		super(type);
		super.setAdapter(new RankingAdapter(this));
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

package jp.gr.java_conf.sakamako.rakuten.shop.home;

import com.squareup.otto.Subscribe;

import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;
import jp.gr.java_conf.sakamako.rakuten.shop.event.FinisheReloadEvent;

public class RankingFragment extends BaseFragment{
	
	public RankingFragment(){
		this(TYPE_GRID);
	}

	public RankingFragment(int type) {
		super(type);
		super.setAdapter(new RankingAdapter(this));
	}

	private RankingFragment(int type, BaseItemAdapter adapter) {
		super(type);
		super.setAdapter(adapter);
	}

	/**
	@Override
	public BaseFragment replace() {
		int type = super.getReverseType();
		RankingFragment fragment = new RankingFragment(type,getAdapter());
		return fragment;
	}
	*/
	
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
	
	@Subscribe
	public void onFinishReload(FinisheReloadEvent event){
		super.onFinishReload(event);
	}

}

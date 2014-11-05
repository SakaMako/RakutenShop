package jp.gr.java_conf.sakamako.rakuten.shop.home;

import jp.gr.java_conf.sakamako.rakuten.shop.async.ReloadAsyncTask.ReloadbleListener;

public class RankingFragment extends BaseFragment
implements ReloadbleListener{
	
	public RankingFragment(){
		this(TYPE_GRID);
	}

	public RankingFragment(int type) {
		super(type);
		mAdapter = new RankingAdapter(this);
	}

	private RankingFragment(int type, BaseItemAdapter adapter) {
		super(type);
		mAdapter = adapter;
	}

	@Override
	public BaseFragment replace() {
		int type = super.getReverseType();
		RankingFragment fragment = new RankingFragment(type,this.mAdapter);
		//fragment.setInitPosition(this.getVisiblePosition());
		return fragment;
	}
	
	//---------------------------------------------------------------
	//メニューの切り替え
	@Override
	public boolean isDeletable() {
		return false;
	}

}

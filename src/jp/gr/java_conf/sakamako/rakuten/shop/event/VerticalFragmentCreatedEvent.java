package jp.gr.java_conf.sakamako.rakuten.shop.event;

import android.support.v4.view.DirectionalViewPager;
import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemVerticalAdapter;

public class VerticalFragmentCreatedEvent {

	private DirectionalViewPager mVerticalPager = null;
	private ItemVerticalAdapter mVerticalAdapter = null;

	public VerticalFragmentCreatedEvent(DirectionalViewPager verticalPager,
			ItemVerticalAdapter verticalAdapter) {
		mVerticalPager = verticalPager;
		mVerticalAdapter = verticalAdapter;
	}

	public ItemVerticalAdapter getVerticalAdapter() {
		return mVerticalAdapter ;
	}

	public DirectionalViewPager getVerticalPager() {
		return mVerticalPager;
	}

}

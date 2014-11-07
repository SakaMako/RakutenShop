package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;

public class ShowItemDetailEvent {
	
	private Item mItem = null;

	public ShowItemDetailEvent(Item item) {
		mItem = item;
	}
	
	public Item getmItem() {
		return mItem;
	}


}

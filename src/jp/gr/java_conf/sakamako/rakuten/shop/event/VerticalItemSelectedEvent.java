package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;

public class VerticalItemSelectedEvent {

	private Item mItem = null;

	public VerticalItemSelectedEvent(Item item) {
		mItem = item;
	}

	public Item getItem() {
		return mItem;
	}
}

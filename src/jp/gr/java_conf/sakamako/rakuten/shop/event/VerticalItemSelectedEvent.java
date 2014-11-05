package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;

public class VerticalItemSelectedEvent {

	//private int mPos = 0;
	private Item mItem = null;

	//public VerticalItemSelectedEvent(int pos, Item item) {
	public VerticalItemSelectedEvent(Item item) {

		//mPos  = pos;
		mItem = item;
	}

	//public int getPosition() {
	//	return mPos;
	//}

	public Item getItem() {
		return mItem;
	}


}

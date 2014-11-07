package jp.gr.java_conf.sakamako.rakuten.shop.event;

public class TabChangedEvent {

	private int mPos = 0;

	public TabChangedEvent(int pos) {
		mPos  = pos;
	}
	
	public int getPosition(){
		return mPos;
	}

}

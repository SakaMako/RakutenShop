package jp.gr.java_conf.sakamako.rakuten.shop.event;

public class MakeToastEvent {
	
	private String mMessage = null;

	public MakeToastEvent(String message) {
		mMessage = message;
	}
	
	public String getMessage() {
		return mMessage;
	}

	
}

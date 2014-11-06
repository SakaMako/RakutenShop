package jp.gr.java_conf.sakamako.rakuten.shop.event;

public class NetworkErrorEvent {
	
	private Exception mEx = null;

	public NetworkErrorEvent(Exception ex) {
		mEx = ex;
	}
	
	public void stackTrace(){
		mEx.printStackTrace();
	}

}

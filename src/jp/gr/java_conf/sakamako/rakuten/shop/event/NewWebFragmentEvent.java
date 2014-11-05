package jp.gr.java_conf.sakamako.rakuten.shop.event;

public class NewWebFragmentEvent {

	private String mUrl = null;

	public NewWebFragmentEvent(String url) {
		mUrl = url;
	}

	public String getUrl() {
		return mUrl;
	}

}

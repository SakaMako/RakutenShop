package jp.gr.java_conf.sakamako.rakuten.shop.event;

public class ChangePageTitleEvent {

	private String mTitle = null;

	public ChangePageTitleEvent(String title) {
		mTitle = title;
	}

	public String getTitle() {
		return mTitle;
	}
}

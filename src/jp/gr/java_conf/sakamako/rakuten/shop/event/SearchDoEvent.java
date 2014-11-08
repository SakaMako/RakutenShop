package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;

public class SearchDoEvent {

	private SearchParams mSearchParams = null;

	public SearchDoEvent(SearchParams searchParams) {
		mSearchParams  = searchParams;
	}

	public SearchParams getSearchParams() {
		return mSearchParams;
	}

}

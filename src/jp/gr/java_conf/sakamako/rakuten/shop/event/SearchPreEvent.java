package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;

public class SearchPreEvent {

	private SearchParams mSearchParams = null;
 
	public SearchPreEvent(SearchParams searchParams) {
		mSearchParams = searchParams;
	}

	public SearchParams getSearchParams() {
		return mSearchParams;
	}

}

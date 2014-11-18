package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.Serializable;

import jp.gr.java_conf.sakamako.rakuten.shop.App;

public class SearchParams implements Serializable {

	private static final long serialVersionUID = 1L;
	private String mSearchString = null;
	private boolean isZaiko = false;
	private String itemCode = null;
	
	public SearchParams(String pSearchString){
		this.mSearchString = pSearchString;
	}

	public boolean isZaiko() {
		return isZaiko;
	}

	public void setZaiko(boolean isZaiko) {
		this.isZaiko = isZaiko;
	}

	public String getSearchString() {
		return mSearchString;
	}
	public void setSearchString(String mSearchString) {
		this.mSearchString = mSearchString;
	}
	public static String getShopUrl() {
		return App.getShopUrl();
	}
	public static int getGenreId() {
		return App.getGenreId();
	}
	
	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String code) {
		this.itemCode = code;
	}
}
package jp.gr.java_conf.sakamako.rakuten.shop.item;

import android.support.v4.app.Fragment;

public class ItemBaseFragment extends Fragment{
	private boolean isDeleted = false;

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

}

package jp.gr.java_conf.sakamako.rakuten.shop.home;

import jp.gr.java_conf.sakamako.rakuten.shop.event.VerticalItemSelectedEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.view.View;
import android.view.ViewGroup;

public interface BaseListView {
	public void onAttachedFragment(BaseFragment fragment);
	public View getView(Item item, View convertView, ViewGroup parent);
	public void invalidateViews ();
}

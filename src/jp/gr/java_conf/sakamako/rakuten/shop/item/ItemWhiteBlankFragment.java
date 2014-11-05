package jp.gr.java_conf.sakamako.rakuten.shop.item;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemWhiteBlankFragment extends ItemBaseFragment{
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
        View v = inflater.inflate(R.layout.item_detail_white_blank, container, false);		
        return v;
	}
	
}

package jp.gr.java_conf.sakamako.view;

import java.util.ArrayList;
import java.util.Collection;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;


public class CustomSearchView extends SearchView 
implements OnQueryTextListener,OnFocusChangeListener{
	public CustomSearchView(Context context) {
		super(context);
	}
	
	public CustomSearchView(Context context, AttributeSet attrs){
		super(context,attrs);
		
		setOnQueryTextListener(this);
		setOnFocusChangeListener(this);

        //attrs.xmlに定義したスタイルのインスタンスを作成
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomSearchView);
                
        setTextColor(a.getColor(R.styleable.CustomSearchView_textColor, Color.BLACK));
        setIconifiedByDefault(true);
	}
	
	@Override
	public boolean onQueryTextChange(String arg0) {
		Log.d(this.getClass().getSimpleName(),"onQueryTextChange="+arg0);
		// ×印でクリアされたら検索も初期化
		if(arg0.isEmpty()){
			EventHolder.searchItem(new SearchParams(""));
		}
		
		// 何もしない
		return true;
	}
	
	@Override
	// 検索がサブミットされたら
	public boolean onQueryTextSubmit(String arg0) {
		Log.d(this.getClass().getSimpleName(),"onQueryTextSubmit="+arg0);

		String searchKeyword = getQuery().toString();
		SearchParams searchParams = new SearchParams(searchKeyword);
		EventHolder.searchItem(searchParams);
	
		return true;
	}
	
	public void setTextColor(int id){
		for (TextView textView : findChildrenByClass(this, TextView.class)) {
			textView.setTextColor(id);
		}
	}
	
	private static <V extends View> Collection<V> findChildrenByClass(ViewGroup viewGroup, Class<V> clazz) {

	    return gatherChildrenByClass(viewGroup, clazz, new ArrayList<V>());
	}

	private static <V extends View> Collection<V> gatherChildrenByClass(ViewGroup viewGroup, Class<V> clazz, Collection<V> childrenFound) {

	    for (int i = 0; i < viewGroup.getChildCount(); i++)
	    {
	        final View child = viewGroup.getChildAt(i);
	        if (clazz.isAssignableFrom(child.getClass())) {
	            childrenFound.add((V)child);
	        }
	        if (child instanceof ViewGroup) {
	            gatherChildrenByClass((ViewGroup) child, clazz, childrenFound);
	        }
	    }

	    return childrenFound;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
        // EditTextのフォーカスが外れた場合
        if (hasFocus == false) {
            // ソフトキーボードを非表示にする
            InputMethodManager imm = (InputMethodManager)App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
	}

}

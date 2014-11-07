package jp.gr.java_conf.sakamako.view;

import java.util.ArrayList;
import java.util.Collection;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CustomSearchView extends SearchView {

	public CustomSearchView(Context context) {
		super(context);
	}
	
	public CustomSearchView(Context context, AttributeSet attrs){
		super(context,attrs);
		

        //attrs.xmlに定義したスタイルのインスタンスを作成
        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CustomSearchView);
                
        setTextColor(a.getColor(R.styleable.CustomSearchView_textColor, Color.BLACK));
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

}

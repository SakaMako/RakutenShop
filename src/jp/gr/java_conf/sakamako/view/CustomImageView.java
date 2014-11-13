package jp.gr.java_conf.sakamako.view;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;



public class CustomImageView extends NetworkImageView {

	public CustomImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
         
        //setMeasuredDimension(width,height);
    }

}

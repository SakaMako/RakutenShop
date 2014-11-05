package jp.gr.java_conf.sakamako.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

public class CustomWebView extends WebView{

	public CustomWebView(Context context) {
		super(context);
	}
	
	public CustomWebView(Context context, AttributeSet attrs){
		super(context,attrs);
	}
	public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr){
		super(context,attrs,defStyleAttr);
	}
	public CustomWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing){
		super(context, attrs, defStyleAttr, privateBrowsing);
	}
	
	private boolean is_gone=false;
	public void onWindowVisibilityChanged(int visibility)
	       {super.onWindowVisibilityChanged(visibility);
	        if (visibility==View.GONE)
	           {try
	                {WebView.class.getMethod("onPause").invoke(this);//stop flash
	                }
	            catch (Exception e) {}
	            this.pauseTimers();
	            this.is_gone=true;
	           }
	        else if (visibility==View.VISIBLE)
	             {try
	                  {WebView.class.getMethod("onResume").invoke(this);//resume flash
	                  }
	              catch (Exception e) {}
	              this.resumeTimers();
	              this.is_gone=false;
	             }
	       }
	public void onDetachedFromWindow()
	       {//this will be trigger when back key pressed, not when home key pressed
	        if (this.is_gone)
	           {try
	               {this.destroy();
	               }
	            catch (Exception e) {}
	           }
	       }


}

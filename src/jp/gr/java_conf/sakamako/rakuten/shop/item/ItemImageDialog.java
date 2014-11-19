package jp.gr.java_conf.sakamako.rakuten.shop.item;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

public class ItemImageDialog extends DialogFragment {

	private Item mItem = null;

	public ItemImageDialog(Item item) {
		mItem  = item;
	}
	
	  @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	//mDialog = this;
		  
	        final Dialog dialog = new Dialog(getActivity());
	        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	        dialog.setContentView(R.layout.web);
	        Log.d(this.getClass().getSimpleName(),"url="+mItem.getLargeImage());
	        
	        WebView wv = (WebView)dialog.findViewById(R.id.web_view);
	        wv.getSettings().setJavaScriptEnabled(true);
	        wv.getSettings().setSupportZoom(true);
	        wv.getSettings().setBuiltInZoomControls(true);
	        wv.setWebViewClient(new WebViewClient());
	        wv.loadUrl(mItem.getLargeImage());
	        //wv.zoomIn();
	        //wv.zoomIn();
	        
	        dialog.setCanceledOnTouchOutside(true);
	        dialog.setCancelable(false);
	        dialog.show();
	        return dialog;
	    }
}

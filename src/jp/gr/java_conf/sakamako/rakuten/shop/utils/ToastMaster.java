package jp.gr.java_conf.sakamako.rakuten.shop.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class ToastMaster extends Toast{
	private static Toast sToast = null;
	
	public ToastMaster(Context context) {
		super(context);
	}
	
	@Override
	public void show() {
		Log.d("ToastMaster","show");

		ToastMaster.setToast(this);
		super.show();
	}
	private static void setToast(Toast toast) {
		Log.d("ToastMaster","setToast");
		if (sToast != null)		sToast.cancel();
		sToast = toast;
	}
	public static void cancelToast() {
		Log.d("ToastMaster","cancelToast");
		if (sToast != null) sToast.cancel();
		sToast = null;
	}
}
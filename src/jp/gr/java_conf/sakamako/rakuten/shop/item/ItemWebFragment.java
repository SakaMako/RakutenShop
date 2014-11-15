package jp.gr.java_conf.sakamako.rakuten.shop.item;

import java.net.MalformedURLException;
import java.net.URL;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.ItemImageDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.event.NewWebFragmentEvent;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
//import android.webkit.CookieManager;
//import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ItemWebFragment extends ItemBaseFragment 
implements OnRefreshListener
{
	private Item mItem = null;
	private String mUrl = null;
	private WebView mWebView = null;
	private boolean isLoadStarted = false;
	private SwipeRefreshLayout mSwipeRefreshLayout =null;

	public ItemWebFragment() {
	}
	
	public void reset() {
		isLoadStarted = false;
		if(mWebView != null){
		mWebView.loadUrl("about:blank");
        //ItemWebViewClient iwc = new ItemWebViewClient();
    	//mWebView.setWebViewClient(iwc);
    	mWebView.clearCache(true);
    	mWebView.clearView();
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle saveInstanceState){
		
		Log.d(this.getClass().getSimpleName(),"start-----------------");
        View v = inflater.inflate(R.layout.item_detail_web, container, false);	
        
        mWebView = (WebView)v.findViewById(R.id.item_web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        
        ItemWebViewClient iwc = new ItemWebViewClient();
    	mWebView.setWebViewClient(iwc);
        
        mSwipeRefreshLayout  = (SwipeRefreshLayout) v.findViewById(R.id.refresh_web);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mSwipeRefreshLayout.setColorScheme(R.color.red, R.color.green, R.color.blue, R.color.yellow);
		
		mWebView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				WebView w = (WebView)v;
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (w.getHitTestResult() != null) {
						Log.d(this.getClass().getSimpleName(), "[1]HitTestResult.getType() " + w.getHitTestResult().getType());
						//return true;
					}
				}
				return false;
			}
		});
        
		Log.d(this.getClass().getSimpleName(),"end-----------------");
        
        return v;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d(this.getClass().getSimpleName(),"onDestory-"+ mUrl);
		
		//mWebView.stopLoading();
		mWebView.setWebChromeClient(null);
		mWebView.setWebViewClient(null);
		//mWebView.destroy();
		mWebView = null;
	}
	
	public void loadWeb(Item item){
		mItem = item;
		loadWeb(item.getAffiliateUrl());
	}

	public void loadWeb(String url) {
		if(isLoadStarted) return;
		isLoadStarted = true;
		mUrl = url;
		
		mSwipeRefreshLayout .setRefreshing(true);
        Log.d(this.getClass().getSimpleName(),mUrl + "を表示します");
		mWebView.getSettings().setUserAgentString(System.getProperty( "http.agent" ));
        mWebView.loadUrl(mUrl);
	}
	
	public boolean isLoadStarted(){
		return isLoadStarted;
	}

	
	//-------------------------------------
	private class ItemWebViewClient extends WebViewClient {
		
		public ItemWebViewClient(){
		}
		
	    @Override
	    public void onPageStarted(WebView view, String url, android.graphics.Bitmap favicon) {
	        super.onPageStarted(view, url, favicon);
        	Log.d(this.getClass().getSimpleName(),"onPageStarted="+url);

	    }
		
        @Override
        public void onPageFinished(WebView view, String url){
        	Log.d(this.getClass().getSimpleName(),"onPageFinished");
        	 mSwipeRefreshLayout .setRefreshing(false);
        }
        
		
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
        	Log.d(this.getClass().getSimpleName(),"読み込み:"+isLoadStarted+":"+url);
        	
    		// 拡大画像はアプリ内表示のため、１回読み込んでも有効とする
    		if(url.indexOf("http://image.rakuten.co.jp/"+SearchParams.getShopUrl()+"/") == 0){
    			if(mItem != null){
    				mWebView.stopLoading();
    				((BaseActivity)getActivity()).showDialog("画像表示",new ItemImageDialog(mItem));
    				return false;
    			}
    		}
    		
        	if(view.getHitTestResult() == null){
        		Log.d(this.getClass().getSimpleName(),"shouldOverrideUrlLoading hitTestResult=null");
        		
        		// リダイレクト結果、市場トップにいった場合はページ page not found
            	if(url.equals("http://www.rakuten.co.jp/")){
            		Log.d(this.getClass().getSimpleName(),"shouldOverrideUrlLoading.notFound:"+System.getProperty( "http.agent" ));
            		mWebView.stopLoading();
            		mWebView.getSettings().setUserAgentString("Mozilla/5.0");
            		mWebView.loadUrl(mUrl);
            		return false;
            	}
            	
            	// リダイレクト
        		return false;
        	}
        	
        	Log.d(this.getClass().getSimpleName(),"type:"+view.getHitTestResult().getType());
        	
        	//if( view.getHitTestResult().getType() <= 0){
            //		Log.d(this.getClass().getSimpleName(),"shouldOverrideUrlLoading hitTestResult<=0");
        	//		return false;
        	//}

        	try {
				if(new URL(url).getHost().contains(".rakuten.co.jp")){
					mWebView.stopLoading();
					EventHolder.newWebFragment(url);
					return false;
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
        		
        	Log.d(this.getClass().getSimpleName(),"外部リンク=" + url);
        		 
        	mWebView.stopLoading();
        	Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        	startActivity(i);
        	return true;
        }
	}

	@Override
	public void onRefresh() {
		Log.d(this.getClass().getSimpleName(),"reload start ----------------------------");
		mWebView.reload();
		Log.d(this.getClass().getSimpleName(),"reload end ----------------------------");
	}
}

package jp.gr.java_conf.sakamako.rakuten.shop;

import java.io.InputStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.io.IOException;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.Volley;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.home.BaseItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.utils.CustomImageCache;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;


public class App extends Application{
	  private static App instance;

	  private static String mShopUrl = null;
	  private static int mGenreId = -1;
	  private static Properties mConf = null;
	  private static ImageCache mImageCache= null;

	private static RequestQueue mRequestQueue;

	private static ImageLoader mImageLoader;
	 
	  public App() {
	    super();
	    instance = this;
	  }
	  
	  public static ImageLoader getImageLoader(){
		  if(mRequestQueue == null){
			  mRequestQueue = Volley.newRequestQueue(App.getAppContext());
		  }
		  
		  if(mImageLoader == null){
			  mImageLoader = new ImageLoader(mRequestQueue, new CustomImageCache());
		  }
		  
		  return mImageLoader;
		  
	  }
	  
	  private Properties getProperties(){
		  if(mConf  == null){
			  try{
				  Properties conf = new Properties();
				  Resources res = getInstance().getResources();  
				  InputStream is = res.openRawResource(R.raw.app);  
				  conf.load(is);
				  mConf = conf;
			  }
			  catch (IOException e) {
				  e.printStackTrace();
			  }
		  }
		  return mConf;
	  }

	  public static int getGenreId() {
		  if(mGenreId  == -1){
			 String sGenreId = App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.shop.genre_id");
			 if(sGenreId!=null){
				 mGenreId = Integer.parseInt(sGenreId);
			 }
		  }
		  return mGenreId;
	  }
	  
	  public static String getShopUrl() {
		if(mShopUrl == null){
				mShopUrl = App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.shop.shop_url");
		}
		return mShopUrl;
	  }
	  
	  public static App getInstance() {
			return instance;
		}
	  
	  
	    /**
	     * アプリケーションのContextを取得します。
	     * @return アプリケーションのContext
	     */
	    public static Context getAppContext() {
	    	return App.getInstance().getApplicationContext();
	    }
	 
	private static BaseItemAdapter mAdapter = null;
	public static void setCurrentAdapter(BaseItemAdapter adapter) {
		mAdapter = adapter;
	}

	public static BaseItemAdapter getCurrentAdapter() {
		return mAdapter;
	}

	public static boolean isRanking() {
		String sIsRanking = App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.shop.is_ranking");
		if(sIsRanking!=null){
			return Boolean.parseBoolean(sIsRanking);
		}
		return false;
	}

	public static boolean getDrawerMenuByReserve() {
		String sIsDrawer = App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.shop.is_drawer_reserve");
		if(sIsDrawer!=null){
			return Boolean.parseBoolean(sIsDrawer);
		}
		return false;
	}

	// for Rakuten Web API Service
	public static String getDeveloperId() {
		return App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.api.developer_id");
	}

	// for Rakuten Web API Service
	public static String getAffiliateId() {
		return App.getInstance().getProperties().getProperty("jp.gr.java_conf.noappnolife.rakuten.api.affiliate_id");
	}

	private static boolean isNetworkError = false;
	public static boolean isNetworkError() {
		return isNetworkError;
	}
	public static void setIsNetworkError(boolean b) {
		isNetworkError = b;
		if(b){
			new Timer().schedule(
					new TimerTask(){
						@Override
						public void run() {
							Log.d(this.getClass().getSimpleName(),"isNetworkErrorを戻す");
							isNetworkError = false;
						}
					}
					,(long)1000 * 30
					);
			
		}
		
	}

}

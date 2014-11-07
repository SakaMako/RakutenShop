package jp.gr.java_conf.sakamako.rakuten.shop.utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class CustomImageCache implements ImageCache{
	
	private LruCache<String, Bitmap> mMemoryCache;

    public CustomImageCache() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mMemoryCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
    	if(bitmap == null){
    		Log.d(this.getClass().getSimpleName(),"putBitmap is null");
    		return;
    	}
        mMemoryCache.put(url, bitmap);
    }

}

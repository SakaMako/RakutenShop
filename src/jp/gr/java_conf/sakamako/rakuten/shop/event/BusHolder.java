package jp.gr.java_conf.sakamako.rakuten.shop.event;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusHolder {
	 
	  private static BusEx mBus = null;
	 
	  public static Bus get() {
		  if(mBus == null){
			  mBus = new BusEx(ThreadEnforcer.MAIN);
		  }
	    return mBus;
	  }
	  
	  
	  public static class BusEx extends Bus{

		public BusEx(ThreadEnforcer main) {
			super(main);
		}
		
		public BusEx() {
			super();
		}

		@Override
		public void register(Object o){
			super.register(o);
			Log.d(this.getClass().getSimpleName(),"register="+ o.getClass().getSimpleName());
			
		}
	  }
}

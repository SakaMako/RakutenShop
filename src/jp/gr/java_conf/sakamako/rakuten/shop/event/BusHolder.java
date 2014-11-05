package jp.gr.java_conf.sakamako.rakuten.shop.event;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusHolder {
	 
	  private static Bus mBus = new Bus(ThreadEnforcer.MAIN);
	 
	  public static Bus get() {
	    return mBus;
	  }
}

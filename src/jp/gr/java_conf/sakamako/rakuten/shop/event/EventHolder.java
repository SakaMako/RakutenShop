package jp.gr.java_conf.sakamako.rakuten.shop.event;

import jp.gr.java_conf.sakamako.rakuten.shop.item.ItemVerticalAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.SearchParams;
import android.support.v4.view.DirectionalViewPager;
import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class EventHolder {
	 
	  private static BusEx mBus = null;
	 
	  private static Bus get() {
		  if(mBus == null){
			  mBus = new BusEx(ThreadEnforcer.MAIN);
		  }
	    return mBus;
	  }
	  
	  public static void register(Object o){
		  get().register(o);
	  }
	  
	  public static void unregister(Object o) {
		  EventHolder.get().unregister(o);
	  }
	  
		//-----------------------------------------------------
	  private static class BusEx extends Bus{

		private BusEx(ThreadEnforcer main) {
			super(main);
		}
		
		private BusEx() {
			super();
		}
		
		// デバッグ用にオーバーライドしておく
		@Override
		public void register(Object o){
			super.register(o);
			Log.d(this.getClass().getSimpleName(),"register="+ o.getClass().getSimpleName());	
		}
		@Override
		public void unregister(Object o){
			Log.d(this.getClass().getSimpleName(),"unregister="+ o.getClass().getSimpleName());	
			super.unregister(o);
		}
	  }
		//-----------------------------------------------------

	  // HomeActiivity から ItemActivity を呼び出し
	  public static void showItemDetail(Item item) {
		  get().post(new ShowItemDetailEvent(item));
	  }

	  // タブの選択
	  public static void changeTab(int pos) {
		  get().post(new TabChangedEvent(pos));
	  }

	  // ネットワークエラー
	  public static void networkError(Exception e) {
		  get().post(new NetworkErrorEvent(e));
	  }

	  // 縦方向の商品の選択
	  public static void selectVerticalItem(Item item) {
	        get().post(new VerticalItemSelectedEvent(item));
	  }

	  // WebView の表示
	  public static void newWebFragment(String url) {
		  get().post(new NewWebFragmentEvent(url));
	  }

	  /**
	   * 縦方向のフラグメントの作成
	   * ItemVerticalFragment.onActivityCreated
	   * |
	   * ItemHorizontalAdapter.onPostCreateView
	   */
	  public static void createVirticalFragment(
			DirectionalViewPager verticalPager,
			ItemVerticalAdapter verticalAdapter) {
		  get().post(new VerticalFragmentCreatedEvent(verticalPager,verticalAdapter));
	  }

	  // ItemActivity の終了
	  public static void finishItemActivity() {
		get().post(new ItemActivityFinishEvent());
	  }

	  // 追加読み込みが完了したら
	  public static void finishReload() {
		  get().post(new FinishReloadEvent());
	  }

	/**
	 * 検索窓からのサーチ
	 * @link HomeActivity.onSeachItem
	 */
	public static void searchItem(SearchParams searchParams) {
		get().post(new SearchPreEvent(searchParams));
	}
	
	/**
	 * 検索
	 * @link SearchAdapter.doSearch
	 */
	public static void doSearchItem(SearchParams searchParams) {
		get().post(new SearchDoEvent(searchParams));
		
		// HomeAdapte.goToSearch
		// 
		get().post(new SearchPostEvent());
	}

}

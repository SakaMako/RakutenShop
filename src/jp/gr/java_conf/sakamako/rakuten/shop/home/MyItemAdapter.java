package jp.gr.java_conf.sakamako.rakuten.shop.home;
import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.event.EventHolder;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyItemList;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Category;
import android.util.Log;

public class MyItemAdapter extends BaseItemAdapter
	implements MyItemList.OnChangedMyItemListener,BaseItemAdapter.ReloadbleListener,BaseItemAdapter.Countable,BaseItemAdapter.Scrollable
{
	private Category mCat = null;
	
		public MyItemAdapter(BaseFragment fragment,Category cat) {
			super(App.getAppContext(), R.layout.home_list_item
					,fragment
					,MyCategory.getInstance().getMyItem(cat)
					);
			mCat = cat;
			Log.d("MyItemAdapter","type="+fragment.getType());
		}
		
		@Override
		public List<Item> onReload() throws Exception {
			List<Item>tmpList = new ArrayList<Item>();			
			for(int i=0;i<this.getCount();i++){
				Item it = this.getItem(i);
				
				Item tmpItem = ItemAPI.getItem(it);
				if(tmpItem != null){
					tmpList.add(tmpItem);
				}
				else{
					tmpList.add(it);
					it.setIsAvailability(""+Item.AVALIABILITY_NG);
				}
				Thread.sleep(300);
			}
			return tmpList;
		}
		
		public void onPostReload(boolean isReload,List<Item>result){
			if(result==null) return;
			MyItemList itemList = mCat.getItemList();
			for(int i=0;i<result.size();i++){
				itemList.updateItem(result.get(i));
			}
			
			EventHolder.makeToast("更新が完了しました。");
		}

		@Override
		public List<Item> onSearch() throws Exception {
			// 何もしない
			return null;
		}
		
		// 商品が更新されたら notifyDataSetChanged を呼び出す
		public void onChangedMyItem(MyItemList items) {
			Log.d("MyItemAdapter","onChangedMyItem");
			this.notifyDataSetChanged();
		}

		@Override
		public String getTitle() {
			return mCat.getLabel();
		}

		@Override
		public int getAllCount() {
			return mCat.getItemList().size();
		}

		@Override
		public boolean isMoreScrollable() {
			return false;
		}
}
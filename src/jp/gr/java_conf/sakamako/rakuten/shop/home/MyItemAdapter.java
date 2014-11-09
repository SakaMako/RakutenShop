package jp.gr.java_conf.sakamako.rakuten.shop.home;
import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyItemList;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.util.Log;

public class MyItemAdapter extends BaseItemAdapter
	implements MyItemList.OnChangedMyItemListener,BaseItemAdapter.ReloadbleListener
{
	private Category mCat = null;
	
		public MyItemAdapter(BaseFragment fragment,int textViewResourceId,Category cat) {
			super(App.getAppContext(), textViewResourceId
					,fragment
					,MyCategory.getInstance().getMyItem(cat)
					);
			mCat = cat;
			Log.d("MyItemAdapter","type="+fragment.getType());
		}
		
		@Override
		public List<Item> onReload() throws Exception {
			MyItemList list = MyCategory.getInstance().getMyItem(mCat);
			Log.d(this.getClass().getSimpleName(),"list="+list.size());
			
			for(int i=0;i<this.getCount();i++){
				Item it = this.getItem(i);
				if(it.isValid()) continue;
				
				Item i2 = ItemAPI.getItem(it);
				if(i2 != null){
					Log.d(this.getClass().getSimpleName(),"update="+it.getName());
					it = i2;
				}
				else{
					it.setIsAvailability(""+Item.AVALIABILITY_NG);
				}
				list.updateItemWithoutNotify(it);
				Thread.sleep(500);
			}
			
			list.getFile().onChangedMyItem(list);

			return list;
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


}
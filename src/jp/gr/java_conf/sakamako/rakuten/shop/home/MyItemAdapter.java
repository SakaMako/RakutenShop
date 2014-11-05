package jp.gr.java_conf.sakamako.rakuten.shop.home;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyItemList;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.util.Log;

public class MyItemAdapter extends BaseItemAdapter
	implements MyItemList.OnChangedMyItemListener
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
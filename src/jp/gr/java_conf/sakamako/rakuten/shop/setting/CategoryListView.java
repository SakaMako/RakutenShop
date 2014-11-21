package jp.gr.java_conf.sakamako.rakuten.shop.setting;

import java.util.List;

import com.android.volley.toolbox.NetworkImageView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import jp.gr.java_conf.sakamako.view.SortableListView;
import jp.gr.java_conf.sakamako.view.SortableListView.DragListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CategoryListView extends SortableListView
implements DragListener{

	public CategoryListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setSortable(true);
		setOnItemClickListener((SettingActivity)getContext());
		setDragListener(this);
		setAdapter(new CategoryAdapter(context,R.layout.setting_category_item));
	}

	@Override
	public int onStartDrag(int position) {
		// 何もしない
		return position;
	}

	@Override
	public int onDuringDrag(int positionFrom, int positionTo) {
		// 何もしない
		return positionFrom;
	}

	@Override
	public boolean onStopDrag(int from, int to) {
		Log.d(this.getClass().getSimpleName(),"onStopDrag = " + from + " -> " + to);
		if(to < 0 || from < 0){
			return false;
		}
		CategoryAdapter adapter = (CategoryAdapter) this.getAdapter();
		if(adapter.getCount() <= to){
			to = adapter.getCount()-1;
		}

		String fromItem = adapter.getItem(from);	
		adapter.remove(fromItem);
		adapter.insert(fromItem, to);
		adapter.notifyDataSetChanged();
		
		return true;
	}
	
	private class CategoryAdapter extends ArrayAdapter<String>{

		private CategoryAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			
			List<Category> list = MyCategory.getInstance().getList();
			for(int i=0;i<list.size();i++){
				this.add(list.get(i).getLabel());
			}
		}
		

		@Override
		public final View getView(int position, View convertView, ViewGroup parent) {
			// ビューを受け取る   
			LinearLayout view = (LinearLayout) convertView;   
			if (view == null) {
				// 受け取ったビューがnullなら新しくビューを生成   
				view = (LinearLayout)((LayoutInflater)App.getAppContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
						.inflate(R.layout.setting_category_item, null);
			}

			TextView textView = (TextView)view.findViewById(R.id.category_name);   
			textView.setText(getItem(position));  
			return view;
		}
	}
}

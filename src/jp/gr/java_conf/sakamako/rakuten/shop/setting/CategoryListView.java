package jp.gr.java_conf.sakamako.rakuten.shop.setting;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import jp.gr.java_conf.sakamako.view.SortableListView;
import jp.gr.java_conf.sakamako.view.SortableListView.DragListener;
import android.widget.ArrayAdapter;

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

	}
	
	

}

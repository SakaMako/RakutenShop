package jp.gr.java_conf.sakamako.rakuten.shop.item;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.toolbox.NetworkImageView;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import jp.gr.java_conf.sakamako.rakuten.shop.BaseActivity;
import jp.gr.java_conf.sakamako.rakuten.shop.dialog.ItemImageDialog;
import jp.gr.java_conf.sakamako.rakuten.shop.home.MyItemAdapter;
import jp.gr.java_conf.sakamako.rakuten.shop.home.MyItemFragment;
import jp.gr.java_conf.sakamako.rakuten.shop.model.Item;
import jp.gr.java_conf.sakamako.rakuten.shop.model.ItemAPI;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ItemDetailFragment extends ItemBaseFragment implements 
OnItemSelectedListener
, OnClickListener
{
	private static Map<Item,ItemDetailFragment> fragmentList = null;

	public static ItemDetailFragment getInstance(Item item){
		ItemDetailFragment fragment = getInstanceFromList(item);
		if(fragment == null){
			fragment = new ItemDetailFragment();
            Bundle args = new Bundle();  
            args.putSerializable("item", item);
	        fragment.setArguments(args);
			fragmentList.put(item, fragment);
		}
		return fragment;
	}
	
	private static ItemDetailFragment getInstanceFromList(Item item){
		if(fragmentList == null){
			fragmentList = new LinkedHashMap<Item,ItemDetailFragment>(10){
				@Override
				protected boolean removeEldestEntry(Map.Entry<Item,ItemDetailFragment> eldest){
					if(size() > 10){
						Log.d(this.getClass().getSimpleName(),"removeEldestEntry,size="+size());
						eldest = null;
						return true;
					}
					return false;	
				}
			};
		}
		return fragmentList.get(item);
	}
		
	//--------------------------------------------------------------------
	private Item mItem = null;
	private NetworkImageView imageView;
	
	public ItemDetailFragment(){
		super();
	}
	
	public Item getItem(){
		if(mItem == null){
			Bundle bundle = getArguments();
			mItem = (Item) bundle.getSerializable("item");
		}
		return mItem;
	}
	
	@Override
	public void onCreate(Bundle state){
		super.onCreate(state);
		//Log.d("ItemDetailFragment","start-----------------");
		if(mItem == null){
			Log.d(this.getClass().getSimpleName(),"mItemが無い");
			if(state != null){
				Log.d(this.getClass().getSimpleName(),"Bundleがある");
				mItem = (Item) state.getSerializable("item");
			}
			else{
				mItem = getItem();
			}
		}
		
		ItemDetailFragment fragment = getInstanceFromList(mItem);
		if(fragment == null){
			fragmentList.put(mItem, this);
		}
		
		if(!mItem.isValid()){
			try{
				Item itemAfterApi = ItemAPI.getItem(mItem);
				if(itemAfterApi != null){
					mItem = MyCategory.getInstance().updateItem(itemAfterApi);
				}
				else{
					mItem.setIsAvailability(Item.AVALIABILITY_NG+"");
				}
			}
			catch(Exception e){
				e.printStackTrace();
				mItem.setIsAvailability(Item.AVALIABILITY_NG+"");
			}
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);
		Log.d(this.getClass().getSimpleName(),"SavedInstanceState="+mItem.getName());
		state.putSerializable("item",mItem);
		imageView =null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle state){
        View v = inflater.inflate(R.layout.item_detail, container, false);		
		imageView = (NetworkImageView)v.findViewById(R.id.item_image);
        imageView.setImageUrl(mItem.getLargeImage(), App.getImageLoader());
        imageView.setOnClickListener(this);
        imageView.setDefaultImageResId(R.drawable.ic_action_refresh);
        imageView.setErrorImageResId(R.drawable.ic_action_remove);
        
        ((TextView)v.findViewById(R.id.item_name2)).setText(mItem.getName());
        ((TextView)v.findViewById(R.id.item_price)).setText(mItem.getPriceString());
        
        List<String> labelList = MyCategory.getInstance().getLabelList();
        
        Spinner s = (Spinner) v.findViewById(R.id.item_category_spinner);
        ArrayAdapter<String> adapter = 
        		new ArrayAdapter<String>(
        				App.getAppContext()
                        ,R.layout.simple_spinner_dropdown_item
                        ,labelList
        				);
        s.setAdapter(adapter);
        
        s.setSelection(
        		labelList.indexOf(
        				MyCategory.getInstance().getCategoryByItem(mItem)
        		)
        		,false
        );
        s.setOnItemSelectedListener(this);
        
        ImageView iv = (ImageView)v.findViewById(R.id.right_slide_allow);
        if(mItem.getIsAvailability()==Item.AVALIABILITY_OK){
        	iv.setVisibility(View.VISIBLE);
        }
        else{
        	iv.setVisibility(View.INVISIBLE);
        }
        return v;
	}

	@Override
	// カテゴリが選ばれたら
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
	    String  value = parent.getItemAtPosition(position).toString();
	    MyCategory.getInstance().moveCategory(mItem,value);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// 何もしない
		
	}

	@Override
	public void onClick(View v) {
		((BaseActivity)getActivity()).showDialog("画像表示",new ItemImageDialog(mItem));
	}
}

package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.util.Log;

public class MyItemList extends ArrayList<Item>{
	
	private static final long serialVersionUID = 5403609459866937642L;

	private MyItemFile mMyItemFile = null;
	private List<OnChangedMyItemListener> mListener = null;
		
	public MyItemList(String fileName){
		this(new MyItemFile(fileName));
	}
	
	private MyItemList (MyItemFile myItemFile){
		mMyItemFile = myItemFile;
		mMyItemFile.read(this);
		mListener = new ArrayList<OnChangedMyItemListener>();
		addOnChangedListener(mMyItemFile);
	}
	
	public MyItemFile getFile(){
		return mMyItemFile;
	}
	
	// MyItemList が更新された際に呼び出したいものがあればこれを実装する。
	public interface OnChangedMyItemListener{
		public void onChangedMyItem(MyItemList items);
	}
	// 
	public void addOnChangedListener(OnChangedMyItemListener listener){
		mListener.add(listener);
	}
	
	public void removeOnChangedListener(OnChangedMyItemListener listener){
		mListener.remove(listener);
	}
	//
	private void callNotifyChangedListener(){
		Log.d("MyItemList","notifyChangedListener="+this.size()+"件");
		for(Iterator<OnChangedMyItemListener> i=mListener.iterator();i.hasNext();){
			i.next().onChangedMyItem(this);
		}
	}
	
	private int isExists(Item item){
		int index = 0;
		for(Iterator<Item> i=this.iterator();i.hasNext();index++){
			Item it = i.next();
			if(it.getCode().equals(item.getCode())){
				return index;
			}
		}
		return -1;
	}
	
	/** 指定された商品があれば更新 */
	public Item updateItem(Item item){
		int pos = isExists(item);
		if(pos < 0) return item;
				
		super.set(pos, item);
		callNotifyChangedListener();
		
		return item;
	}
	
	public void removeItem(Item item){
		this.remove(isExists(item));
		callNotifyChangedListener();
	}
	public void addItem(Item item){
		this.add(0,item);
		callNotifyChangedListener();
	}

	public void move(int from, int to) {
		if(to < 0) to = 0;
		Log.d("MyItemList.move","from="+from + ",to="+to);
		
		Item fromItem = this.get(from);
		this.remove(from);
		this.add(to,fromItem);
		callNotifyChangedListener();
	}

	public void remove() {
		Log.d("MyItemList","MyItemFileの削除");
		mMyItemFile.remove();
		mMyItemFile = null;
	}
}

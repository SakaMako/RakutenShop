package jp.gr.java_conf.sakamako.rakuten.shop.model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.util.Log;

public class MyCategory extends MyCategoryFile{
	
	public static final String CATEGORY_NONE = "なし";
	
	public static MyCategory mCategory= null;
	private List<Category> mList = null;
	
	public static MyCategory getInstance(){
		if(mCategory == null){
			mCategory = new MyCategory();
		}
		return mCategory;
	}

	private MyCategory(){
		mList = read();
	}
	
	public List<Category> getList(){
		return mList;
	}

	
	public MyItemList getMyItem(String categoryName) {
		Category cat = getCategory(categoryName);
		if(cat == null) return null;
		return getCategory(categoryName).getItemList();
	}
		
	public Category getCategory(String categoryName){
		// 最初の初期化時には getInstance() を通さないと null pointer
		List<Category> list = MyCategory.getInstance().getList();
		Log.d(this.getClass().getSimpleName(),"getCategory="+list.size()+"件/"+categoryName);

		for(Iterator<Category> i= list.iterator();i.hasNext();){
			Category c = i.next();
			if(categoryName.toUpperCase().equals(c.getLabel().toUpperCase())){
				return c;
			}
		}
		Log.d(this.getClass().getSimpleName(),"getCategory is null");
		
		return null;
	}
	public MyItemList getMyItem(Category cat) {
		// 最初の初期化時には getInstance() を通さないと null pointer
		List<Category> list = MyCategory.getInstance().getList();
		Log.d(this.getClass().getSimpleName(),"getMyItem="+list.size()+"件");
		for(Iterator<Category> i= list.iterator();i.hasNext();){
			Category c = i.next();
			if(cat.getId() == c.getId()){
				return c.getItemList();
			}
		}
		return null;
	}
	
	public Category add(String text) {
		Log.d("MyCategory","カテゴリの作成" + text);
		// 既にあるなら null 
		if(getMyItem(text) != null){
			Log.d("MyCategory","カテゴリの重複" + text);
			return null;
		}
		
		int max = getMaxId();
		Category cat = new Category(max+1,text,"recent_item_"+(max+1)+".xml");
		mList.add(cat);
		write(mList);
		Log.d("MyCategory","カテゴリの追加="+cat.getId()+","+cat.getLabel());
		return cat;
	}
	public int getMaxId(){
		int max = 0;
		for(Iterator<Category> it = mList.iterator();it.hasNext();){
			Category cat = it.next();
			if(max < cat.getId()){
				max = cat.getId();
			}
		}
		return max;
	}

	public String getCategoryByItem(Item item) {
		for(Iterator<Category> it = mList.iterator();it.hasNext();){
			Category cat = it.next();
			if(cat.getItemList().contains(item)){
				return cat.getLabel();
			}
		}
		return CATEGORY_NONE;
	}
	
	public Item updateItem(Item item){
		String label = getCategoryByItem(item);
		if(label.equals(CATEGORY_NONE)) return item;
		MyItemList itemList = getMyItem(label);
		return itemList.updateItem(item);
	}

	public void moveCategory(Item item,  String s) {
		
		String before = getCategoryByItem(item);
		// 移動元と移動先が同じなら何もしない
		if(before.equals(s)) return;
		
		Log.d("MyCategory","カテゴリの移動 "+before+" "+s);
		
		if(!before.equals(CATEGORY_NONE)){
			getMyItem(before).removeItem(item);
		}
		if(!s.equals(CATEGORY_NONE)){
			getMyItem(s).addItem(item);
		}
		
	}
	
	// カテゴリの String の配列を返す
	public ArrayList<String> getLabelList() {
    	ArrayList<String> list = new ArrayList<String>();
    	for(Iterator<Category> i=getList().iterator();i.hasNext();){
    		list.add(i.next().getLabel());
    	}
	    	
    	list.add(MyCategory.CATEGORY_NONE);
    	return list;
    }
	
	
	public void moveCategory(List<String> list){
		List<Category> tmp = new ArrayList<Category>();
		
		for(int i=0;i<list.size();i++){
			Category cat = this.getCategory(list.get(i));
			tmp.add(cat);
		}
		mList.clear();
		mList.addAll(tmp);
		write(mList);
	}

	public void remove(Category cat) {
		for(int i=0;i<mList.size();i++){
    		Category c = mList.get(i);
    		if(cat.getId() == c.getId()){
    			Log.d("MyCategory","カテゴリの削除="+i+","+c.getLabel()+","+c.getId());
    			c.remove();
    			mList.remove(i);
    			write(mList);
    			return;
    		}
    	}
	}
	

	




}

package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import android.content.Context;
import android.util.Log;
import jp.gr.java_conf.sakamako.rakuten.shop.App;

public class MyCategory extends XmlFileHandler{
	
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
		write();
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
			if(cat.mList.contains(item)){
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

	public void remove(Category cat) {
		for(int i=0;i<mList.size();i++){
    		Category c = mList.get(i);
    		if(cat.getId() == c.getId()){
    			Log.d("MyCategory","カテゴリの削除="+i+","+c.getLabel()+","+c.getId());
    			c.remove();
    			mList.remove(i);
    			write();
    			return;
    		}
    	}
	}
	
	private final static String FILE_NAME = "category.xml";
	private final static String ROOT = "root";
	private final static String ELEMENTS = "categories";
	private final static String ELEMENT = "category";
	private final static String NO = "no";
	private final static String LABEL = "label";
	private final static String FILE = "file";
	private static final String PATH = "/"+ROOT+"/"+ELEMENTS+"/"+ ELEMENT;
	
	private List<Category> read() {
		
		List<Category> list = new ArrayList<Category>();
		try{
			/** 設定 xml の読み込み */		
			FileInputStream input = App.getAppContext().openFileInput(FILE_NAME);
			SAXReader reader = new SAXReader();
			Document document = reader.read(input);

			List<? extends Node> nodes = document.selectNodes(PATH);
			for(int i=0;i<nodes.size();i++){
				Node node = (Node)nodes.get(i);
				Category cat = new Category(
						Integer.parseInt(convertString(node.selectSingleNode(NO)))
						,convertString(node.selectSingleNode(LABEL))
						,convertString(node.selectSingleNode(FILE))
				);
				list.add(cat);
			}
		} catch (DocumentException e) {
			Log.e(this.getClass().getSimpleName(),"解析エラー",e);
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getSimpleName(),"読み込みエラー",e);
		}
		return list;
	}
			
	public void write(){
	
		try{
    		Document document = DocumentHelper.createDocument();
    		Element root = document.addElement(ROOT).addElement(ELEMENTS);
			for(int i=0;i<mList.size();i++){
				Element item = root.addElement(ELEMENT);
				item.addElement(NO).addText(""+mList.get(i).getId());
				item.addElement(LABEL).addText(mList.get(i).getLabel());
				item.addElement(FILE).addText(mList.get(i).getItemList().getFile().getFileName());
			}
    		FileOutputStream outStream = App.getAppContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	    	
    		XMLWriter writer = new XMLWriter(outStream);
    		writer.write(document);
    		writer.close();
			Log.e(this.getClass().getSimpleName(),"書き込み完了 "+FILE_NAME);
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(),"書き込みエラー",e);
		}
	}
	
	public class Category {
		private int mId = -1;
		private String mLabel = null;
		private MyItemList mList = null;
		
		private Category(int id,String text,String fileName){
			mId = id;
			mLabel = text;
			mList = new MyItemList(fileName);
		}
		public void remove() {
			mList.remove();
		}
		public int getId(){
			return mId;
		}
		public String getLabel() {
			return mLabel;
		}
		public MyItemList getItemList(){
			return mList;
		}
		
		@Override
		public boolean equals(Object i){
			boolean r =  this.getLabel().equals(((Category)i).getLabel());		
			return r;		
		}
	}



}

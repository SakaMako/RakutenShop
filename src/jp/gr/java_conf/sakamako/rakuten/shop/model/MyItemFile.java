package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import android.content.Context;
import android.util.Log;

/**
 * お気に入りアイテムをローカルに保存、取得する
 * @author makoto.sakamoto
 *
 */
public class MyItemFile extends XmlFileHandler implements MyItemList.OnChangedMyItemListener{
	
	private static final String ITEM_IMAGE = "itemImage";
	private static final String ITEM_URL = "itemUrl";
	private static final String ITEM_NAME = "itemName";
	private static final String ITEM_PRICE = "itemPrice";
	private static final String ITEM_CODE = "itemCode";
	private static final String AVAILABILITY = "availability";
	private static final String RECENT_ITEM = "RecentItem";
	private static final String ITEMS = "items";
	private static final String ITEM = "item";
	private static final String PATH = "/"+RECENT_ITEM+"/"+ITEMS+"/"+ITEM;

	private String mFileName = null;
	
	public MyItemFile(String fileName){
		mFileName = fileName;
	}
	
	public String getFileName(){
		return mFileName;
	}
	
	protected void read(List<Item> list) {
		
		try{
			/** 設定 xml の読み込み */		
			FileInputStream input = App.getAppContext().openFileInput(mFileName);
			SAXReader reader = new SAXReader();
			Document document = reader.read(input);

			List<? extends Node> nodes = document.selectNodes(PATH);
			for(int i=0;i<nodes.size();i++){
				Item item = Item.newInstance();
				Node node = (Node)nodes.get(i);
				
				item.setImage(convertString(node.selectSingleNode(ITEM_IMAGE)));
				item.setUrl(convertString(node.selectSingleNode(ITEM_URL)));
				item.setName(convertString(node.selectSingleNode(ITEM_NAME)));
				item.setPrice(Integer.parseInt(convertString(node.selectSingleNode(ITEM_PRICE))));
				item.setCode(convertString(node.selectSingleNode(ITEM_CODE)));
				item.setIsAvailability(convertString(node.selectSingleNode(AVAILABILITY)));
				list.add(item);
			}
		} catch (DocumentException e) {
			Log.e(this.getClass().getSimpleName(),"解析エラー",e);
		} catch (FileNotFoundException e) {
			Log.e(this.getClass().getSimpleName(),"読み込みエラー",e);
		}
	}

	@Override
	public void onChangedMyItem(MyItemList list) {
		
		try{
	    	Document document = DocumentHelper.createDocument();
	    	Element root = document.addElement(RECENT_ITEM).addElement(ITEMS);
			for(int i=0;i<list.size();i++){
				Element item = root.addElement(ITEM);					
				item.addElement(ITEM_NAME).addText(list.get(i).getName());
				item.addElement(ITEM_URL).addText(list.get(i).getUrl());
				item.addElement(ITEM_IMAGE).addText(list.get(i).getImage());
				item.addElement(ITEM_PRICE).addText(Integer.toString(list.get(i).getPrice()));
				item.addElement(ITEM_CODE).addText(list.get(i).getCode());
				item.addElement(AVAILABILITY).addText(""+list.get(i).getIsAvailability());
			}
	    	FileOutputStream outStream = App.getAppContext().openFileOutput(mFileName, Context.MODE_PRIVATE);
		    	
	    	XMLWriter writer = new XMLWriter(outStream);
	    	writer.write(document);
	    	writer.close();
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(),"書き込みエラー",e);
		}		
		    		
	}

	// ファイルの削除
	public void remove() {
		Log.d("MyItemFile",mFileName + "の削除");
		App.getAppContext().deleteFile(mFileName);
	}
}

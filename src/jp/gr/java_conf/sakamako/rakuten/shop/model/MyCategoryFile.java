package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import android.util.Log;

public class MyCategoryFile extends XmlFileHandler{
	private final static String FILE_NAME = "category.xml";
	private final static String ROOT = "root";
	private final static String ELEMENTS = "categories";
	private final static String ELEMENT = "category";
	private final static String NO = "no";
	private final static String LABEL = "label";
	private final static String FILE = "file";
	private static final String PATH = "/"+ROOT+"/"+ELEMENTS+"/"+ ELEMENT;
	
	protected List<Category> read() {
		
		List<Category> list = new ArrayList<Category>();
		try{
			/** 設定 xml の読み込み */		
			//FileInputStream input = App.getAppContext().openFileInput(FILE_NAME);
			FileInputStream input = super.openInput(FILE_NAME);
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
			
	public void write(List<Category> list){
	
		try{
    		Document document = DocumentHelper.createDocument();
    		Element root = document.addElement(ROOT).addElement(ELEMENTS);
			for(int i=0;i<list.size();i++){
				Element item = root.addElement(ELEMENT);
				item.addElement(NO).addText(""+list.get(i).getId());
				item.addElement(LABEL).addText(list.get(i).getLabel());
				item.addElement(FILE).addText(list.get(i).getItemList().getFile().getFileName());
			}
    		//FileOutputStream outStream = App.getAppContext().openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
	    	FileOutputStream outStream = super.openOutput(FILE_NAME);
			
    		XMLWriter writer = new XMLWriter(outStream);
    		writer.write(document);
    		writer.close();
			Log.e(this.getClass().getSimpleName(),"書き込み完了 "+FILE_NAME);
		} catch (IOException e) {
			Log.e(this.getClass().getSimpleName(),"書き込みエラー",e);
		}
	}

}

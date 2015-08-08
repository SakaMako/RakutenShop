package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import jp.gr.java_conf.sakamako.rakuten.shop.App;

import org.dom4j.Node;

import android.content.Context;

public abstract class XmlFileHandler {
	private static final String NONE = "";
	public String convertString(Node node){
		if(node == null){
			return NONE;
		}
		return node.getText();
	}
	
	protected FileInputStream openInput(String fileName) throws FileNotFoundException{
	    File dst = new File(App.getAppContext().getExternalFilesDir(null), fileName);
	    return new FileInputStream(dst);
	}
	protected FileOutputStream openOutput(String fileName) throws FileNotFoundException{
	    File dst = new File(App.getAppContext().getExternalFilesDir(null), fileName);
	    return new FileOutputStream(dst);
	}

}

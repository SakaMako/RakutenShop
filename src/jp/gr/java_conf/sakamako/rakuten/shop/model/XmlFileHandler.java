package jp.gr.java_conf.sakamako.rakuten.shop.model;

import org.dom4j.Node;

public abstract class XmlFileHandler {
	private static final String NONE = "";
	public String convertString(Node node){
		if(node == null){
			return NONE;
		}
		return node.getText();
	}

}

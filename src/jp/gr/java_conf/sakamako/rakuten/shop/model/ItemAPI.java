package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import jp.gr.java_conf.sakamako.rakuten.shop.App;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.xml.sax.SAXException;

import android.util.Log;

public class ItemAPI {
	
	/**
	 * 商品管理番号から一意の商品を１件取得する
	 * @param mngNumber
	 * @return
	 * @throws IOException
	 * @throws SAXException
	 * @throws DocumentException
	 */
	public static Item getItem(Item item) throws IOException, SAXException, DocumentException{
		SearchParams searchParams = new SearchParams("");
		searchParams.setItemCode(item.getCode());
		List<Item> itemList = getItemList(1,searchParams);
		if(itemList.isEmpty()){
			return null;
		}
		
		return itemList.get(0);
	}
	
	
	//https://app.rakuten.co.jp/services/api/IchibaItem/Search/20140222?applicationId=7fd3747dfec7c786998b372d690c98df&affiliateId=03e6fa11.d2147685.03e6fa12.54f60980&format=xml&hits=20&page=1&imageFlag=1&shopCode=amiami&sort=-updateTimestamp&genreId=400962&keyword=%E4%B8%AD%E5%8F%A4

	//https://app.rakuten.co.jp/services/api/IchibaItem/Search/20140222?applicationId=7fd3747dfec7c786998b372d690c98df&affiliateId=03e6fa11.d2147685.03e6fa12.54f60980&format=xml&hits=20&page=1&imageFlag=1&carrier=2&shopCode=amiami&sort=-updateTimestamp&availability=0&genreId=400962&keyword=%E4%B8%AD%E5%8F%A4

	public static List<Item> getItemList(int page,SearchParams searchParams) throws IOException, SAXException,DocumentException{
		List<Item> list = new ArrayList<Item>();
		try{
			String requestPath = "https://app.rakuten.co.jp/services/api/IchibaItem/Search/20140222"
					+ "?applicationId="+ App.getDeveloperId()
					+ "&affiliateId=" + App.getAffiliateId()
					+ "&format=xml"
					+ "&hits=20&page=" + page
					+ "&imageFlag=1"
					//+ "&carrier=2"
					+ "&shopCode=" + URLEncoder.encode(searchParams.getShopUrl(),"utf-8")
					+ "&sort=" + URLEncoder.encode("-updateTimestamp","utf-8")
					;
			
			if(!searchParams.isZaiko()){
				requestPath += "&availability=0";
			}
			if(searchParams.getItemCode() != null){
				requestPath += "&itemCode="+searchParams.getItemCode();
			}
			if(searchParams.getGenreId() != -1){
				requestPath += "&genreId=" + searchParams.getGenreId();
			}
			
			// 検索文字
			String finalSearchKey = searchParams.getSearchString();
			if(finalSearchKey != null && !finalSearchKey.equals("")){
				requestPath += "&keyword=" + URLEncoder.encode(finalSearchKey,"utf-8");				
			}
		
			Log.d(ItemAPI.class.getSimpleName(),"APIのURL="+ requestPath);

			URL requestUrl = new URL(requestPath);			
			SAXReader reader = new SAXReader();
			Document document = reader.read(requestUrl);
			
			List<? extends Node> node = document.selectNodes("/root/Items/Item");
			Log.d(ItemAPI.class.getSimpleName(), "取得した商品数=" + node.size()+"です");
			for(int i=0;i<node.size();i++){
				Item item = Item.newInstanceWithChache();
				
				String nodeCode = ((Element)node.get(i)).selectSingleNode("itemCode").getText();
				item.setCode(nodeCode);
				
				String nodeImage = ((Element)node.get(i)).selectSingleNode("mediumImageUrls").selectSingleNode("imageUrl").getText();
				item.setImage(nodeImage);
				
				String nodeUrl = ((Element)node.get(i)).selectSingleNode("itemUrl").getText();
				item.setUrl(nodeUrl);
				
				String nodeAffiliateUrl = ((Element)node.get(i)).selectSingleNode("affiliateUrl").getText();
				item.setAffiliateUrl(nodeAffiliateUrl);
				
				String nodeName = ((Element)node.get(i)).selectSingleNode("itemName").getText();
				item.setName(nodeName);

				String nodePrice = ((Element)node.get(i)).selectSingleNode("itemPrice").getText();
				item.setPrice(Integer.parseInt(nodePrice));
				
				String nodeAvaliability = ((Element)node.get(i)).selectSingleNode("availability").getText();
				item.setIsAvailability(nodeAvaliability);

				list.add(item);
			}
			
		} catch(UnsupportedEncodingException e){
			throw e;
		} catch (MalformedURLException e) {
			throw e;
		} catch (DocumentException e) {
			throw e;
		} 
		
		return list;
	}

	// page は１オリジン
	public static List<Item> getRankingList(int page) throws Exception{
	
	List<Item> list = new ArrayList<Item>();
	try{
		String requestPath = "https://app.rakuten.co.jp/services/api/IchibaItem/Ranking/20120927"
				+ "?applicationId="+ App.getDeveloperId()
				+ "&affiliateId=" + App.getAffiliateId()
				+ "&format=xml"
				+ "&hits=20&page=" + page
				+ "&imageFlag=1"
				+ "&genreId=" + SearchParams.getGenreId()
				;
			Log.d(ItemAPI.class.getSimpleName(),"getRankingList.URL="+ requestPath);

		URL requestUrl = new URL(requestPath);			
		SAXReader reader = new SAXReader();
		Document document = reader.read(requestUrl);
		
		List<? extends Node> node = document.selectNodes("/root/Items/Item");
		Log.d(ItemAPI.class.getSimpleName(), "取得した商品数=" + node.size()+"です");
		for(int i=0;i<node.size();i++){
			String shopUrl = ((Element)node.get(i)).selectSingleNode("shopCode").getText();
			if(!shopUrl.equals(SearchParams.getShopUrl())) continue;
			
			Item item = Item.newInstanceWithChache();
			
			String nodeCode = ((Element)node.get(i)).selectSingleNode("itemCode").getText();
			item.setCode(nodeCode);
			
			String nodeImage = ((Element)node.get(i)).selectSingleNode("mediumImageUrls").selectSingleNode("imageUrl").getText();
			item.setImage(nodeImage);
			
			String nodeUrl = ((Element)node.get(i)).selectSingleNode("itemUrl").getText();
			item.setUrl(nodeUrl);
			
			String nodeAffiliateUrl = ((Element)node.get(i)).selectSingleNode("affiliateUrl").getText();
			item.setAffiliateUrl(nodeAffiliateUrl);
			
			String nodeName = ((Element)node.get(i)).selectSingleNode("itemName").getText();
			item.setName(nodeName);

			String nodePrice = ((Element)node.get(i)).selectSingleNode("itemPrice").getText();
			item.setPrice(Integer.parseInt(nodePrice));
			
			String nodeAvaliability = ((Element)node.get(i)).selectSingleNode("availability").getText();
			item.setIsAvailability(nodeAvaliability);

			list.add(item);
		}
		
	} catch (MalformedURLException e) {
		e.printStackTrace();
		throw e;
	} catch (DocumentException e) {
		e.printStackTrace();
		throw e;
	} 
	
	return list;
}
}

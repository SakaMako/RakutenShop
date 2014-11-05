package jp.gr.java_conf.sakamako.rakuten.shop.model;

import java.io.Serializable;

import android.util.Log;

public class Item implements Serializable{
	

	private final static long serialVersionUID = 1L;
	
	public final static int ITEM_SIZE_LIST = 355; 
	public final static int ITEM_SIZE_DETAIL = 355;
	
	public final static int AVALIABILITY_OK = 1;
	public final static int AVALIABILITY_NG = 0;
	
	private String image = null;
	private String url = null;
	private String affiliateUrl = null;
	private String name = null;
	private int price = 0;
	private String code = null;
	private int isAvailability = AVALIABILITY_OK;
	private long validTime = -1L;
		
	/**
	 * 複製を作るコンストラクタ
	 * @param item - コピー元
	 */
	
	public static Item newInstance() {
		return new Item(-1L);
	}
	public static Item newInstanceWithChache(){
		return new Item(System.currentTimeMillis());
	}
	
	/**
	 * ただのコンストラクタ
	 */
	private Item(Long l){
		this.validTime = l;
	}
	
	public boolean isValid(){
		if(System.currentTimeMillis() - validTime <= 60 * 60 * 1000) return true;
		return false;
	}
	
	/**
	 * 閲覧履歴で画像をポップアップする際に大きいサイズの画像ＵＲＬを返す
	 * @return
	 */
	public String getLargeImage(){
		return image.replaceAll("128x128", ITEM_SIZE_DETAIL + "x" + ITEM_SIZE_DETAIL);
	}
	
	public String getItemListImage(){
		return image.replaceAll("128x128", ITEM_SIZE_LIST + "x" + ITEM_SIZE_LIST);
	}
	
	/**
	 * デバッグ用の String 取得メソッド
	 */
	public String toString(){
		return "Code = " + code + "\n"
				+ "Name = " + name + "\n"
				+ "Url = " + url + "\n"
				+ "image = " + image + "\n"
				+ "price = " + price;
	}
	
	
	public String getImage() {
		return image;
	}
	public String getUrl(){
		return url;
	}
	public String getName() {
		return name;
	}
	public int getPrice(){
		return price;
	}
	public String getCode(){
		return code;
	}
	public String getPriceString() {
		return String.format("%1$,3d", this.getPrice())+"円";
	}

	public void setImage(String image) {
		this.image = image;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPrice(int price){
		this.price = price;
	}
	public void setCode(String code){
		this.code = code;
	}

	public int getIsAvailability() {
		return isAvailability;
	}

	public void setIsAvailability(String isAvailability) {
		try{
			this.isAvailability = Integer.parseInt(isAvailability);
		}
		catch(NumberFormatException e){
			this.isAvailability = AVALIABILITY_OK;
		}
	}

	public void setAffiliateUrl(String affiliateUrl) {
			this.affiliateUrl = affiliateUrl;
	}
	public String getAffiliateUrl() {
		return affiliateUrl;
	}
	
	@Override
	public boolean equals(Object i){
		//Log.d(this.getClass().getSimpleName(),"equals--------------------------");

		//Log.d(this.getClass().getSimpleName(),"org="+this.getCode()+","+this.getName());
		//Log.d(this.getClass().getSimpleName(),"com="+((Item)i).getCode()+","+((Item)i).getName());
		boolean r =  this.getCode().equals(((Item)i).getCode());		
		//Log.d(this.getClass().getSimpleName(),"result="+r+"--------------------------");
		
		
		return r;		
	}

}

package jp.gr.java_conf.sakamako.rakuten.shop.model;


public class Category {
		private int mId = -1;
		private String mLabel = null;
		private MyItemList mList = null;
		
		public Category(int id,String text,String fileName){
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

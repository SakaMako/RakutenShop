package jp.gr.java_conf.sakamako.rakuten.shop.home;

import java.util.Calendar;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.App;
import android.app.Activity;
import android.util.Log;
import android.widget.ArrayAdapter;

public class DrawerMenuAdapter extends ArrayAdapter<String>{

	public DrawerMenuAdapter(Activity activity) {
		super(activity.getApplicationContext(), R.layout.drawer_list_menu);
		if(App.getDrawerMenuByReserve()){
			Calendar cal = Calendar.getInstance();
			for(int i=0;i<6;i++){
				int m = cal.get(Calendar.MONTH)+1;
				super.add(String.format("《%s月予約》"
	    			 ,hanNum2ZenNum(String.format("%02d", m))));
				cal.add(Calendar.MONTH, 1);
			}
		}
	     
		String[] c = activity.getResources().getStringArray(R.array.drawer_menu_list);
		Log.d("DrawerMenuAdapter","length=" + c.length);
		for(int i=0;i<c.length;i++){
			super.add(c[i]);
		}
		
	}
    // 半角数字を全角数字に変換
    public static String hanNum2ZenNum(String s) {

        StringBuffer sb = new StringBuffer(s);
        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            if (c >= '0' && c <= '9') {

                sb.setCharAt(i, (char)(c - '0' + '０'));
            }
        }

        return sb.toString();
    }
}

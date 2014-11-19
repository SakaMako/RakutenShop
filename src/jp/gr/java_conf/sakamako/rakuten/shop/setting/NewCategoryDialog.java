package jp.gr.java_conf.sakamako.rakuten.shop.setting;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewCategoryDialog extends DialogFragment {
	
	//private DialogFragment mDialog = null;
	private EditText mTextView = null;
	private OnNewCategoryListener mListener = null;
	
	public interface OnNewCategoryListener{
		public boolean onNewCategory(String s);
	}
	
	public NewCategoryDialog(OnNewCategoryListener listener){
		super();
		mListener  = listener;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	//mDialog = this;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.new_category_dialog, null, false);
        
        mTextView = (EditText)view.findViewById(R.id.category_name);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("カテゴリの追加");
        builder.setPositiveButton("決定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        //builder.setNegativeButton("キャンセル", null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AlertDialog) getDialog()).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {            
            @Override
            public void onClick(View v)
            {
                String text = mTextView.getText().toString();
                if(!mListener.onNewCategory(text)){
                	Log.d("NewCategoryDialogFragment","カテゴリの重複"+text);
                	Toast.makeText(getActivity(), "["+text+"]は重複してます", 10).show();
                }
                else{
                	dismiss();
                }
            }
        });
    }

}

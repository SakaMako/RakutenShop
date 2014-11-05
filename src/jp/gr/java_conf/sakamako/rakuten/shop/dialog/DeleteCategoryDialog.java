package jp.gr.java_conf.sakamako.rakuten.shop.dialog;

import jp.gr.java_conf.sakamako.rakuten.shop.R;
import jp.gr.java_conf.sakamako.rakuten.shop.model.MyCategory.Category;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DeleteCategoryDialog extends DialogFragment {
	
	private TextView mTextView = null;
	private Category mCat = null;
	private DeleteCategoryListener mListener = null;
	
	public interface DeleteCategoryListener{
		public void onDeleteCategory();
	};
	
	public DeleteCategoryDialog(Category cat,DeleteCategoryListener listener){
		mCat = cat;
		mListener = listener;
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_category_dialog, null, false);
        
        mTextView = (TextView)view.findViewById(R.id.delete_category_name);
        mTextView.setText(mCat.getLabel());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("カテゴリの削除");
        builder.setPositiveButton("決定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	mListener.onDeleteCategory();
            }
        });
        builder.setNegativeButton("キャンセル", null);
        builder.setView(view);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }
}

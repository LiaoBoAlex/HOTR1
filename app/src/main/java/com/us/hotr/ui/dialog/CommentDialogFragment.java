package com.us.hotr.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;

/**
 * Created by Mloong on 2017/9/15.
 */

public class CommentDialogFragment extends Dialog {
    public CommentDialogFragment(Context context) {
        super(context);
    }

    public CommentDialogFragment(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        TextView tvPost;
        EditText etPost;
        ImageView ivCancel;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private PostListener postListener;
        private String userName;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder(Context context, String userName) {
            this.context = context;
            this.userName = userName;
        }

        public CommentDialogFragment.Builder setCancelButton(DialogInterface.OnClickListener listener) {
            this.negativeButtonClickListener = listener;
            return this;
        }

        public CommentDialogFragment.Builder setPostButton(PostListener listener){
            this.postListener = listener;
            return this;
        }

        public CommentDialogFragment create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CommentDialogFragment dialog = new CommentDialogFragment(context,
                    R.style.NoTitleDialog);
            View layout = inflater.inflate(R.layout.dialog_comment, null);
            tvPost = (TextView) layout.findViewById(R.id.tv_post);
            etPost = (EditText) layout.findViewById(R.id.et_comment);
            ivCancel = (ImageView) layout.findViewById(R.id.iv_cancel);
            if(userName != null)
                etPost.setHint(String.format(context.getString(R.string.reply2), userName));
            dialog.addContentView(layout, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            dialog.setCanceledOnTouchOutside(true);

            tvPost.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (postListener != null) {
                        postListener.onClick(dialog,
                               etPost.getText().toString().trim());
                    }

                }
            });

            ivCancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onClick(dialog,
                                DialogInterface.BUTTON_NEGATIVE);
                    }

                }
            });

            dialog.setContentView(layout);
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            return dialog;
        }
    }

    public interface PostListener{
        void onClick(DialogInterface dialog, String content);
    }
}

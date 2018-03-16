package com.us.hotr.customview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.us.hotr.R;


/**
 * User: cpoopc
 * Date: 2015-09-01
 * Time: 00:18
 */
public class ChatSoftInputLayout extends BaseSoftInputLayout {

    // emotionView,otherView容器
    private View container;

//    private View btnEmotion;
//    private EmotionPager emotionView;

    private ImageView btnOther;
//    private View otherView;

    private View frame;
    private TextView btnSend;
    private RecyclerView recyclerView;
    private EditText editText;
    private LinearLayout llCamera, llPhoto;

    public ChatSoftInputLayout(Context context) {
        super(context);
    }

    public ChatSoftInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChatSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatSoftInputLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflateView() {
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.chat_softinput_layout, this, true);
        container = layout.findViewById(R.id.cl_option);
        frame = layout.findViewById(R.id.frame);
        editText = (EditText) layout.findViewById(R.id.et_reply);
        btnSend = (TextView) layout.findViewById(R.id.tv_send);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recyclerview);
        llCamera = (LinearLayout) layout.findViewById(R.id.ll_camera);
        llPhoto = (LinearLayout) layout.findViewById(R.id.ll_photo);
        setupOtherView(layout);

        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                    hideKeyBoardView();
                return false;
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editText.getText().toString().trim().isEmpty()) {
                    btnOther.setVisibility(View.VISIBLE);
                    btnSend.setVisibility(View.GONE);
                }else{
                    btnOther.setVisibility(View.GONE);
                    btnSend.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupOtherView(View layout) {
        btnOther = (ImageView) layout.findViewById(R.id.iv_option);
        btnOther.setOnClickListener(this);
        add2ShowViewList(container);
        add2MappingMap(btnOther, SHOW_OTHER, container);
    }

    @Override
    protected View getContainer() {
        return container;
    }

    @Override
    protected View getFrame() {
        return frame;
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    @Override
    protected View getBtnKeyBoard() {
        return null;
    }

    public void setOnSendClickListener(OnClickListener onSendClickListener) {
        btnSend.setOnClickListener(onSendClickListener);


    }

    public void setOnPhotoClickListener(OnClickListener onSendClickListener) {
        llPhoto.setOnClickListener(onSendClickListener);
    }

    public void setOnCameraClickListener(OnClickListener onSendClickListener) {
        llCamera.setOnClickListener(onSendClickListener);
    }

    public String getEdittextContent() {
        return editText.getText().toString().trim();
    }

    public void clearEdittextContent(){
        editText.setText("");
    }

}

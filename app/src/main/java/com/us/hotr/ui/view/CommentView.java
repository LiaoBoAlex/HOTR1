package com.us.hotr.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import com.us.hotr.R;

public class CommentView extends ConstraintLayout {

    ImageView iv1, iv2, iv3, iv4, iv5;

    public CommentView(@NonNull Context context) {
        super(context);
        init();
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        LayoutInflater.from(getContext()).inflate(R.layout.view_comment, this);
        iv1 = (ImageView) findViewById(R.id.iv_1);
        iv2 = (ImageView) findViewById(R.id.iv_2);
        iv3 = (ImageView) findViewById(R.id.iv_3);
        iv4 = (ImageView) findViewById(R.id.iv_4);
        iv5 = (ImageView) findViewById(R.id.iv_5);
    }

    public void setMark(double mark){
        if(mark == 5){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star_selected);
            iv4.setImageResource(R.mipmap.ic_star_selected);
            iv5.setImageResource(R.mipmap.ic_star_selected);
        }else if (mark<5 && mark > 4){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star_selected);
            iv4.setImageResource(R.mipmap.ic_star_selected);
            iv5.setImageResource(R.mipmap.ic_star);
        }else if (mark == 4){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star_selected);
            iv4.setImageResource(R.mipmap.ic_star_selected);
            iv5.setVisibility(INVISIBLE);
        }else if (mark < 4 && mark > 3){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star_selected);
            iv4.setImageResource(R.mipmap.ic_star);
            iv5.setVisibility(INVISIBLE);
        }else if (mark == 3){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star_selected);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else if (mark > 2 && mark < 3){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setImageResource(R.mipmap.ic_star);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else if (mark == 2){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star_selected);
            iv3.setVisibility(INVISIBLE);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else if (mark > 1 && mark < 2){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setImageResource(R.mipmap.ic_star);
            iv3.setVisibility(INVISIBLE);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else if(mark == 1){
            iv1.setImageResource(R.mipmap.ic_star_selected);
            iv2.setVisibility(INVISIBLE);
            iv3.setVisibility(INVISIBLE);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else if (mark > 0 && mark < 1){
            iv1.setImageResource(R.mipmap.ic_star);
            iv2.setVisibility(INVISIBLE);
            iv3.setVisibility(INVISIBLE);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }else{
            iv1.setVisibility(INVISIBLE);
            iv2.setVisibility(INVISIBLE);
            iv3.setVisibility(INVISIBLE);
            iv4.setVisibility(INVISIBLE);
            iv5.setVisibility(INVISIBLE);
        }
    }
}

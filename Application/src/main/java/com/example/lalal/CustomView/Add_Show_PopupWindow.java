package com.example.lalal.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.lalal.MyNewGraduate.R;

public class Add_Show_PopupWindow extends PopupWindow {

    private Button takePhotoBtn, pickPhotoBtn, cancelBtn;
    private View mMenuView;

    @SuppressLint("InflateParams")
    public Add_Show_PopupWindow(Context context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.activity_add__show__popup_window, null);
        takePhotoBtn = (Button) mMenuView.findViewById(R.id.takePhotoBtn);
        pickPhotoBtn = (Button) mMenuView.findViewById(R.id.pickPhotoBtn);
        cancelBtn = (Button) mMenuView.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(itemsOnClick);
        pickPhotoBtn.setOnClickListener(itemsOnClick);
        takePhotoBtn.setOnClickListener(itemsOnClick);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PAnimation);
        ColorDrawable dw = new ColorDrawable(0x80000000);
        this.setBackgroundDrawable(dw);
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }
}

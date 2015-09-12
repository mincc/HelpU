package com.example.chungmin.helpu;

/**
 * Created by Chung Min on 8/28/2015.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewColon extends TextView {

    public String addColon = "";

    public TextViewColon(Context context) {
        super(context);
    }

    public TextViewColon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TextViewColon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }


    public void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TextViewColon);

        addColon = ta.getString(R.styleable.TextViewColon_addColon);
        if (addColon.equals("true")) {
            int[] set = {
                    android.R.attr.text,        // idx 0
            };
            TypedArray taText = context.obtainStyledAttributes(attrs, set);
            CharSequence text = taText.getText(0);
            setText(text + " : ");
            taText.recycle();
        }
        ta.recycle();

    }
}
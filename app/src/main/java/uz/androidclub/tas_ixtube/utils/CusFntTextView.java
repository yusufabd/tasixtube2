package uz.androidclub.tas_ixtube.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class CusFntTextView extends TextView {

    public CusFntTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CusFntTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CusFntTextView(Context context) {
        super(context);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
//            Typeface tf = FontUtil.get(getContext(), "comfortaa.ttf");
            Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/comfortaa.ttf");
            setTypeface(tf);
        }
    }
}
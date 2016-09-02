package com.anna.lozytska.achievementstimer.ui.widget;

import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;

/**
 * Created by Ann Lozytska on 01.09.16.
 */
public class HorizontalTextInputLayout extends TextInputLayout {
    public HorizontalTextInputLayout(Context context) {
        this(context, null);
    }

    public HorizontalTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(HORIZONTAL);
    }
}

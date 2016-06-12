package com.stest.OtherhelperClass;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by ldy on 6/12/16.
 */
public class NoSclideViewpager extends ViewPager {
    private boolean noslide=true;

    public NoSclideViewpager(Context context) {
        super(context);
    }

    public NoSclideViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean isNoslide() {
        return noslide;
    }

    public void setNoslide(boolean noslide) {
        this.noslide = noslide;
    }

    @Override
    public void scrollTo(int x, int y) {
        super.scrollTo(x, y);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(noslide==false)
        return super.onTouchEvent(ev);
        else
            return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(noslide==false)
        return super.onInterceptTouchEvent(ev);
        else
            return false;
    }
}

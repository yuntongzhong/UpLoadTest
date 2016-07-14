package com.example.zyt.uploadtest.ui.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 父viewpager，用于解决嵌套viewpager
 */
public class ParentViewPager extends ViewPager {

	//设置是否可以滑动，默认为true
	private boolean scrollble = true;

	public ParentViewPager(Context context) {
		super(context);
	}

	public ParentViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// 设置不能滑动
		if (scrollble) {
			return super.onTouchEvent(ev);
		}
		return false;
	}
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (scrollble) {
            return super.onInterceptTouchEvent(event);
        }
 
        return false;
    }
	public boolean isScrollble() {
		return scrollble;
	}

	public void setScrollble(boolean scrollble) {
		this.scrollble = scrollble;
	}
}

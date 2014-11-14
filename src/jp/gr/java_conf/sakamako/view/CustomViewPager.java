package jp.gr.java_conf.sakamako.view;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.Scroller;

// http://stackoverflow.com/questions/8155257/slowing-speed-of-viewpager-controller-in-android

public class CustomViewPager extends ViewPager {
	   public CustomViewPager(Context context) {
	        super(context);
	        postInitViewPager();
	    }

	    public CustomViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	        postInitViewPager();
	    }

	    private ScrollerCustomDuration mScroller = null;

	    /**
	     * Override the Scroller instance with our own class so we can change the
	     * duration
	     */
	    private void postInitViewPager() {

	        try {
	            Class<?> viewpager = ViewPager.class;
	            Field scroller = viewpager.getDeclaredField("mScroller");
	            scroller.setAccessible(true);
	            Field interpolator = viewpager.getDeclaredField("sInterpolator");
	            interpolator.setAccessible(true);

	            mScroller = new ScrollerCustomDuration(getContext(),
	                    (Interpolator) interpolator.get(null));
	            scroller.set(this, mScroller);
				this.setOffscreenPageLimit(4);
				this.setScrollDurationFactor(5);
	        } catch (Exception e) {
	        }
	    }

	    /**
	     * Set the factor by which the duration will change
	     */
	    public void setScrollDurationFactor(double scrollFactor) {
	        mScroller.setScrollDurationFactor(scrollFactor);
	    }
	    
	    private class ScrollerCustomDuration extends Scroller {

	        private double mScrollFactor = 1;

	        public ScrollerCustomDuration(Context context) {
	            super(context);
	        }

	        public ScrollerCustomDuration(Context context, Interpolator interpolator) {
	            super(context, interpolator);
	        }

	        @SuppressLint("NewApi")
	        public ScrollerCustomDuration(Context context, Interpolator interpolator, boolean flywheel) {
	            super(context, interpolator, flywheel);
	        }

	        /**
	         * Set the factor by which the duration will change
	         */
	        public void setScrollDurationFactor(double scrollFactor) {
	            mScrollFactor = scrollFactor;
	        }

	        @Override
	        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
	            super.startScroll(startX, startY, dx, dy, (int) (duration * mScrollFactor));
	        }
	    }

}

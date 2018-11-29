package com.example.mylibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * @author wzw on 2018/11/29
 * 只支持一个控件高亮
 */
public class SimpleGuideView extends FrameLayout {
    View mTarget;
    View mGuideView;
    Paint mPaint;
    RectF mRectF;
    private Direction mDirection;
    private int mRadius;
    private FrameLayout mParentView;
    private int mHighLightPadding;
    private int mBgColor;
    private boolean mAnimation;
    private OnViewGoneListener mViewGoneListener;
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    public SimpleGuideView(@NonNull Context context, Build build) {
        super(context);
        init(build);
    }

    public SimpleGuideView(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
        init(new Build(context));
    }

    public SimpleGuideView(@NonNull Context context, AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(new Build(context));
    }

    private void init(Build build) {
        if (build != null) {
            mGuideView = build.guideView;
            mTarget = build.lightView;
            mDirection = build.direction;
            mRadius = build.radius;
            mHighLightPadding = build.highLightPadding;
            mBgColor = build.bgColor;
            mAnimation = build.animation;
            mViewGoneListener = build.viewGoneListener;

            View clickableView = mGuideView.findViewById(build.clickableViewId);
            if (clickableView != null) {
                clickableView.setOnClickListener(clickListener);
            } else {
                mGuideView.setOnClickListener(clickListener);
            }
        }

        //viewGroup默认不走onDraw方法
        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        // 高亮的关键,需关闭硬件加速
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        mPaint.setXfermode(xfermode);
        //设置画笔遮罩滤镜,可以传入BlurMaskFilter或EmbossMaskFilter，前者为模糊遮罩滤镜而后者为浮雕遮罩滤镜
        //这个方法已经被标注为过时的方法了，如果你的应用启用了硬件加速，你是看不到任何阴影效果的
        if (mRadius > 0) {
            mPaint.setMaskFilter(new BlurMaskFilter(mRadius, BlurMaskFilter.Blur.INNER));
        }
        //关闭当前view的硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        //解决点击穿透问题
        return true;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (GONE == getVisibility()) {
            setVisibility(VISIBLE);
        }

        if (mAnimation) {
            animate().setListener(null).alpha(1);
        }

        mRectF = getRectF();
        LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (Direction.TOP.equals(mDirection)) {
            lp.bottomMargin = (int) mRectF.top;
        } else if (Direction.BOTTOM.equals(mDirection)) {
            lp.topMargin = (int) mRectF.bottom;
        } else {
            lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        }
        addView(mGuideView, lp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 画半透明背景
        canvas.drawColor(mBgColor);
        // 画高亮
        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }

    public void show() {
        Activity activity = (Activity) getContext();
        mParentView = activity.findViewById(android.R.id.content);
        mParentView.post(new Runnable() {
            @Override
            public void run() {
                mParentView.addView(SimpleGuideView.this, new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            }
        });
    }

    public void dismiss() {
        if (mAnimation) {
            //动画时长默认300ms
            animate().alpha(0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    release();
                }
            });
        } else {
            release();
        }

        if (mViewGoneListener != null) {
            mViewGoneListener.onViewGone();
        }
    }

    private void release() {
        removeView(mGuideView);
        mParentView.removeView(SimpleGuideView.this);
    }

    private RectF getRectF() {
        // 获取mTarget在mParentView中的位置
        Rect l = getLocationInView(mParentView, mTarget);
        return new RectF(l.left - mHighLightPadding, l.top - mHighLightPadding, l.right + mHighLightPadding, l.bottom + mHighLightPadding);
    }

    /**
     * 获取要高亮的子控件在指定父控件中的位置
     */
    private static Rect getLocationInView(@NonNull View parent, @NonNull View child) {
        View decorView = null;
        Context context = child.getContext();
        if (context instanceof Activity) {
            decorView = ((Activity) context).getWindow().getDecorView();
        }

        Rect result = new Rect();
        Rect tmpRect = new Rect();

        View tmpChild = child;

        if (child == parent) {
            // 获取View 被点击产生响应的对应 Rect 矩形区域。
            child.getHitRect(result);
            return result;
        }
        while (tmpChild != decorView && tmpChild != parent) {
            tmpChild.getHitRect(tmpRect);
            result.left += tmpRect.left;
            result.top += tmpRect.top;
            tmpChild = (View) tmpChild.getParent();
            if (tmpChild == null) {
                throw new IllegalArgumentException("the view is not showing in the window!");
            }

            //fix bug the wrong rect user will received in ViewPager
            if (tmpChild.getParent() != null && (tmpChild.getParent() instanceof ViewPager)) {
                tmpChild = (View) tmpChild.getParent();
            }
        }
        result.right = result.left + child.getMeasuredWidth();
        result.bottom = result.top + child.getMeasuredHeight();
        return result;
    }

    /**
     * 定义GuideView相对于targetView的方位，
     */
    public enum Direction {
        TOP, BOTTOM, FULL
    }

    public interface OnViewGoneListener {
        void onViewGone();
    }

    public static class Build {
        private static final int COLOR_TRANSLUCENT = 0x80000000;
        View lightView;
        View guideView;
        private Direction direction;
        private int radius;
        private int highLightPadding;
        private int bgColor;
        private int clickableViewId;
        private OnViewGoneListener viewGoneListener;
        private Context context;
        private boolean animation;

        public Build(Context context) {
            this.context = context;
            direction = Direction.BOTTOM;
            radius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                    context.getResources().getDisplayMetrics());
            highLightPadding = radius;
            bgColor = COLOR_TRANSLUCENT;
        }

        public SimpleGuideView build() {
            return new SimpleGuideView(context, this);
        }

        /**
         * 要高亮的控件
         */
        public Build setHighLightView(@NonNull View highLightView) {
            this.lightView = highLightView;
            return this;
        }

        /**
         * 引导页布局
         */
        public Build setGuildView(@NonNull View guideView) {
            this.guideView = guideView;
            return this;
        }

        /**
         * 引导页布局
         */
        public Build setGuildView(@LayoutRes int resId) {
            this.guideView = View.inflate(context, resId, null);
            return this;
        }

        /**
         * 指定可点击的View及MyGuideView的隐藏监听；
         * 不设置，默认监听{@link #guideView}的点击事件
         */
        public Build setClickableView(@IdRes int clickableViewId, OnViewGoneListener viewGoneListener) {
            this.clickableViewId = clickableViewId;
            this.viewGoneListener = viewGoneListener;
            return this;
        }

        /**
         * 设置引导页布局的相对lightView的位置
         */
        public Build setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        /**
         * 设置高亮的圆角
         */
        public Build setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        /**
         * 设置高亮目标的padding值
         */
        public Build setHighLightPadding(int highLightPadding) {
            this.highLightPadding = highLightPadding;
            return this;
        }

        /**
         * 背景色，默认 {@link #COLOR_TRANSLUCENT}
         */
        public Build setBgColor(@ColorInt int bgColor) {
            this.bgColor = bgColor;
            return this;
        }

        /**
         * Alpha动画300ms
         */
        public Build setAlphaAnimation(boolean animation) {
            this.animation = animation;
            return this;
        }
    }
}

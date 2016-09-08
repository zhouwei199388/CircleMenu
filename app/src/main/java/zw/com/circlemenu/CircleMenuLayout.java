package zw.com.circlemenu;

import android.content.Context;
import android.media.tv.TvContentRating;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Admin on 2016/9/6.
 */
public class CircleMenuLayout extends ViewGroup {

    private int mRadius;
    //该容器内child item的默认尺寸
    private static final float RADIO_DEFAULT_CHILD_DIMENSION = 1 / 4f;
    //菜单的中心child的默认尺寸
    private float RADIO_DEFAULT_CANTERITEM_DIMENSION = 1 / 3F;

    //该容器的内边距，无视padding属性
    private static final float RADIO_PADDING_LAYOUT = 1 / 12F;
    //当每秒移动角度达到该值时，是快速移动
    private static final int FLINGABLE_VALUE = 300;
    //如果移动角度达到该值， 屏蔽点击
    private static final int NOCLICK_VALUE = 3;
    //当每秒移动角度达到该值时，是快速移动
    private int mFlingableValue = FLINGABLE_VALUE;

    //该容器的内边距  无视padding属性
    private float mPadding;

    //布局开始角度
    private double mStartAngle = 0;

    //菜单项文本
    private String[] mItemTexts;

    //菜单项图标
    private int[] mItemImgs;

    //菜单的个数
    private int mMenuItemCount;
    //检测按下到抬起的旋转角度
    private float mTmpAngle;

    //检测按下到抬起时使用时间
    private long mDownTimes;

    //判断是否正在滚动
    private boolean isFling;


    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int layoutRadius = mRadius;
        final int childCount = getChildCount();
        int left, top;

        int cWidth = (int) (layoutRadius * RADIO_DEFAULT_CHILD_DIMENSION);

        float angleDelay = 360 / (getChildCount() - 1);

        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (child.getId() == R.id.id_circle_menu_item_center) {
                continue;
            }
            if (child.getVisibility() == GONE) {
                continue;
            }

            mStartAngle %= 360;
            float tmp = layoutRadius / 2f - cWidth / 2 - mPadding;


            left = layoutRadius / 2 + (int) Math.round(tmp * Math.cos(Math.toRadians(mStartAngle)
            ) - 1 / 2f * cWidth);

            top = layoutRadius / 2 + (int) Math.round(tmp * Math.sin(Math.toRadians(mStartAngle))
                    - 1 / 2f * cWidth);

            child.layout(left, top, left + cWidth, top + cWidth);
            mStartAngle += angleDelay;
        }
        View cView = findViewById(R.id.id_circle_menu_item_center);
        if (cView != null) {
            cView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnMenuItemClickListener != null) {
                        mOnMenuItemClickListener.itemCenterClick(v);
                    }
                }
            });
            int cl = layoutRadius / 2 - cView.getMeasuredWidth() / 2;
            int cr = cl + cView.getMeasuredWidth();
            cView.layout(cl, cl, cr, cr);
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


        int resWidth = 0;
        int resHeight = 0;


        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);


        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            resWidth = getSuggestedMinimumWidth();

            resWidth = resWidth == 0 ? getDefaultWidth() : resWidth;

            resHeight = getSuggestedMinimumHeight();

            resHeight = resHeight == 0 ? getDefaultWidth() : resHeight;
        } else {
            resWidth = resHeight = Math.min(width, height);
        }
        setMeasuredDimension(resWidth, resHeight);

        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight());

        final int count = getChildCount();

        int childSize = (int) (mRadius * RADIO_DEFAULT_CHILD_DIMENSION);
        int childMode = MeasureSpec.EXACTLY;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }

            int makeMeasureSpec = -1;
            if (child.getId() == R.id.id_circle_menu_item_center) {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius *
                                RADIO_DEFAULT_CANTERITEM_DIMENSION),
                        childMode);
            } else {
                makeMeasureSpec = MeasureSpec.makeMeasureSpec(childSize, childMode);
            }
            child.measure(makeMeasureSpec, makeMeasureSpec);
        }
        mPadding = RADIO_PADDING_LAYOUT * mRadius;
    }


    private float mLastX;
    private float mLastY;
    private AutoFlingRunnable mFlingRunable;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        float x = ev.getX();
        float y = ev.getY();

        Log.e("TAG", "X:  " + x + "Y:  " + y);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = x;
                mLastY = y;
                mDownTimes = System.currentTimeMillis();
                if (isFling) {
                    removeCallbacks(mFlingRunable);
                    isFling = false;
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                float start = getAngle(mLastX, mLastY);
                float end = getAngle(x, y);
                if (getQuadrant(x, y) == 1 || getQuadrant(x, y) == 4) {
                    mStartAngle += end - start;
                    mTmpAngle += end - start;
                }
                requestLayout();
                mLastX = x;
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
                float anglePerSecond = mTmpAngle * 1000 / (System.currentTimeMillis() - mDownTimes);
                if (Math.abs(anglePerSecond) > mFlingableValue && !isFling) {
                    post(mFlingRunable = new AutoFlingRunnable(anglePerSecond));
                    return true;
                }
                if (Math.abs(mTmpAngle) > NOCLICK_VALUE) {
                    return true;
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private int getDefaultWidth() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics outMetrics = new DisplayMetrics();

        wm.getDefaultDisplay().getMetrics(outMetrics);
        return Math.min(outMetrics.widthPixels, outMetrics.heightPixels);
    }

    public void setMenuItemIconsAndTexts(int[] resIds, String[] texts) {
        mItemImgs = resIds;
        mItemTexts = texts;

        if (resIds == null && texts == null) {
            throw new IllegalArgumentException("icon and texts is null");
        }
        mMenuItemCount = resIds == null ? texts.length : resIds.length;

        if (resIds != null && texts != null) {
            mMenuItemCount = Math.min(resIds.length, texts.length);
        }
        addMenuItems();
    }

    private void addMenuItems() {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        for (int i = 0; i < mMenuItemCount; i++) {
            final int j = i;

            View view = inflater.inflate(R.layout.item_cirle_menu, this, false);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_menu);
            TextView contentTv = (TextView) view.findViewById(R.id.tv_menu);

            if (iv != null) {
                iv.setVisibility(VISIBLE);
                iv.setImageResource(mItemImgs[i]);
                iv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnMenuItemClickListener != null) {
                            mOnMenuItemClickListener.itemClick(v, j);
                        }
                    }
                });
            }

            if (contentTv != null) {
                contentTv.setVisibility(VISIBLE);
                contentTv.setText(mItemTexts[i]);
            }

            addView(view);
        }
    }

    private float getAngle(float xTouch, float yTouch) {
        double x = xTouch - (mRadius / 2d);
        double y = yTouch - (mRadius / 2d);
        return (float) (Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
    }

    private float getQuadrant(float x, float y) {
        int tmpX = (int) (x - mRadius / 2);
        int tmpY = (int) (y - mRadius / 2);
        if (tmpX >= 0) {
            return tmpY > 0 ? 4 : 1;
        } else {
            return tmpY > 0 ? 3 : 2;
        }
    }

    private class AutoFlingRunnable implements Runnable {
        private float angelPerSecond;

        public AutoFlingRunnable(float velocity) {
            this.angelPerSecond = velocity;
        }

        @Override
        public void run() {
            if ((int) Math.abs(angelPerSecond) < 20) {
                isFling = false;
                return;
            }
            isFling = true;
            mStartAngle += (angelPerSecond / 30);
            angelPerSecond /= 1.0666F;
            postDelayed(this, 30);
            requestLayout();
        }
    }


    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

}

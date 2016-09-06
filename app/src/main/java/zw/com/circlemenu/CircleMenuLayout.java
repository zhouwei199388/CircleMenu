package zw.com.circlemenu;

import android.content.Context;
import android.media.tv.TvContentRating;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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

    //判断是否正在滚动
    private boolean isFling;


//    public CircleMenuLayout(Context context) {
//        super(context);
//    }

    public CircleMenuLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 0, 0, 0);
    }

//    public CircleMenuLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

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
                makeMeasureSpec = MeasureSpec.makeMeasureSpec((int) (mRadius * RADIO_DEFAULT_CANTERITEM_DIMENSION),
                        childMode);
                child.measure(makeMeasureSpec, makeMeasureSpec);
            }
        }

        mPadding = RADIO_PADDING_LAYOUT * mRadius;
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


    public interface OnMenuItemClickListener {
        void itemClick(View view, int pos);

        void itemCenterClick(View view);
    }

    private OnMenuItemClickListener mOnMenuItemClickListener;

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

}

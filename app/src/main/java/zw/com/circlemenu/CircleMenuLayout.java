package zw.com.circlemenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

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

    }
}

package zw.com.circlemenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Admin on 2016/9/6.
 */
public class MainActivity extends Activity {

//    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财",
//            "转账汇款", "我的账户"};


    private int[] mItemImgs = new int[]{R.drawable.home_mbank_1_normal,
            R.drawable.home_mbank_2_normal, R.drawable.home_mbank_3_normal,
            R.drawable.home_mbank_4_normal, R.drawable.home_mbank_5_normal};


    private zw.com.circlemenu.view.CircleMenuLayout mCircleMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCircleMenu = (zw.com.circlemenu.view.CircleMenuLayout) findViewById(R.id.menu_layout);
//        mCircleMenu.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        mCircleMenu.setMenuItemLayoutId(R.layout.item_cirle_menu);
        mCircleMenu.setMenuItemIconsAndTexts(mItemImgs);

        mCircleMenu.setOnMenuItemClickListener(new zw.com.circlemenu.view.CircleMenuLayout
                .OnMenuItemClickListener() {


            @Override
            public void itemClick(View view, int pos) {
//                Toast.makeText(MainActivity.this, mItemTexts[pos], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemCenterClick(View view) {
                Toast.makeText(MainActivity.this, "center view", Toast.LENGTH_SHORT).show();
            }
        });
//        mCircleMenu.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
//            @Override
//            public void itemClick(View view, int pos) {
//                Toast.makeText(MainActivity.this, mItemTexts[pos], Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void itemCenterClick(View view) {
//                Toast.makeText(MainActivity.this, "center view", Toast.LENGTH_SHORT).show();
//            }
//        });
    }
}

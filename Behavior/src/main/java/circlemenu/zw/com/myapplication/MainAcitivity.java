package circlemenu.zw.com.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2016/9/22.
 */
public class MainAcitivity extends Activity {

    private List<String> mList = new ArrayList<>();
    private ListView mListView;
    private LinearLayout mBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.listview);
        mBehavior = (LinearLayout) findViewById(R.id.ll_behavior);
        setList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R
                .layout.simple_list_item_1, mList);
        mListView.setAdapter(adapter);
    }


    private void setList() {
        for (int i = 0; i < 30; i++) {
            mList.add("张三 " + i);
        }
    }
}

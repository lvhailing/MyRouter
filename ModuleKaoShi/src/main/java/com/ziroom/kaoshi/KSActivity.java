package com.ziroom.kaoshi;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 *
 * 路径最好用:  /当前类所属模块/当前类的直接父包名/当前类名
 */

public class KSActivity extends AppCompatActivity {
    private static final String TAG = "KSActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ks);

        Uri data = getIntent().getData();
        if (data != null) {
            String uid = data.getQueryParameter("uid");
            Log.i(TAG, "uid: " + uid);
        }
    }

}

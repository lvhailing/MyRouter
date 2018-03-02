package com.ziroom.weixiao;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ziroom.processor.annotation.DependInsertOnParam;
import com.ziroom.processor.annotation.PathOnClass;
import com.ziroom.routermiddleware.RouterMiddleware;
import com.ziroom.routermiddleware.denpendency.IDependency;

/**
 *
 * 路径最好用:  /当前类所属模块/当前类的直接父包名/当前类名
 */

@PathOnClass("/ModuleWeiXiao/weixiao/WXActivity")
public class WXActivity extends AppCompatActivity {
    private static final String TAG = "WXActivity";

    @DependInsertOnParam("/RouterMiddleware/dependency/KaoShiDependencyImp")
    private IDependency iDependency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_wx);

        Log.i(TAG, "onCreate");

        RouterMiddleware.getInstance().getDependInjectRouter().inject(this);
    }

    public void click(View v) {
        iDependency.myToast(this);
    }

}

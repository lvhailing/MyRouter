package com.ziroom.routermiddleware.denpendency;


import android.content.Context;
import android.widget.Toast;

import com.ziroom.processor.annotation.DependencyOnClass;

/**
 * Created by lvhl on 2017/3/2.
 * 路径最好用:  /当前类所属模块/当前类的直接父包名/当前类名
 */

@DependencyOnClass("/RouterMiddleware/dependency/KaoShiDependencyImp")
public class KaoShiDependencyImp implements IDependency {

    @Override
    public void myToast(Context context) {
        Toast.makeText(context, "show hello from kaoshi", Toast.LENGTH_SHORT).show();
    }
}

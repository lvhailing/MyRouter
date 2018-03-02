package com.ziroom.routermiddleware.denpendency;


import android.content.Context;
import android.widget.Toast;

import com.ziroom.processor.annotation.DependencyOnClass;

/**
 * Created by lvhl on 2017/3/2.
 * 路径最好用:  /当前类所属模块/当前类的直接父包名/当前类名
 */

@DependencyOnClass("/RouterMiddleware/dependency/WeiXiaoDependencyImp")
public class WeiXiaoDependencyImp implements IDependency {

    @Override
    public void myToast(Context context) {
        Toast.makeText(context, "show hello from weixiao", Toast.LENGTH_SHORT).show();
    }
}

package com.ziroom.routermiddleware.routers;

import android.content.Context;

import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 * path路由
 * path说明：在类上通过注解 指明activity或fragment；
 */

public class PathActivityRouter extends BaseActivityRouter {
    private static final String TAG = PathActivityRouter.class.getSimpleName();

    public PathActivityRouter(Context applicationContext, Map<String, Class> classMap) {
        super.classMap = classMap;
        super.applicationContext = applicationContext;
    }

    @Override
    public BaseActivityBuilder build(String path) {
        return new BaseActivityBuilder(path);
    }

    @Override
    protected String getRouterType() {
        return "router-path";
    }
}

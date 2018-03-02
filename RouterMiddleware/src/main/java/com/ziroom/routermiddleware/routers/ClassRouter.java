package com.ziroom.routermiddleware.routers;

import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 * 类路由
 * 获取path 或 uri 所对应的类
 */

public class ClassRouter extends BaseRouter {
    private static final String TAG = ClassRouter.class.getSimpleName();

    public ClassRouter(Map<String, Class> classMap) {
        this.classMap = classMap;
    }

    @Override
    public Builder build(String pathOrUri) {
        return new Builder(pathOrUri);
    }

    @Override
    public String getRouterType() {
        return "router-class";
    }
}

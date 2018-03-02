package com.ziroom.routermiddleware.routers;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ziroom.routermiddleware.CommonCallback;
import com.ziroom.routermiddleware.exceptions.CommonException;

import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 * path路由
 * path说明：在类上通过注解 指明activity或fragment；
 */

public class PathFragmentRouter extends BaseRouter {
    private static final String TAG = PathFragmentRouter.class.getSimpleName();

    private Context applicationContext;

    public PathFragmentRouter(Context applicationContext, Map<String, Class> classMap) {
        this.classMap = classMap;
        this.applicationContext = applicationContext;
    }

    @Override
    public PathFragmentBuilder build(String uri) {
        return new PathFragmentBuilder(uri);
    }

    @Override
    protected String getRouterType() {
        return "router-fragment";
    }

    public class PathFragmentBuilder extends BaseRouter.Builder {
        private Bundle mArgBundle;

        public PathFragmentBuilder(String pathOrUri) {
            super(pathOrUri);
        }

        public PathFragmentBuilder setBundle(Bundle bundle) {
            mArgBundle = bundle;
            return this;
        }

        public Object getFragment() {
            return PathFragmentRouter.this.getFragment(this, mCallback);
        }
    }

    public Object getFragment(PathFragmentBuilder builder) {
        return getFragment(builder, null);
    }

    private Object getFragment(PathFragmentBuilder builder, CommonCallback callback) {
        Class clazz = getTheClass(builder, callback);
        Uri uri = Uri.parse(builder.mPathOrUri);
        try {
            if (Fragment.class.isAssignableFrom(clazz)) {
                Object obj = clazz.newInstance();
                Fragment fragment = (Fragment) obj;
                fragment.setArguments(builder.mArgBundle);
                return fragment;
            } else if (android.app.Fragment.class.isAssignableFrom(clazz)) {
                Object obj = clazz.newInstance();
                android.app.Fragment fragment = (android.app.Fragment) obj;
                fragment.setArguments(builder.mArgBundle);
                return fragment;
            } else {
                if (callback != null) {
                    callback.onError(new CommonException("Create Fragment with uri (" + uri + ") fail!!" + "please checkout the fragment's constructor with no parameter."));
                }
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.onError(new CommonException("Create Fragment with uri (" + uri + ") fail!!" + "please checkout the fragment's constructor with no parameter."));
            }
        }
        return null;
    }
}

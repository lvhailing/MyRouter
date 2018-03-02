package com.ziroom.routermiddleware.routers;

import android.net.Uri;

import com.ziroom.routermiddleware.CommonCallback;
import com.ziroom.routermiddleware.exceptions.CommonException;

import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 * 路由基类
 */

public abstract class BaseRouter {
    private static final String TAG = BaseRouter.class.getSimpleName();

    protected Map<String, Class> classMap;

    protected abstract Builder build(String pathOrUri);

    protected abstract String getRouterType();

    public class Builder {
        protected String mPathOrUri;
        protected CommonCallback mCallback;

        public Builder(String pathOrUri) {
            mPathOrUri = pathOrUri;
        }

        public Builder callback(CommonCallback callback) {
            mCallback = callback;
            return this;
        }

        public Class getTheClass() {
            return BaseRouter.this.getTheClass(this, mCallback);
        }
    }

    protected Class getTheClass(Builder builder) {
        return getTheClass(builder, null);
    }

    protected Class getTheClass(Builder builder, CommonCallback callback) {
        if (builder == null) {
            if (callback != null) {
                callback.onError(new CommonException("BaseActivityBuilder is null, when invoke method BaseActivityRouter'getActivity."));
            }
            return null;
        }
        Uri uri = Uri.parse(builder.mPathOrUri);
        if (uri == null) {
            if (callback != null) {
                callback.onError(new CommonException(getRouterType() + " is null, when invoke method BaseActivityRouter'getActivity"));
            }
            return null;
        }
        String uriStr = builder.mPathOrUri;
        //以path形式获取到的uriStr不能截取字符串
//        if (builder.mPathOrUri.contains("?")) {
//            // 通过uri路径来指明activity 不包括参数
//            uriStr = uriStr.substring(0, uriStr.indexOf("?"));
//        }
        Class activityClazz = classMap.get(uriStr);
        if (activityClazz == null) {
            if (callback != null) {
                callback.onError(new CommonException("can't find Activity by " + getRouterType() + " : " + uriStr + ", when invoke method BaseActivityRouter'getActivity"));
            }
            return null;
        }

        //成功后回调
        if (callback != null) {
            callback.onSuccess();
        }
        return activityClazz;
    }
}

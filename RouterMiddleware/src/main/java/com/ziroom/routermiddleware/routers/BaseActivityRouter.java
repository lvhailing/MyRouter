package com.ziroom.routermiddleware.routers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.ziroom.routermiddleware.CommonCallback;

import java.util.Set;

/**
 * Created by lvhl on 2016/12/21.
 * 路由基类 path或uri路由
 * uri说明：在清单文件里隐式声明activity
 * path说明：在类上通过注解 指明activity或fragment；
 */

public abstract class BaseActivityRouter extends BaseRouter {
    private static final String TAG = BaseActivityRouter.class.getSimpleName();

    protected Context applicationContext;

    protected abstract String getRouterType();

    protected abstract BaseRouter.Builder build(String pathOrUri);

    public class BaseActivityBuilder extends BaseRouter.Builder {
        private Bundle mArgBundle;
        private int mFlags;
        private int mRequestCode;
        private Object mSourceActivityOrFragment;

        public BaseActivityBuilder(String pathOrUri) {
            super(pathOrUri);
        }

        public Builder callback(CommonCallback callback) {
            super.callback(callback);
            return this;
        }

        public BaseActivityBuilder setBundle(Bundle bundle) {
            mArgBundle = bundle;
            return this;
        }

        public BaseActivityBuilder addFlags(int flags) {
            mFlags = flags;
            return this;
        }

        /**
         * @param requestCode              请求码
         * @param sourceActivityOrFragment 可以是activity 普通情况下适用，activity的onActivityResult执行
         *                                 也可以是fragment 因为只有用fragment.startActivityForResult时 fragment中的onActivityResult才会执行
         *                                 否则只有activity的onActivityResult执行
         * @return
         */
        public BaseActivityBuilder setRequestCode(int requestCode, Object sourceActivityOrFragment) {
            mRequestCode = requestCode;
            mSourceActivityOrFragment = sourceActivityOrFragment;
            return this;
        }

        public void openActivity() {
            BaseActivityRouter.this.openActivity(this, mCallback);
        }
    }

    protected void openActivity(BaseActivityBuilder builder) {
        openActivity(builder, null);
    }

    protected void openActivity(BaseActivityBuilder builder, CommonCallback callback) {
        Class activityClazz = getTheClass(builder, callback);
        Uri uri = Uri.parse(builder.mPathOrUri);

        Intent intent = new Intent(applicationContext, activityClazz);

        //设置intent的flag 注意：NewTask 对startActivityForResult()有影响
        if (builder.mRequestCode > 0 && builder.mSourceActivityOrFragment != null && builder.mFlags == Intent.FLAG_ACTIVITY_NEW_TASK) {
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            intent.setFlags(builder.mFlags);
        }

        //设置intent的bundle
        Bundle bundle = builder.mArgBundle == null ? new Bundle() : builder.mArgBundle;
        Set<String> queryParameterNames = uri.getQueryParameterNames();
        if (queryParameterNames != null && queryParameterNames.size() > 0) {
            for (String key : queryParameterNames) {
                bundle.putString(key, uri.getQueryParameter(key));
            }
        }
        intent.putExtras(bundle);

        //启动activity 启动activity的源 可能是fragment
        if (builder.mRequestCode > 0 && builder.mSourceActivityOrFragment != null) {
            if (builder.mSourceActivityOrFragment instanceof Activity) {
                ((Activity) builder.mSourceActivityOrFragment).startActivityForResult(intent, builder.mRequestCode);
            } else if (builder.mSourceActivityOrFragment instanceof Fragment) {
                ((Fragment) builder.mSourceActivityOrFragment).startActivityForResult(intent, builder.mRequestCode);
            } else if (builder.mSourceActivityOrFragment instanceof android.app.Fragment) {
                ((android.app.Fragment) builder.mSourceActivityOrFragment).startActivityForResult(intent, builder.mRequestCode);
            }
        } else {
            applicationContext.startActivity(intent);
        }

        //成功后回调
        if (callback != null) {
            callback.onSuccess();
        }
    }
}

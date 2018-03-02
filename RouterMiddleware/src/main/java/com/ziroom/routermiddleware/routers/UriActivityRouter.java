package com.ziroom.routermiddleware.routers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import com.ziroom.processor.annotation.UriOnMethod;
import com.ziroom.processor.annotation.UriOnParameter;
import com.ziroom.routermiddleware.CommonCallback;
import com.ziroom.routermiddleware.exceptions.CommonException;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 * uri路由
 * uri说明：在清单文件里隐式声明activity
 */

public class UriActivityRouter extends BaseActivityRouter {
    private static final String TAG = UriActivityRouter.class.getSimpleName();

    private Map<String, Object> proxyInstanceMap = new HashMap<>();
    private Map<String, InvocationHandler> invocationHandlerMap = new HashMap<>();

    @Override
    public BaseActivityBuilder build(String uri) {
        return new BaseActivityBuilder(uri);
    }

    @Override
    protected String getRouterType() {
        return "router-uri";
    }


    public UriActivityRouter(Context applicationContext, Map<String, Class> classMap) {
        this.classMap = classMap;
        this.applicationContext = applicationContext;
    }

    public <T> T create(@NonNull Class<T> clazz) {
        return create(clazz, 0, null);
    }

    public <T> T create(@NonNull Class<T> clazz, int requestCode, Object sourceActivityOrFragment) {
        return create(clazz, requestCode, sourceActivityOrFragment, null);
    }

    public <T> T create(@NonNull Class<T> clazz, int requestCode, Object sourceActivityOrFragment, CommonCallback callback) {
        //检查是否缓存了这个clazz对应的class代理
        String clazzKey = requestCode > 0 ? (clazz.getCanonicalName() + requestCode) : clazz.getCanonicalName();
        if (proxyInstanceMap.get(clazzKey) != null) {
            return (T) proxyInstanceMap.get(clazzKey);
        }

        InvocationHandler handler;
        handler = getInvocationHandler(clazz, requestCode, sourceActivityOrFragment, callback);
        if (handler == null) {
            if (callback != null) {
                callback.onError(new CommonException("please check whether you use the annotation UriOnClass!"));
            }
            return null;
        }
        Object routerStub = Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, handler);
        //缓存这个clazz代理  （key是clazz的CanonicalName 即 包名.类名）
        proxyInstanceMap.put(clazzKey, routerStub);
        return (T) routerStub;
    }

    private InvocationHandler getInvocationHandler(Class<?> clazz, final int requestCode, final Object sourceActivityOrFragment, final CommonCallback callback) {
        //检查是否缓存了这个clazz对应的InvocationHandler
        String clazzKey = requestCode > 0 ? (clazz.getCanonicalName() + requestCode) : clazz.getCanonicalName();
        if (invocationHandlerMap.get(clazzKey) != null) {
            return invocationHandlerMap.get(clazzKey);
        }

        Method[] methods = clazz.getDeclaredMethods();

        //确认代理的类的所以方法上，有无注解UriOnMethod
        int uriMethodNum = 0;
        for (Method method : methods) {
            method.setAccessible(true);
            UriOnMethod annotation = method.getAnnotation(UriOnMethod.class);
            if (annotation != null) {
                uriMethodNum++;
            }
        }
        if (uriMethodNum <= 0) {
            if (callback != null) {
                callback.onError(new CommonException(" can't find method with annotation UriOnMethod!"));
            }
            return null;
        }

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getAnnotation(UriOnMethod.class) != null) {
                    //开始组装uri，用来隐式打开activity
                    StringBuilder sb = new StringBuilder();
                    UriOnMethod uriAnnotation = method.getAnnotation(UriOnMethod.class);
                    sb.append(uriAnnotation.value());

                    //参数的二维数组
                    Annotation[][] paramAnnotation2Array = method.getParameterAnnotations();
                    if (paramAnnotation2Array != null) {
                        int pos = 0;
                        for (int i = 0; i < paramAnnotation2Array.length; i++) {
                            //参数的一维数组
                            Annotation[] paramAnnotation1Array = paramAnnotation2Array[i];
                            if (paramAnnotation1Array != null) {
                                for (Annotation paramAnnotation : paramAnnotation1Array) {
                                    if (paramAnnotation instanceof UriOnParameter) {
                                        sb.append(pos == 0 ? "?" : "&");
                                        pos++;
                                        UriOnParameter parameter = (UriOnParameter) paramAnnotation;
                                        String paramKey = parameter.value();
                                        sb.append(paramKey);
                                        sb.append("=");
                                        sb.append(args[i]);
                                    }
                                }
                            }
                        }
                    }
                    //打开组装好的uri
                    openRouterUri(sb.toString(), requestCode, sourceActivityOrFragment, callback);
                }
                return null;
            }
        };
        //缓存这个InvocationHandler （key是clazz的CanonicalName 即 包名.类名）
        invocationHandlerMap.put(clazzKey, handler);
        return handler;
    }


    public void openRouterUri(String routerUri) {
        openRouterUri(routerUri, 0, null, null);
    }

    public void openRouterUri(String routerUri, CommonCallback callback) {
        openRouterUri(routerUri, 0, null, callback);
    }

    public void openRouterUri(String routerUri, int requestCode, Object sourceActivityOrFragment, CommonCallback callback) {
        Uri uri = Uri.parse(routerUri);
        PackageManager packageManager = applicationContext.getPackageManager();
        Intent uriIntent = new Intent(Intent.ACTION_VIEW, uri);

        //NEW_TASK 会对startActivityForResult产生影响
        if (requestCode > 0 && sourceActivityOrFragment != null) {
            uriIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        } else {
            uriIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        List<ResolveInfo> activities = packageManager.queryIntentActivities(uriIntent, 0);
        if (activities.size() > 0) {
            //启动activity 启动activity的源 可能是fragment
            if (requestCode > 0 && sourceActivityOrFragment != null) {
                if (sourceActivityOrFragment instanceof Activity) {
                    ((Activity) sourceActivityOrFragment).startActivityForResult(uriIntent, requestCode);
                } else if (sourceActivityOrFragment instanceof Fragment) {
                    ((Fragment) sourceActivityOrFragment).startActivityForResult(uriIntent, requestCode);
                } else if (sourceActivityOrFragment instanceof android.app.Fragment) {
                    ((android.app.Fragment) sourceActivityOrFragment).startActivityForResult(uriIntent, requestCode);
                }
            } else {
                applicationContext.startActivity(uriIntent);
            }
        } else {
            //尝试通过path的方式打开uri （path的方式：在类上通过注解 指明activity；uri的方式：在清单文件里隐式声明activity）
            BaseActivityBuilder builder = build(routerUri).setRequestCode(requestCode, sourceActivityOrFragment);
            openActivity(builder, callback);
        }
    }

}

package com.ziroom.routermiddleware.routers;

import android.content.Context;
import android.text.TextUtils;

import com.ziroom.processor.annotation.DependInsertOnParam;
import com.ziroom.routermiddleware.CommonCallback;
import com.ziroom.routermiddleware.exceptions.CommonException;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by lvhl on 2017/3/2.
 */

public class DependInjectRouter {

    private Context applicationContext;
    private Map<String, Class> classMap;

    public DependInjectRouter(Context applicationContext, Map<String, Class> classMap) {
        this.classMap = classMap;
        this.applicationContext = applicationContext;
    }

    public void inject(Object obj) {
        inject(obj, null);
    }

    public void inject(Object obj, CommonCallback callback) {
        if (obj == null) {
            if (callback != null) {
                callback.onError(new CommonException("the object you injected is null, when invoke method DependInjectRouter'inject."));
            }
            return;
        }
        Field[] fieldArray = obj.getClass().getDeclaredFields();
        for (int i = 0; i < fieldArray.length; i++) {
            fieldArray[i].setAccessible(true);
            if (fieldArray[i].isAnnotationPresent(DependInsertOnParam.class)) {
                DependInsertOnParam annotationParam = fieldArray[i].getAnnotation(DependInsertOnParam.class);
                String value = annotationParam.value();
                if (TextUtils.isEmpty(value)) {
                    value = fieldArray[i].getName();
                }
                Class dependencyInfo = classMap.get(value);
                if (dependencyInfo == null) {
                    if (callback != null) {
                        callback.onError(new CommonException("can't find class, did you injected it by key: " + value));
                    }
                    return;
                }
                if (!fieldArray[i].getType().isAssignableFrom(dependencyInfo)) {//当前class是否是括号中class的父类
                    //字段类型和找到的注解类型不匹配，抛出异常
                    if (callback != null) {
                        callback.onError(new CommonException("the field type can't match with annotation type which found by key: " + value));
                    }
                    return;
                }
                try {
                    fieldArray[i].set(obj, dependencyInfo.getConstructor().newInstance());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

package com.ziroom.processor.helpers;

import com.ziroom.processor.exception.AnnotationTargetUseException;
import com.ziroom.processor.exception.AnnotationValueNullException;
import com.ziroom.processor.model.ElementModel;
import com.ziroom.processor.utils.Logger;
import com.ziroom.processor.utils.TextUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

/**
 * Created by lvhl3 on 2017/12/12.
 * 类简单说明:
 */

public class ProcessHelper {

    public static Map<String, ElementModel> collectClassInfo(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClazz, ElementKind kind, Logger logger) {
        Map<String, ElementModel> map = new HashMap<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClazz);
        for (Element element : elements) {
            if (element.getKind() != kind) {
                throw new AnnotationTargetUseException(element.getSimpleName() + "'s annotation must be on a " + kind.name());
            }
            try {
                TypeElement typeElement = (TypeElement) element;
                Annotation annotation = typeElement.getAnnotation(annotationClazz);

                Method method = annotationClazz.getDeclaredMethod("value");
                method.setAccessible(true);
                Object value = method.invoke(annotation);

                String clazzName = typeElement.getQualifiedName().toString();
                String simpleName = typeElement.getSimpleName().toString();
                if (!(value instanceof String) || TextUtils.isEmpty((String) value)) {
                    throw new AnnotationValueNullException(element.getSimpleName() + "'s " + annotationClazz.getSimpleName() + " annotation's value is null!!");
                }
                map.put((String) value, new ElementModel(typeElement, (String) value, clazzName, simpleName));

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        return map;
    }

    public static List<ElementModel> collectClassInfo(RoundEnvironment roundEnv, Class<? extends Annotation> annotationClazz, ElementKind kind) {
        List<ElementModel> list = new ArrayList<>();
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotationClazz);
        for (Element element : elements) {
            if (element.getKind() != kind) {
                throw new AnnotationTargetUseException(element.getSimpleName() + "'s annotation must be on a " + kind.name());
            }
            try {
                TypeElement typeElement = (TypeElement) element;
                Annotation annotation = typeElement.getAnnotation(annotationClazz);

                Method method = annotationClazz.getDeclaredMethod("value");
                method.setAccessible(true);
                Object value = method.invoke(annotation);

                String clazzName = typeElement.getQualifiedName().toString();
                String simpleName = typeElement.getSimpleName().toString();
                if (!(value instanceof String) || TextUtils.isEmpty((String) value)) {
                    throw new AnnotationValueNullException(element.getSimpleName() + "'s " + annotationClazz.getSimpleName() + " annotation's value is null!!");
                }
                list.add(new ElementModel(typeElement, (String) value, clazzName, simpleName));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return list;
    }
}

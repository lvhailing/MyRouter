package com.ziroom.processor.helpers;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.ziroom.processor.common.Constant;
import com.ziroom.processor.model.ElementModel;
import com.ziroom.processor.utils.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by lvhl on 2016/12/7.
 */

public class ClassCreateHelper {

    private Logger mLogger;

    public ClassCreateHelper(Logger logger) {
        mLogger = logger;
    }

    public void createClass(Elements elementUtils, Filer filer, List<ElementModel> elementModels, String moduleName) {

        TypeElement interfaceElm = elementUtils.getTypeElement(Constant.INTERFACE_EXCHANGE);

        //生成参数类型，Map<String, Class>
        ParameterizedTypeName parameterTypeName = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(Class.class)
        );

        //生成参数，(Map<String, Class> data)
        ParameterSpec parameterSpec = ParameterSpec.builder(parameterTypeName, "data").build();

        //生成方法
        // @Override
        // public void exchangeData(Map<String, Class> data) {
        // }
        MethodSpec.Builder methodSpec = MethodSpec.methodBuilder("exchangeData")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(parameterSpec);

        //生成方法体，如：routerMap.put("/app/myrouter/SecondActivity", SecondActivity.class);
        for (ElementModel holder : elementModels) {
            methodSpec.addStatement("data.put($S, $T.class)", holder.getValueName(), ClassName.get(holder.getTypeElement()));
        }

        //生成类名，并指定实现的接口，并加上方法
        //如： public class RouterLinkUtils$$app implements IActivityRouterInitalizer {
        //           参考上面的方法...
        //    }
        TypeSpec.Builder typeSpec = TypeSpec.classBuilder(Constant.PATH_ROUTER_$$ + moduleName)
                .addSuperinterface(ClassName.get(interfaceElm))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodSpec.build());

        //将类关联到包的路径下
        JavaFile javaFile = JavaFile.builder(Constant.ROUTER_PACKAGE_NAME, typeSpec.build()).build();

        try {
            //写入到filer
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

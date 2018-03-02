package com.ziroom.processor.model;

import javax.lang.model.element.TypeElement;

/**
 * Created by lvhl on 2016/12/7.
 */

public class ElementModel {

    protected TypeElement typeElement;
    protected String valueName;//注解中的值
    protected String clazzName;//被注解类的全名
    protected String simpleName;//被注解类的 简称

    public ElementModel(TypeElement typeElement, String valueName, String clazzName, String simpleName) {
        this.typeElement = typeElement;
        this.valueName = valueName;
        this.clazzName = clazzName;
        this.simpleName = simpleName;
    }

    public TypeElement getTypeElement() {
        return typeElement;
    }

    public void setTypeElement(TypeElement typeElement) {
        this.typeElement = typeElement;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getSimpleName() {
        return simpleName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}

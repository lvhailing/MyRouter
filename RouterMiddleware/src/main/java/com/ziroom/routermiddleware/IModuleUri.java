package com.ziroom.routermiddleware;

import com.ziroom.processor.annotation.UriOnMethod;
import com.ziroom.processor.annotation.UriOnParameter;

/**
 * Created by lvhl on 2016/12/21.
 */

public interface IModuleUri {

    /**
     * 去往考试module的KSActivity
     * @param key   携带的入参 本例是uid
     */
    @UriOnMethod("router://ziroom:8080/ModuleKaoShi_KSActivity") //方法上的uri注解
    public void startKSActivity(@UriOnParameter("uid") String key);//参数上的注解

}

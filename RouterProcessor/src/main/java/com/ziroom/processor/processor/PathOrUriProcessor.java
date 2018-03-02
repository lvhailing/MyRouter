package com.ziroom.processor.processor;

import com.google.auto.service.AutoService;
import com.ziroom.processor.annotation.PathOnClass;
import com.ziroom.processor.annotation.UriOnClass;
import com.ziroom.processor.common.Constant;
import com.ziroom.processor.helpers.ClassCreateHelper;
import com.ziroom.processor.helpers.ProcessHelper;
import com.ziroom.processor.model.ElementModel;
import com.ziroom.processor.utils.Logger;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

/**
 * Created by lvhl3 on 2017/12/12.
 * 类简单说明:
 */

@AutoService(Processor.class)
public class PathOrUriProcessor extends AbstractProcessor {

    private Filer mFiler;
    private Elements mElementUtils;
    private Logger mLogger;
    private String moduleName;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        Messager msg = processingEnv.getMessager();
        mLogger = new Logger(msg);

        Map<String, String> options = processingEnv.getOptions();
        if (options != null && options.size() > 0) {
            moduleName = options.get(Constant.MODULE_NAME);
        }
        mLogger.info("PathOrUriProcessor : " + moduleName + " init ------ ");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(PathOnClass.class.getCanonicalName());
        types.add(UriOnClass.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        ClassCreateHelper createHelper = new ClassCreateHelper(mLogger);
        List<ElementModel> pathList = ProcessHelper.collectClassInfo(roundEnvironment, PathOnClass.class, ElementKind.CLASS);
        pathList.addAll(ProcessHelper.collectClassInfo(roundEnvironment, UriOnClass.class, ElementKind.CLASS));
        if (pathList.size() > 0) {
            createHelper.createClass(mElementUtils, mFiler, pathList, moduleName);
        }
        return true;
    }
}

package com.ziroom.routermiddleware;

import android.content.Context;

import com.ziroom.processor.common.Constant;
import com.ziroom.routermiddleware.routers.ClassRouter;
import com.ziroom.routermiddleware.routers.DependInjectRouter;
import com.ziroom.routermiddleware.routers.PathActivityRouter;
import com.ziroom.routermiddleware.routers.PathFragmentRouter;
import com.ziroom.routermiddleware.routers.UriActivityRouter;
import com.ziroom.routermiddleware.utils.FileUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lvhl on 2016/12/21.
 */

public class RouterMiddleware {
    private static final String TAG = RouterMiddleware.class.getSimpleName();

    private Map<String, Class> classMap = new HashMap<>();
    private volatile static RouterMiddleware myRouter;
    private static Context mApplicationContext;
    private PathActivityRouter pathActivityRouter;
    private PathFragmentRouter pathFragmentRouter;
    private UriActivityRouter uriActivityRouter;
    private ClassRouter classRouter;
    private DependInjectRouter dependInjectRouter;

    private RouterMiddleware() {
    }

    public static RouterMiddleware getInstance() {
        if (myRouter == null) {
            synchronized (RouterMiddleware.class) {
                if (myRouter == null) {
                    myRouter = new RouterMiddleware();
                }
            }
        }
        return myRouter;
    }

    public ClassRouter getClassRouter() {
        if (classRouter == null) {
            classRouter = new ClassRouter(classMap);
        }
        return classRouter;
    }

    public PathActivityRouter getPathActivityRouter() {
        if (pathActivityRouter == null) {
            pathActivityRouter = new PathActivityRouter(mApplicationContext, classMap);
        }
        return pathActivityRouter;
    }

    public PathFragmentRouter getPathFragmentRouter() {
        if (pathFragmentRouter == null) {
            pathFragmentRouter = new PathFragmentRouter(mApplicationContext, classMap);
        }
        return pathFragmentRouter;
    }

    public UriActivityRouter getUriActivityRouter() {
        if (uriActivityRouter == null) {
            uriActivityRouter = new UriActivityRouter(mApplicationContext, classMap);
        }
        return uriActivityRouter;
    }

    public DependInjectRouter getDependInjectRouter() {
        if (dependInjectRouter == null) {
            dependInjectRouter = new DependInjectRouter(mApplicationContext, classMap);
        }
        return dependInjectRouter;
    }

    public void init(Context context) {
        mApplicationContext = context;
        if (classMap.isEmpty()) {
            try {
                //将com.ziroom.processor.builds包下的所有类的所有方法执行下  将编译时生成的数据传入到map中
                List<String> classNames = FileUtil.getClassNameByPackageName(mApplicationContext, Constant.ROUTER_PACKAGE_NAME);
                for (String className : classNames) {
                    ((IExchangeData) (Class.forName(className).getConstructor().newInstance())).exchangeData(classMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

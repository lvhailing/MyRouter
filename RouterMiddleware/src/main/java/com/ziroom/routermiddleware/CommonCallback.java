package com.ziroom.routermiddleware;

/**
 * Created by lvhl on 2017/1/16.
 */

public interface CommonCallback {

    void onSuccess();

    void onError(Throwable error);

}

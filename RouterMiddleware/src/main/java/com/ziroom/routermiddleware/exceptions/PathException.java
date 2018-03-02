package com.ziroom.routermiddleware.exceptions;

/**
 * Created by lvhl on 2017/1/25.
 */

public class PathException extends RuntimeException {
    public PathException() {
        super("Maybe the path uri is wrong or not use annotation PathOnClass, please check!");
    }

    public PathException(String message) {
        super(message);
    }
}

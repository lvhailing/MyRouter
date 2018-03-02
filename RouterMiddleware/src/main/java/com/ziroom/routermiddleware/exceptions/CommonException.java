package com.ziroom.routermiddleware.exceptions;

/**
 * Created by lvhl on 2017/1/25.
 */

public class CommonException extends RuntimeException {
    public CommonException() {
        super("my router CommonException ");
    }

    public CommonException(String message) {
        super(message);
    }
}

package com.ziroom.processor.exception;

/**
 * Created by lvhl on 2017/1/11.
 */

public class AnnotationTargetUseException extends RuntimeException {

    public AnnotationTargetUseException(String message){
        super("Annotation use error : " + message);
    }
}

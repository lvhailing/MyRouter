package com.ziroom.processor.exception;

/**
 * Created by lvhl on 2017/1/11.
 */

public class AnnotationValueNullException extends RuntimeException {

    public AnnotationValueNullException(){
        super("Annotation's value is null");
    }

    public AnnotationValueNullException(String message){
        super("Annotation's value is null : " + message);
    }
}

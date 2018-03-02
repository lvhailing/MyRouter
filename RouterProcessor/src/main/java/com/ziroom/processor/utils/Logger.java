package com.ziroom.processor.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

import static java.awt.SystemColor.info;

/**
 * Created by lvhl on 2017/1/11.
 */

public class Logger {

    private Messager mMsg;

    public Logger(Messager messager) {
        mMsg = messager;
    }

    public void info(CharSequence info) {
        if (info != null && !info.equals("")) {
            mMsg.printMessage(Diagnostic.Kind.NOTE, "Logger: " + info);
        }
    }

    public void error(CharSequence error) {
        if (error != null && !error.equals("")) {
            mMsg.printMessage(Diagnostic.Kind.ERROR, "Logger: " + "[" + error + "]");
        }
    }

    public void error(Throwable error) {
        if (error != null) {
            mMsg.printMessage(Diagnostic.Kind.ERROR, "Logger: " + "An exception is encountered, [" + error.getMessage() + "]" + "\n" + formatStackTrace(error.getStackTrace()));
        }
    }


    public void warning(CharSequence warning) {
        if (warning != null && !warning.equals("")) {
            mMsg.printMessage(Diagnostic.Kind.WARNING, "Logger: " + warning);
        }
    }

    /**
     * 获取错误方法栈的信息
     *
     * @param stackTrace
     * @return
     */
    private String formatStackTrace(StackTraceElement[] stackTrace) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : stackTrace) {
            sb.append("   at ").append(element.toString()).append("\n");
        }
        return sb.toString();
    }


}

package com.morelap.tagskill;

import java.lang.Thread.UncaughtExceptionHandler;

public class AppException extends Exception implements UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {

    }

}

package com.belatrix.practicaltest.exception;

public class LogException extends Exception {

    String exceptionMessage;

    public LogException(String message){
        exceptionMessage = message;
    }

    public String toString() {
        return "LogException[" + exceptionMessage + "]";
    }

}

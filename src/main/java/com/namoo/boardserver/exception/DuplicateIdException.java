package com.namoo.boardserver.exception;

// Runtime 시에 예외를 던짐
public class DuplicateIdException extends RuntimeException {
    public DuplicateIdException(String msg) {
        super(msg);
    }
}

package com.namoo.boardserver.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME) // 런타임까지 어노테이션 정보 유지
@Target(ElementType.METHOD) // 메서드에만 붙일 수 있음
public @interface LoginCheck {
    // 사용자인지 관리자인지 구분
    public static enum UserType{
        USER, ADMIN
    }

    UserType type();
}

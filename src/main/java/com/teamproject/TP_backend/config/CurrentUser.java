package com.teamproject.TP_backend.config;

import java.lang.annotation.*;

@Target(ElementType.PARAMETER)  // 메서드 파라미터에 붙임
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
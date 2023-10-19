package ru.council.telegram.support.annotations;

import lombok.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnTextMessage {
    long id() default -1;
    @NonNull String pattern() default "";
    boolean isPattern() default false;
}

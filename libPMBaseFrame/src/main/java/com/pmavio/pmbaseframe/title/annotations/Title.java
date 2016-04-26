package com.pmavio.pmbaseframe.title.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者：Mavio
 * 日期：2016/3/3.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Title {
    /**
     * 标题名称
     * @return
     */
    String value() default "";

    /**
     * 内容布局资源文件id
     * @return
     */
    int layoutRes() default 0;
}

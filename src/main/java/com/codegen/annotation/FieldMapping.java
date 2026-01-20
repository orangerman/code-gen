package com.codegen.annotation;

import java.lang.annotation.*;

/**
 * 字段映射配置
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMapping {

    /**
     * 数据库字段名
     */
    String column() default "";

    /**
     * 字段说明
     */
    String description() default "";

    /**
     * 是否为主键
     */
    boolean primaryKey() default false;

    /**
     * 是否在 DTO 中忽略该字段
     */
    boolean ignoreInDto() default false;

    /**
     * 是否在 VO 中忽略该字段
     */
    boolean ignoreInVo() default false;
}

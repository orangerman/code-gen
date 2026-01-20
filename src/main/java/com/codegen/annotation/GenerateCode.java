package com.codegen.annotation;

import java.lang.annotation.*;

/**
 * 标记需要生成代码的实体类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GenerateCode {

    /**
     * 生成的文件类型
     */
    CodeType[] value() default {CodeType.ALL};

    /**
     * 基础包名，如果不指定则使用实体类所在包名
     */
    String basePackage() default "";

    /**
     * 作者信息
     */
    String author() default "Code Generator";

    /**
     * 模块名称
     */
    String moduleName() default "";

    /**
     * 表名（用于 Mapper XML 等）
     */
    String tableName() default "";


    /**
     * 账单类型
     */
    String billType() default "";
}
package com.codegen.model;

import lombok.Data;

import java.util.List;

/**
 * 代码生成上下文数据模型
 */
@Data
public class GenerateContext {

    /**
     * 实体类名
     */
    private String className;

    /**
     * 实体类简单名称（不含包名）
     */
    private String simpleClassName;

    /**
     * 包名
     */
    private String packageName;

    /**
     * 基础包名
     */
    private String basePackage;

    /**
     * 作者
     */
    private String author;

    /**
     * 模块名称
     */
    private String moduleName;

    /**
     * 表名
     */
    private String tableName;

    /**
     * 字段列表
     */
    private List<FieldInfo> fields;

    /**
     * 主键字段
     */
    private FieldInfo primaryKey;

    /**
     * 当前日期时间
     */
    private String dateTime;

    /**
     * 账单类型
     */
    private String billType;
}

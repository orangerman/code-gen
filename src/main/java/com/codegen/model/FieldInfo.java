package com.codegen.model;

import lombok.Data;

/**
 * 字段信息
 */
@Data
public class FieldInfo {

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 字段类型（简单名称）
     */
    private String fieldType;

    /**
     * 字段类型（完整名称）
     */
    private String fullFieldType;

    /**
     * 数据库列名
     */
    private String columnName;

    /**
     * 字段描述
     */
    private String description;

    /**
     * 是否为主键
     */
    private boolean primaryKey;

    /**
     * 是否在 DTO 中忽略
     */
    private boolean ignoreInDto;

    /**
     * 是否在 VO 中忽略
     */
    private boolean ignoreInVo;

    /**
     * 首字母大写的字段名（用于 getter/setter）
     */
    private String capitalizedFieldName;
}

package com.codegen.core;

import com.codegen.annotation.CodeType;
import com.codegen.annotation.FieldMapping;
import com.codegen.annotation.GenerateCode;
import com.codegen.model.FieldInfo;
import com.codegen.model.GenerateContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * 代码生成器核心引擎
 */
@Slf4j
@Component
public class CodeGenerator {

    @Autowired
    private TemplateEngine templateEngine;

    /**
     * 为指定类生成代码
     */
    public void generateForClass(Class<?> clazz) {
        GenerateCode annotation = clazz.getAnnotation(GenerateCode.class);
        if (annotation == null) {
            log.warn("Class {} does not have @GenerateCode annotation", clazz.getName());
            return;
        }

        log.info("Generating code for class: {}", clazz.getName());

        GenerateContext context = buildContext(clazz, annotation);
        List<CodeType> codeTypes = getCodeTypes(annotation);

        for (CodeType codeType : codeTypes) {
            try {
                generateCode(context, codeType);
            } catch (Exception e) {
                log.error("Error generating {} for class {}", codeType, clazz.getName(), e);
            }
        }
    }

    /**
     * 为包路径下所有带注解的类生成代码
     */
    public void generateForPackage(String packageName) {
        Set<Class<?>> classes = ClassScanner.scanAnnotatedClasses(packageName, GenerateCode.class);
        log.info("Found {} classes with @GenerateCode annotation in package: {}", classes.size(), packageName);

        for (Class<?> clazz : classes) {
            generateForClass(clazz);
        }
    }

    /**
     * 构建生成上下文
     */
    private GenerateContext buildContext(Class<?> clazz, GenerateCode annotation) {
        GenerateContext context = new GenerateContext();

        context.setClassName(clazz.getName());
        context.setSimpleClassName(clazz.getSimpleName());
        context.setPackageName(clazz.getPackage().getName());
        context.setBasePackage(StringUtils.isNotBlank(annotation.basePackage())
                ? annotation.basePackage()
                : clazz.getPackage().getName());
        context.setAuthor(annotation.author());
        context.setModuleName(annotation.moduleName());
        context.setTableName(StringUtils.isNotBlank(annotation.tableName())
                ? annotation.tableName()
                : toUnderscoreCase(clazz.getSimpleName()));
        context.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        context.setBillType(annotation.billType());

        List<FieldInfo> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            FieldInfo fieldInfo = buildFieldInfo(field);
            fields.add(fieldInfo);

            if (fieldInfo.isPrimaryKey()) {
                context.setPrimaryKey(fieldInfo);
            }
        }
        context.setFields(fields);

        if (context.getPrimaryKey() == null && !fields.isEmpty()) {
            FieldInfo firstField = fields.get(0);
            firstField.setPrimaryKey(true);
            context.setPrimaryKey(firstField);
        }

        return context;
    }

    /**
     * 构建字段信息
     */
    private FieldInfo buildFieldInfo(Field field) {
        FieldInfo info = new FieldInfo();
        info.setFieldName(field.getName());
        info.setFieldType(field.getType().getSimpleName());
        info.setFullFieldType(field.getType().getName());
        info.setCapitalizedFieldName(StringUtils.capitalize(field.getName()));

        FieldMapping mapping = field.getAnnotation(FieldMapping.class);
        if (mapping != null) {
            info.setColumnName(StringUtils.isNotBlank(mapping.column())
                    ? mapping.column()
                    : toUnderscoreCase(field.getName()));
            info.setDescription(mapping.description());
            info.setPrimaryKey(mapping.primaryKey());
            info.setIgnoreInDto(mapping.ignoreInDto());
            info.setIgnoreInVo(mapping.ignoreInVo());
        } else {
            info.setColumnName(toUnderscoreCase(field.getName()));
            info.setDescription(field.getName());
        }

        return info;
    }

    /**
     * 获取要生成的代码类型列表
     */
    private List<CodeType> getCodeTypes(GenerateCode annotation) {
        CodeType[] types = annotation.value();
        if (types.length == 1 && types[0] == CodeType.ALL) {
            return Arrays.asList(
                    CodeType.MAPPER,
                    CodeType.SERVICE,
                    CodeType.JOB
                    );
        }
        return Arrays.asList(types);
    }

    /**
     * 生成指定类型的代码
     */
    private void generateCode(GenerateContext context, CodeType codeType) throws IOException {
        String content = templateEngine.render(codeType, context);

        String filePath = buildFilePath(context, codeType);

        File file = new File(filePath);
        file.getParentFile().mkdirs();

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
            log.info("Generated {} successfully: {}", codeType, filePath);
        }
    }

    /**
     * 构建文件路径
     */
    private String buildFilePath(GenerateContext context, CodeType codeType) {
        String baseDir = "generated-sources/";
        String packagePath = context.getBasePackage().replace(".", "/");
        return switch (codeType) {
            case DTO -> baseDir + packagePath + "/dto/" + context.getSimpleClassName() + "DTO.java";
            case VO -> baseDir + packagePath + "/vo/" + context.getSimpleClassName() + "VO.java";
            case MAPPER -> baseDir + packagePath + "/mapper/" + context.getSimpleClassName() + "Mapper.java";
            case SERVICE -> baseDir + packagePath + "/service/" + context.getSimpleClassName() + "Service.java";
            case SERVICE_IMPL ->
                    baseDir + packagePath + "/service/impl/" + context.getSimpleClassName() + "ServiceImpl.java";
            case CONTROLLER ->
                    baseDir + packagePath + "/controller/" + context.getSimpleClassName() + "Controller.java";
            case JOB -> baseDir + packagePath + "/job/" + context.getBillType() + "SysJob.java";
            default -> throw new IllegalArgumentException("Unsupported code type: " + codeType);
        };
    }

    /**
     * 驼峰转下划线
     */
    private String toUnderscoreCase(String camelCase) {
        return camelCase.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }
}
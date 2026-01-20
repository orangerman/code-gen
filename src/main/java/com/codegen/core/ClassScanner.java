package com.codegen.core;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.lang.annotation.Annotation;
import java.util.Set;

/**
 * 类扫描器
 */
public class ClassScanner {

    /**
     * 扫描指定包下所有带指定注解的类
     */
    public static Set<Class<?>> scanAnnotatedClasses(String packageName, Class<? extends Annotation> annotation) {
        Reflections reflections = new Reflections(packageName, Scanners.TypesAnnotated);
        return reflections.getTypesAnnotatedWith(annotation);
    }
}

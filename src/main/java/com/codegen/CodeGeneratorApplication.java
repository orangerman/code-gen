package com.codegen;

import com.codegen.core.CodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 代码生成器应用启动类
 */
@Slf4j
@SpringBootApplication
public class CodeGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(CodeGeneratorApplication.class, args);
    }

    @Bean
    public CommandLineRunner executeCodeGeneration(CodeGenerator codeGenerator) {
        return args -> {
            try {
                log.info("开始执行代码生成...");
                Class<?> clazz = Class.forName("com.codegen.example.MeiTuanWmBill");
                codeGenerator.generateForClass(clazz);
                log.info("代码生成完成!");
            } catch (ClassNotFoundException e) {
                log.error("未找到指定的类", e);
            } catch (Exception e) {
                log.error("代码生成失败", e);
            }
        };
    }

}
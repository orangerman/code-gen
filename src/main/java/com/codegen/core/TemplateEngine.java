package com.codegen.core;

import com.codegen.annotation.CodeType;
import com.codegen.model.GenerateContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 模板引擎
 */
@Slf4j
@Component
public class TemplateEngine {

    private Configuration configuration;

    @PostConstruct
    public void init() {
        configuration = new Configuration(Configuration.VERSION_2_3_33);
        configuration.setClassForTemplateLoading(this.getClass(), "/templates");
        configuration.setDefaultEncoding("UTF-8");
    }

    /**
     * 渲染模板
     */
    public String render(CodeType codeType, GenerateContext context) throws IOException {
        String templateName = getTemplateName(codeType);
        Template template = configuration.getTemplate(templateName);

        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("context", context);

        try (StringWriter writer = new StringWriter()) {
            template.process(dataModel, writer);
            return writer.toString();
        } catch (TemplateException e) {
            throw new IOException("Error rendering template: " + templateName, e);
        }
    }

    /**
     * 获取模板名称
     */
    private String getTemplateName(CodeType codeType) {
        return switch (codeType) {
            case MAPPER -> "mapper.ftl";
            case SERVICE -> "service.ftl";
            case JOB -> "job.ftl";
            default -> throw new IllegalArgumentException("No template for code type: " + codeType);
        };
    }
}
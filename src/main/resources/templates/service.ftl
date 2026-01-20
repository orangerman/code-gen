package ${context.basePackage}.service;

import ${context.className};
import ${context.basePackage}.dto.${context.simpleClassName}DTO;
import ${context.basePackage}.vo.${context.simpleClassName}VO;

import java.util.List;

/**
 * ${context.simpleClassName} Service
 *
 * @author ${context.author}
 * @date ${context.dateTime}
 */
public interface ${context.simpleClassName}Service {

    /**
     * 创建
     */
    ${context.simpleClassName}VO create(${context.simpleClassName}DTO dto);

    /**
     * 根据ID删除
     */
    boolean deleteById(${context.primaryKey.fieldType} ${context.primaryKey.fieldName});

    /**
     * 更新
     */
    ${context.simpleClassName}VO updateById(${context.primaryKey.fieldType} ${context.primaryKey.fieldName}, ${context.simpleClassName}DTO dto);

    /**
     * 根据ID查询
     */
    ${context.simpleClassName}VO getById(${context.primaryKey.fieldType} ${context.primaryKey.fieldName});

    /**
     * 查询所有
     */
    List<${context.simpleClassName}VO> listAll();

    /**
     * 分页查询
     */
    List<${context.simpleClassName}VO> listByPage(int page, int size);
}

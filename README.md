# 代码生成器 (Code Generator)

基于注解的 Java 代码生成器，使用 Java 24 + Spring Boot 3 + Maven 开发。

## 功能特性

- 基于注解自动生成常用代码
- 支持生成 DTO、VO、Mapper、Service、ServiceImpl、Controller
- 使用 FreeMarker 模板引擎，易于定制
- 提供 HTTP API 手动触发代码生成
- 支持字段级别的精细化配置

## 快速开始

### 1. 安装依赖

确保你已经安装了 Java 24 和 Maven。

```bash
mvn clean install
```

### 2. 启动应用

```bash
mvn spring-boot:run
```

应用将在 `http://localhost:8080` 启动。

### 3. 使用注解标记实体类

在你的实体类上添加 `@GenerateCode` 注解:

```java
package com.codegen.example;

import com.codegen.annotation.CodeType;
import com.codegen.annotation.FieldMapping;
import com.codegen.annotation.GenerateCode;
import lombok.Data;

@Data
@GenerateCode(
    value = {CodeType.ALL},  // 生成所有类型的代码
    basePackage = "com.codegen.example",
    author = "Your Name",
    tableName = "t_user"
)
public class User {

    @FieldMapping(
        column = "id",
        description = "用户ID",
        primaryKey = true
    )
    private Long id;

    @FieldMapping(
        column = "username",
        description = "用户名"
    )
    private String username;

    @FieldMapping(
        column = "password",
        description = "密码",
        ignoreInVo = true  // 在 VO 中忽略此字段
    )
    private String password;
}
```

### 4. 触发代码生成

#### 方式一: 为单个类生成代码

```bash
curl -X POST "http://localhost:8080/api/codegen/generate/class?className=com.codegen.example.User"
```

#### 方式二: 为整个包生成代码

```bash
curl -X POST "http://localhost:8080/api/codegen/generate/package?packageName=com.codegen.example"
```

### 5. 查看生成的代码

生成的代码位于项目根目录的 `generated-sources/` 文件夹中，结构如下:

```
generated-sources/
└── com/
    └── codegen/
        └── example/
            ├── dto/
            │   └── UserDTO.java
            ├── vo/
            │   └── UserVO.java
            ├── mapper/
            │   └── UserMapper.java
            ├── service/
            │   ├── UserService.java
            │   └── impl/
            │       └── UserServiceImpl.java
            └── controller/
                └── UserController.java
```

## 注解说明

### @GenerateCode

标记在类上，指定要生成的代码类型。

**属性:**
- `value`: 代码类型数组，可选值: `DTO`, `VO`, `MAPPER`, `SERVICE`, `SERVICE_IMPL`, `CONTROLLER`, `ALL`
- `basePackage`: 基础包名，默认使用实体类所在包
- `author`: 作者信息
- `moduleName`: 模块名称
- `tableName`: 数据库表名

### @FieldMapping

标记在字段上，配置字段的详细信息。

**属性:**
- `column`: 数据库字段名
- `description`: 字段描述
- `primaryKey`: 是否为主键
- `ignoreInDto`: 是否在 DTO 中忽略
- `ignoreInVo`: 是否在 VO 中忽略

## 代码生成类型

| 类型 | 说明 |
|------|------|
| `DTO` | 数据传输对象 |
| `VO` | 值对象 |
| `MAPPER` | MyBatis Mapper 接口 |
| `SERVICE` | Service 接口 |
| `SERVICE_IMPL` | Service 实现类 |
| `CONTROLLER` | REST Controller |
| `ALL` | 生成所有类型 |

## 项目结构

```
code-gen/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── codegen/
│   │   │           ├── annotation/          # 注解定义
│   │   │           ├── core/                # 核心引擎
│   │   │           ├── model/               # 数据模型
│   │   │           ├── controller/          # HTTP 接口
│   │   │           └── example/             # 示例实体
│   │   └── resources/
│   │       ├── templates/                   # FreeMarker 模板
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

## 自定义模板

模板文件位于 `src/main/resources/templates/` 目录，使用 FreeMarker 语法:

- `dto.ftl` - DTO 模板
- `vo.ftl` - VO 模板
- `mapper.ftl` - Mapper 模板
- `service.ftl` - Service 接口模板
- `serviceImpl.ftl` - Service 实现类模板
- `controller.ftl` - Controller 模板

你可以根据项目需求修改这些模板文件。

## 示例

项目包含两个示例实体:

1. `User.java` - 完整示例，生成所有类型的代码
2. `Product.java` - 部分示例，只生成指定类型的代码

运行以下命令查看效果:

```bash
curl -X POST "http://localhost:8080/api/codegen/generate/package?packageName=com.codegen.example"
```

## 技术栈

- Java 24
- Spring Boot 3.4.1
- Maven
- FreeMarker 2.3.33
- Lombok
- Reflections

## 注意事项

1. 确保实体类在类路径中可以被加载
2. 生成的代码仅供参考，请根据实际需求进行调整
3. 建议将 `generated-sources/` 添加到 `.gitignore` 文件中

## License

MIT License

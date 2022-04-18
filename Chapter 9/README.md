## 演示 Spring Boot 的自动装配以及 starter 是如何使用及实现的
***自动装配是基于 Spring 实现的; starter 是基于 Spring Boot 的扩展点来实现的,底层还是 Spring***
### 演示 starter
#### 内容
将A项目进行封装后变成B项目，将B项目变为starter，使其能被其他项目引入就可以用。（完成B项目的起步依赖）
#### 项目间关系
* autoconfigure-demo：演示如何使用starter
* geektime-spring-boot-autoconfigure：演示如何利用 Spring Boot 的扩展点来根据A项目进行自动装配
    * 利用 Spring Boot 会去扫描 META-INF 下的 spring.factories 文件后根据里面的配置项来扫描对应的自动配置类
* geektime-spring-boot-starter：演示组装一个 starter
* greeting：原始的被引入的项目A
### 演示自动装配
#### 自动装配的实现
* geektime-autoconfigure-backport：利用 Spring 实现
* autoconfigure-demo：演示如何使用自动装配
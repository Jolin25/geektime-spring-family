## 演示 Spring Boot 的自动装配以及 starter 是如何使用及实现的
***自动装配是基于 Spring 实现的; starter 是基于 Spring Boot 的扩展点来实现的,底层还是 Spring***
### 演示 starter
#### 内容
将 A项目 进行封装后变成 B项目，将 B项目 变为 starter，使其能被其他项目引入就可以用。（完成 B项目 的起步依赖）
#### 项目间关系
* autoconfigure-demo：演示如何使用 starter
* geektime-spring-boot-autoconfigure：演示如何利用 Spring Boot 的扩展点来根据 A项目 进行自动装配
    * 利用 Spring Boot 会去扫描 META-INF 下的 spring.factories 文件后根据里面的配置项来扫描对应的自动配置类
* geektime-spring-boot-starter：演示组装一个 starter
* greeting：原始的被引入的 项目A
### 演示自动装配
#### 自动装配的实现
* geektime-autoconfigure-backport：利用 Spring 实现
* autoconfigure-demo：演示如何使用自动装配


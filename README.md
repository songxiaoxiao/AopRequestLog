# AopRequestLog
java利用AOP实现切面或者拦截器记录请求日志，
用于项目接口记录入参数信息、header头信息、返回值，用于日志记录，排查


# Spring Boot Demo


### 开发环境

- **JDK1.8 +**
- **IntelliJ IDEA ULTIMATE 2018.2 +**

### 运行方式

1. `git clone git@github.com:songxiaoxiao/AopRequestLog.git`
2. 使用 IDEA 打开 clone 下来的项目
3. 在 IDEA 中打开项目
4. 在 IDEA 中 自动加载jar包
5. 运行gradle run 或者手动右击AppApplication类点击Run AppApplication选项
6. 浏览器输入http://localhost:9966/api/index访问事例测试

### 优化计划
   - 欢迎提优化意见

### 简单开发使用说明

##### 1. 拦截器方式写入日志
        1. 使用拦截器写入日志配置方法
            1. 拦截器是spring框架自带的功能可以直接使用
            2. 第一步：实现类的目录：interception/BasicsLogInterceptor
            3. 第二步：配置载入拦截器 config/InterceptorConfig
       > 本实例getParameter方法仅能获取from-data的参数数据，想要获取application/json参数需要使用流的方法从request中读取出来，但是
       ServletRequest中getReader()和getInputStream()只能调用一次。而又由于@RequestBody注解获取输出参数的方式也是根据流的方式获取的。所以我们前面使用流获取后，后面的@RequestBody就获取不到对应的输入流了。 
       解决方法：先读取流，然后在将流写进去就行了，具体网上搜一下这里不做多余的方案。因为下面我们会用AOP来做就不会有这个问题。
##### 1. AOP方式 写入日志  
        1. 使用拦截器写入日志配置方法  
            1. 第一步：导包
                	`compile("org.springframework.boot:spring-boot-starter-aop:2.0.6.RELEASE")`
                	`compile group: 'org.aspectj', name: 'aspectjweaver', version: '1.9.2'`
                	`compile group: 'org.aspectj', name: 'aspectjrt', version: '1.9.2'`
                	版本要对应自己的环境
            2. 第二步：实现类的目录：aspect/BasicsLogInterceptor，注意要有注解@Aspect
            3. 完成啦。
            
            
    



 




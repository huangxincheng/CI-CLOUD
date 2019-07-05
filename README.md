CI-CLOUD模块

    ---ci-cloud-cache
        缓存模块- 封装Redis工具使用
    
    ---ci-cloud-gateway
        网关模块- 统一入口，鉴权，限流
    
    ---ci-cloud-log
        日志模块- 统一配置logback 统一通过发送kafak消息，使用logstash收集消息到es。
    
    ---ci-cloud-mq
        MQ模块- 封装RocketMq工具类使用
        
    ---ci-cloud-order-api
        订单服务Feign接口
    
    ---ci-cloud-order-core
        订单服务核心代码
        
    ---ci-cloud-order-client
        订单服务提供内部其他服务调用Client
    
    ---ci-cloud-order-rest
        订单服务服务提供者
    
    ---ci-cloud-user-api
        用户服务Feign接口
    
    ---ci-cloud-user-core
        用户服务核心代码
        
    ---ci-cloud-user-clent
        用户服务提供内部其他服务调用client包
    
    ---ci-cloud-user-rest
        用户服务服务提供者
        
    ---ci-cloud-framework
        工具服务不涉及Spring框架
    
    ---ci-cloud-web-framework
        工具服务涉及Spring框架
部署

    Jenkinsfile
    
    deploy目录
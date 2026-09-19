controller: (接收请求（不写业务逻辑）)
common/： 通用组件：（Result，异常）
config/： 配置类：（MP插件、拦截器...）
service/impl/: 业务逻辑（核心）
mapper/: 映射 数据访问（继承BaseMapper)
entity/: 数据库实体（对应类）
dto/: Data Transfer Object（数据传输对象） 请求/相应对象（不直接暴露entity）

项目怎么分层的？"
链路：Controller 收参 → 
Service 处理业务 → 
Mapper 访问数据库。
Controller 里不写业务，
Service 里不碰 SQL，各层职责单一
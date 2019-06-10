# seckill
基于 `springboot2.x + redis + rabbitmq + mysql + thymeleaf` 实现的秒杀系统
性能: 
  - 使用redis实现了数据/页面的缓存
  - 使用rabbitmq实现了异步下单处理
  - 接口防刷限流
  - 秒杀接口隐藏
   

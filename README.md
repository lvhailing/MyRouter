# MyRouter
组件间解耦利器--主要思路是通过Google的auto-service工具自动扫描注解类（需要使用到android-apt来区分module名称），然后square的javapoet生成中间类，再由业务工程调用中间层，以达到解耦目的。


已经应用的线上项目中，属成熟框架

另外也支持注入机制，根据业务需要动态切换实际调用（使用动态代理实现）等

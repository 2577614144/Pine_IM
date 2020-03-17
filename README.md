
基于Netty Protobuf实现的Android即时通讯 心跳机制

### 开发背景

由于本人一直从事于即时通讯相关的公司，最近相对来说不太忙，所以动手开始编写一套netty+protobuffer的 Android sdk，光撸代码自己怎么行，特此记录一下自己如何一比一步搞出来这个IM SDK ，自己好好总结一下，也希望可以帮助到想开发Android即时通讯的童鞋。

开发环境：Window10

开发工具：AndroidStudio3.6    gradle-5.4.1-all

依赖库版本：netty-4.1.45.Final   protobuf-java:3.11.1

代码地址：[项目地址](https://github.com/2577614144/Pine_IM)


**待完成**

- 断线重连
- 消息重发
- 读写超时
- 离线消息


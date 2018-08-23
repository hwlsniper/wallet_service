eostoken后台代码

# 安装说明<br>

# 一、编写目的
	开发、测试、实施能快速部署该系统。
# 二、系统配置
## 1.运行环境
为保证系统能正常运行，需要满足以下条件：<br>
## 1.1服务器环境部署
1)操作系统：ubuntu server 16.04 <br>
2)软件运行环境：nginx，redis，JDK 1.8<br>
3)数据库：mongdb 4.0 ， Mysql 5.7及以上，数据库系统使用UTF-8字符集。<br>
4)依赖系统或功能：zookeeper、eos钱包服务。<br>
## 1.2开发环境部署
 ### 1.2.1 操作系统 
      Windows7及以上版本 
 ### 1.2.2 数据库环境安装。
 #### 1）Mysql安装
    下载地址： https://dev.mysql.com/downloads/mysql/5.7.html#downloads
     请下载mysql-5.7.20-winx64及以上的版本，安装后运行Etokenx_init.sql。
     地址（https://github.com/eostoken/wallet_service/tree/release/1.0.1/sql）
 #### 2）Mongodb安装
    下载地址：https://www.mongodb.com/download-center#community
     请下载4.0.0 及以上的版本。
     
 ### 1.2.3 Redis缓存
 #### 1）redis安装
    下载地址：http://www.redis.cn/download.html
    请下载Redis-x64-3.2.100及以上版本。
    设置redis的密码跟你代码里面配置的密码相同
    每次项目运行前先运行redis-server.exe
    
 ### 1.2.4 Zookeeper环境
 #### 1）Zookeeper 安装
    下载地址：https://www.apache.org/dyn/closer.cgi/zookeeper/     
    请下载一个zookeeper-3.4.2及以上版本。
    每次项目运行前先运行zkServer.cmd
    注：本项目如果要分布式部署请查询zookeeper及dubbo官网（zookeeper官网地址：http://zookeeper.apache.org/ dubbo官网：http://dubbo.apache.org/en-us/  ）做好相关的配置。
    
# 三、项目启动说明
##	1.打包
1）点击运行：maven.package.release.bat  打包, 生成下列文件：<br>
etoken-comp-admin-1.0.0.tar.gz<br>
etoken-comp-api-20180817.tar.gz<br>
etoken-comp-eosblock-1.0.0.tar.gz<br>
etoken-comp-market-1.0.0.tar.gz <br>
etoken-comp-news-1.0.0.tar.gz<br>
etoken-comp-user-1.0.0.tar.gz<br>
2）把tar包上传到服务器解压得到以下文件：<br>
app.jar  bin  logs<br>
3）进入到bin目录下运行”start.sh” (必须要在bin目标下运行”start.sh”)<br>
./start.sh  &         

##	2.项目启动顺序
1.market <br>
2.news<br>
3.eosblock<br>
4.user<br>
5.admin<br>
6.api<br>
# 四、模块介绍
1.market<br>
Market：行情<br>
2.news<br>
News：资讯<br>
3.eosblock<br>
Eosblock：交易<br>
4.user<br>
User：用户<br>
5.admin<br>
Admin：管理后台<br>
6.api<br>
Api：接口<br>



                     





































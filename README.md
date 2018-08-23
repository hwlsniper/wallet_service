eostoken后台代码

# 安装说明<br>

# 一、编写目的
	开发、测试、实施能快速部署该系统。
# 二、系统配置
##	1.运行环境
为保证系统能正常运行，需要满足以下条件：<br>
1)操作系统：ubuntu server 16.04 <br>
2)软件运行环境：nginx，redis，JDK 1.8<br>
3)数据库：mongdb 4.0 ， Mysql 5.7及以上，数据库系统使用UTF-8字符集。<br>
4)依赖系统或功能：zookeeper、eos钱包服务。<br>
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



                      后台运行环境部署文档

1.数据库环境安装。<br>
  Mysql安装<br>
     下载地址： https://dev.mysql.com/downloads/mysql/5.7.html#downloads<br>
     请下载mysql-5.7.20-winx64及以上的版本，安装后运行Etokenx_init.sql。<br>
     地址（https://github.com/eostoken/wallet_service/tree/release/1.0.1/sql）<br>
  Mongodb安装<br>
     https://www.mongodb.com/download-center#community<br>
     请下载4.0.0 及以上的版本。<br>
2.Redis缓存<br>
	redis安装<br>
    下载地址：http://www.redis.cn/download.html<br>
    请下载Redis-x64-3.2.100及以上版本。<br>
    设置redis的密码跟你代码里面配置的密码相同<br>
    每次项目运行前先运行redis-server.exe<br>
3.Zookeeper环境<br>
    Zookeeper 安装<br>
    下载地址：https://www.apache.org/dyn/closer.cgi/zookeeper/   <br>      
    请下载一个zookeeper-3.4.2及遇上版本。<br>
    每次项目运行前先运行zkServer.cmd<br>
    注：本项目如果要分布式部署请查询zookeeper及dubbo官网（zookeeper官网地址：http://zookeeper.apache.org/ dubbo官网：http://dubbo.apache.org/en-us/  ）做好相关的配置。<br>
4.项目运行<br>
    项目下载后用编码工具引入已存在的maven项目。(编码工具这边用的是Spring Tools Suite (STS)）<br>
    然后在父项目下maven install 打包所有项目<br>
    然后按照以下顺序启动。<br>
    1.market<br>
    2.news<br>
    3.eosblock<br>
    4.user<br>
    5.Admin<br>
    6.Api<br>


































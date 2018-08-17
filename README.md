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

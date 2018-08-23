/*
SQLyog Ultimate v12.5.0 (64 bit)
MySQL - 5.7.13 : Database - etokenx
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`etokenx` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;

USE `etokenx`;

/*Table structure for table `admin` */

DROP TABLE IF EXISTS `admin`;

CREATE TABLE `admin` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `login_error` int(255) NOT NULL DEFAULT '5',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `admin` */

/*Table structure for table `banner` */

DROP TABLE IF EXISTS `banner`;

CREATE TABLE `banner` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `url` varchar(1024) COLLATE utf8mb4_bin NOT NULL,
  `img` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `banner` */

insert  into `banner`(`id`,`title`,`url`,`img`,`createdate`,`modifydate`) values 
(14,'糖果派送',' http://static.eostoken.im/eos.html','http://static.eostoken.im/images/20180319/1521437874488.png','2018-03-09 19:20:49','2018-03-20 14:17:36'),
(15,'倒计时','http://eostoken.im/','http://static.eostoken.im/images/20180329/1522339210498.png','2018-03-28 07:24:03','2018-03-29 16:00:14');

/*Table structure for table `coins` */

DROP TABLE IF EXISTS `coins`;

CREATE TABLE `coins` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '????',
  `createdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `site` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '????',
  `intr` varchar(2048) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '????',
  `img` varchar(1024) COLLATE utf8mb4_bin NOT NULL DEFAULT 'http://static.eostoken.im/pocket_asset/asset_default.png' COMMENT 'ͼ??',
  `tag` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '??ǩ',
  `total` bigint(255) DEFAULT NULL COMMENT '????',
  `marke` bigint(20) DEFAULT NULL COMMENT '??ͨ??',
  `code` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'CODE',
  `symble` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '价格对',
  `contract_account` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '合约账号',
  `is_support_market` varchar(10) COLLATE utf8mb4_bin NOT NULL DEFAULT 'n' COMMENT '是否支持行情',
  `precision_number` int(11) DEFAULT NULL COMMENT '精确小数位数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `coins` */

insert  into `coins`(`id`,`name`,`createdate`,`modifydate`,`site`,`intr`,`img`,`tag`,`total`,`marke`,`code`,`symble`,`contract_account`,`is_support_market`,`precision_number`) values 
(27,'EOS','2018-03-09 19:30:46','2018-07-03 10:01:10','https://eos.io/','eos 为区块链奇才 BM （daniel larimer）领导开发的类似操作系统的区块链架构平台，旨在实现分布式应用的性能扩展。eos 提供帐户，身份验证，数据库，异步通信以及在数以百计的cpu或群集上的程序调度。该技术的最终形式是一个区块链体系架构，该区块链每秒可以支持数百万个交易，同时普通用户无需支付使用费用。','http://static.eostoken.im/images/20180808/1533718168903.png',NULL,10000000000,1009132671,'eos','EOS-USDT','eosio.token','y',4),
(32,'MTN','2018-03-09 19:32:30','2018-07-02 16:06:59','http://motion.one/','Motion 是一个基于区块链的分布式游戏分发系统，也是一个以游戏视频为分发载体的社区，它可以让广大用户参与游戏的开发、测试、分发全流程。通过这种模式，透明化中间环节，让开发者得到大部分收益，用户则可以通过游戏测试来促进游戏体验的迭代与创新，还可以通过游戏分发来得到一部分收益，从而使整个游戏产业真正的参与者全部受益，成功完成游戏分发形式的升级。我们希望通过我们的经验和探索来改变游戏的分发模式，从而让游戏开发者和用户真正获益。','http://static.eostoken.im/images/20180319/1521432698152.png',NULL,2000000000,2000000000,'mtn','MTN-USDT',NULL,'y',NULL),
(37,'EOSDAC','2018-03-09 19:34:02','2018-07-09 12:41:48','https://www.dew.one/','DEW代币是支付平台的佣金，在DEW平台上交易时必须消耗的DEW代币，类似币安的BNB代币，但是不同的是，平台是以DEW代币的形式收取手续费收入，并且将直接销毁收到代币的40％，因此，随着交易在系统内继续发生，DEW代币将在整个交易系统中被持续消耗。','http://static.eostoken.im/images/20180319/1521432724780.png',NULL,994895254,994895254,'eosdac','EOSDAC-USDT','eosdactokens','y',4),
(39,'EON','2018-03-09 19:34:02','2018-07-02 16:07:01','https://www.dew.one/','DEW代币是支付平台的佣金，在DEW平台上交易时必须消耗的DEW代币，类似币安的BNB代币，但是不同的是，平台是以DEW代币的形式收取手续费收入，并且将直接销毁收到代币的40％，因此，随着交易在系统内继续发生，DEW代币将在整个交易系统中被持续消耗。','http://static.eostoken.im/images/20180319/1521432724780.png',NULL,200000000,200000000,'eon','EON-ETH',NULL,'y',NULL),
(40,'MSP','2018-07-02 22:08:30','2018-07-03 09:21:11','https://mothership.c','MSP 将在 Ethereum 平台上作为 ERC20 令牌构建。总供应量为200,000,000 MSP。MSP 作为 Mothership 的核心部分，并用于获得优质和白金账户层，应用平台层，社区管理和洞察奖励，对 Mothership beta 产品的访问，声誉管理等。','http://static.eostoken.im/images/20180813/1534153124454.png',NULL,1000000000,1000000000,'msp','MSP','espritblocke','n',4),
(41,'ADD','2018-07-03 13:50:13','2018-07-27 05:42:30','http://www.eosadd.com/','ADD 致力于建设基于 EOS 数据与应用生态，提供从“互联?+”到“区块链+” 的转型的数据中枢与应?平台。ADD打造的ADD数据银?是基于EOS的全球超级计算机与超级节点的实时数据中枢与应用平台，这是个可扩展、低延迟、可交互、并拥有可扩安全链域护栏的分布式网络平台。 ','http://static.eostoken.im/images/20180809/1533801224110.png',NULL,10000000000,10000000000,'add','ADD-ETH','eosadddddddd','y',4),
(42,'EOSCN','2018-07-03 13:51:13','2018-07-03 13:51:13',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,2000000000,2000000000,'eoscn',NULL,'zhiming11111','n',4),
(43,'EOSONE','2018-07-03 13:52:50','2018-07-03 13:52:50',NULL,NULL,'http://static.eostoken.im/images/20180813/1534152214608.png',NULL,10000000000,10000000000,'eosone','EOSONE','hezdanbtgege','n',4),
(44,'EOSWIN','2018-07-03 13:55:15','2018-07-09 12:42:43',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,100000000,100000000,'eoswin',NULL,'etwineos1111','n',4),
(45,'EAC','2018-07-03 13:55:48','2018-07-03 13:55:48','http://earthcoin.io/','地球币是2013年12月20日在加拿大多伦多发布的一款基于scrypt算法的全球性的加密货币，致力于可持续和公平的发展。地球币总量为135亿，以365天为一个周期，开始每块10000个币，以2000为振幅的正弦曲线变化。','http://static.eostoken.im/images/20180813/1534150515893.png',NULL,10000000000,10000000000,'eac','EAC','bcdn12345123','n',4),
(47,'CET','2018-07-03 13:57:16','2018-07-03 13:57:16','https://chaince.com/','Chaince是一个中心化的交易所，主打EOS交易对。CET作为Chaince的平台币，虽然实际用处还未有细则，但估计与其它平台币一样，分红、回购、抵扣手续费等可能是主要用途。','http://static.eostoken.im/images/20180813/1534150292234.png',NULL,2000000000,2000000000,'cet','CET','eosiochaince','n',4),
(48,'HORUS','2018-07-03 13:57:51','2018-07-03 13:57:51',NULL,'\nHorusPay是一个去中心化的全球薪资门户网站，它使得全球的实体能够使用区块链技术与国际薪资供应商交换私有、加密、安全的数据。','http://static.eostoken.im/images/20180813/1534152524391.png',NULL,1200000000,1200000000,'horus','HORUS','horustokenio','n',4),
(51,'IQ','2018-07-16 10:03:29','2018-07-19 07:22:13','https://iq.cash/','IQ币，总量5.6亿，IQCash加密货币，NeoScrypt算法，支持CPU显卡挖矿！','http://static.eostoken.im/images/20180817/1534489188841.png',NULL,100000000000,10000000000,'iq','IQ-ETH','everipediaiq','y',3),
(52,'ECTT','2018-07-17 00:40:52','2018-07-17 00:40:52',NULL,NULL,'http://static.eostoken.im/images/20180804/1533381706348.png',NULL,6000000000,6000000000,'ECTT-ETH','ECTT','ectchaincoin','n',4),
(53,'ETWIN','2018-07-17 17:29:17','2018-07-17 17:29:17',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,100000000,100000000,'etwin',NULL,'yangerpeng22','n',4),
(54,'CHL','2018-07-18 13:02:23','2018-07-18 13:02:23',NULL,NULL,'http://static.eostoken.im/images/20180813/1534150743788.png',NULL,2700000000,2700000000,'chl','CHL','challengedac','n',4),
(55,'KARMA','2018-07-18 13:03:14','2018-07-18 13:03:14','https://www.karmaapp',NULL,'http://static.eostoken.im/images/20180813/1534152692541.png',NULL,100000000000,9469148945,'karma','KARMA','therealkarma','n',4),
(56,'EOX','2018-07-18 13:03:57','2018-07-18 13:03:57','https://www.eoxlab.i','EOX希望基于EOS做成一个区块链的电子商务平台，在这个平台上允许使用基于EOS的加密货币和资产进行消费。','http://static.eostoken.im/images/20180813/1534152271833.png',NULL,10000000000,3200000000,'eox','EOX','eoxeoxeoxeox','n',4),
(57,'EDNA','2018-07-18 13:04:33','2018-07-18 13:04:33',NULL,NULL,'http://static.eostoken.im/images/20180813/1534151505478.png',NULL,1300000000,1000000000,'edna','EDNA','ednazztokens','n',4),
(58,'ATD','2018-07-18 13:05:09','2018-07-18 13:05:09','https://www.atidium.','ATDIUM项目目标是做一个搭建在EOS上的支付体系，最大的优势是自成体系的财务管理系统和依托EOS网络可以免交易手续费的特性。','http://static.eostoken.im/images/20180813/1534149792488.png',NULL,1500000000,1500000000,'atd','ATD','eosatidiumio','n',4),
(59,'EETH','2018-07-21 07:28:29','2018-07-21 07:28:29',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,10000000000,5002000000,'eeth',NULL,'ethsidechain','n',4),
(60,'EOSMKT','2018-07-21 16:21:31','2018-07-21 16:21:31',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,102026791,'eosmkt',NULL,'onemarketeos','n',4),
(61,'ESB','2018-07-21 16:24:21','2018-07-21 16:24:21',NULL,NULL,'http://static.eostoken.im/images/20180813/1534152409297.png',NULL,20000000000,1180259208,'esb','ESB','esbcointoken','n',4),
(62,'BLACK','2018-07-21 16:30:59','2018-07-21 16:30:59',NULL,NULL,'http://static.eostoken.im/images/20180813/1534150227690.png',NULL,3000000000,896582720,'black','BLACK','eosblackteam','n',4),
(63,'EBT','2018-07-22 01:10:18','2018-07-22 01:10:18','www.ebittree.com/',NULL,'http://static.eostoken.im/images/20180813/1534151475302.png',NULL,100000000000,100000000000,'ebt','EBT','theeosbutton','n',4),
(64,'RIDL','2018-07-22 01:31:16','2018-07-22 01:31:16',NULL,NULL,'http://static.eostoken.im/images/20180813/1534153236690.png',NULL,1000000000,1000000000,'ridl','RIDL','gy4doojqgyge','n',4),
(65,'EOSNTS','2018-07-25 07:57:48','2018-07-25 07:57:48',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'eosnts',NULL,'eosninetiess','n',4),
(66,'POC','2018-07-27 03:47:36','2018-07-27 03:47:36',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'poc',NULL,'paycoinchain','n',4),
(67,'VVV','2018-07-27 10:05:36','2018-07-27 10:05:36',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'vvv',NULL,'xteameos1234','n',4),
(68,'OCT','2018-07-31 07:51:08','2018-07-31 07:51:08',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,200000000,200000000,'oct',NULL,'octtothemoon','n',4),
(69,'AC','2018-08-01 12:23:55','2018-08-01 12:23:55',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'ac',NULL,'aircoin.eos','n',4),
(70,'LOVE','2018-08-02 09:19:22','2018-08-02 09:19:22',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000,1000000,'love',NULL,'okkkkkkkkkkk','n',6),
(71,'IPOS','2018-08-04 03:19:27','2018-08-04 03:19:27',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,200040000,'ipos',NULL,'oo1122334455','n',4),
(72,'EPRA','2018-08-04 17:16:55','2018-08-04 17:16:55',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,10000000000,10000000000,'epra',NULL,'epraofficial','n',4),
(73,'MOYU','2018-08-06 03:59:40','2018-08-06 03:59:40',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'moyu',NULL,'okkkkkkkkkkk','n',4),
(74,'GAME','2018-08-06 04:00:57','2018-08-06 04:00:57',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'game',NULL,'okkkkkkkkkkk','n',4),
(75,'TEST','2018-08-06 11:54:33','2018-08-06 11:54:33',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,10000000000,270000000,'test',NULL,'issuemytoken','n',4),
(76,'NYFZ','2018-08-09 06:07:00','2018-08-09 06:07:00',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,100000000,100000000,'nyfz',NULL,'okkkkkkkkkkk','n',0),
(77,'WHB','2018-08-15 12:43:22','2018-08-15 12:43:22',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'whb',NULL,'okkkkkkkkkkk','n',4),
(78,'QB','2018-08-16 09:01:48','2018-08-16 09:01:48',NULL,NULL,'http://static.eostoken.im/images/20180821/1534821015046.png',NULL,1000000000,1000000000,'qb','QB','okkkkkkkkkkk','n',0),
(80,'DIDI','2018-08-17 03:46:00','2018-08-17 03:46:00',NULL,NULL,'http://static.eostoken.im/images/20180817/1534489522603.png',NULL,100000000,100000000,'didi','DIDI','okkkkkkkkkkk','n',0),
(81,'BT','2018-08-18 01:52:28','2018-08-18 01:52:28',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,100000000,10327497,'bt',NULL,'eosbtextoken','n',4),
(82,'FBT','2018-08-21 04:15:44','2018-08-21 04:15:44',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'fbt',NULL,'okkkkkkkkkkk','n',3),
(83,'BOID','2018-08-21 06:42:28','2018-08-21 06:42:28',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,25000000000,6212333237,'boid',NULL,'boidcomtoken','n',4),
(84,'TEA','2018-08-21 09:33:21','2018-08-21 09:33:21',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'tea',NULL,'linzongsheng','n',4),
(85,'BOSS','2018-08-21 22:28:04','2018-08-21 22:28:04',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'boss',NULL,'linzongsheng','n',4),
(86,'KC','2018-08-22 00:27:03','2018-08-22 00:27:03',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,10000000000,10000000000,'kc',NULL,'linzongsheng','n',4),
(87,'ZCB','2018-08-22 06:35:30','2018-08-22 06:35:30',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,3000000000,3000000000,'zcb',NULL,'12345fjfzhjh','n',4),
(88,'HE','2018-08-22 10:14:28','2018-08-22 10:14:28',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,50000000000,50000000000,'he',NULL,'okkkkkkkkkkk','n',4),
(89,'GOLD','2018-08-22 10:35:46','2018-08-22 10:35:46',NULL,NULL,'http://static.eostoken.im/pocket_asset/asset_default.png',NULL,1000000000,1000000000,'gold',NULL,'okkkkkkkkkkk','n',4);

/*Table structure for table `delegatebw` */

DROP TABLE IF EXISTS `delegatebw`;

CREATE TABLE `delegatebw` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `account_name` varchar(50) DEFAULT NULL COMMENT '为那个账号免费抵押了资源',
  `status` bigint(20) DEFAULT NULL COMMENT '0为正在抵押1为已经赎回',
  `cpu` varchar(20) DEFAULT NULL COMMENT '计算资源单位为EOS',
  `net` varchar(20) DEFAULT NULL COMMENT '网络资源单位为EOS',
  `createdate` datetime DEFAULT CURRENT_TIMESTAMP,
  `modifydate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `delegatebw` */

/*Table structure for table `eos_account_order` */

DROP TABLE IF EXISTS `eos_account_order`;

CREATE TABLE `eos_account_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `status` varchar(50) NOT NULL DEFAULT 'paid' COMMENT '状态',
  `uid` bigint(20) NOT NULL COMMENT '用户UID',
  `username` varchar(100) NOT NULL COMMENT '用户名',
  `amount` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '订单金额',
  `ram` bigint(20) NOT NULL DEFAULT '0' COMMENT '内存',
  `cpu` bigint(20) NOT NULL DEFAULT '0' COMMENT 'CPU',
  `net` bigint(20) NOT NULL DEFAULT '0' COMMENT '网络',
  `account_name` varchar(100) DEFAULT NULL COMMENT 'EOS账号',
  `public_key` varchar(255) DEFAULT NULL COMMENT 'EOS账号公钥',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updatedate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='EOS账号订单';

/*Data for the table `eos_account_order` */

/*Table structure for table `eos_elector` */

DROP TABLE IF EXISTS `eos_elector`;

CREATE TABLE `eos_elector` (
  `id` bigint(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(200) NOT NULL COMMENT '候选名称',
  `account` varchar(100) DEFAULT NULL COMMENT '账号',
  `icon` varchar(200) DEFAULT NULL COMMENT '图标',
  `region` varchar(200) DEFAULT NULL COMMENT '地区',
  `url` varchar(200) DEFAULT NULL COMMENT '官网地址',
  `introduce` text COMMENT '团队介绍',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `modifydate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改日期',
  `deleted` int(11) DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8mb4 COMMENT='EOS团队竞选人信息';

/*Data for the table `eos_elector` */

insert  into `eos_elector`(`id`,`name`,`account`,`icon`,`region`,`url`,`introduce`,`createdate`,`modifydate`,`deleted`) values 
(1,'MEET.ONE','eosiomeetone','http://static.eostoken.im/eosElector/77_0000.png','新加坡','https://meet.one/','MEET.ONE是新加坡EOS全球节点竞选团队，致力于构建EOS生态入口。团队具有坚实的技术实力，核心成员均来自顶级互联网公司，拥有亿级用户开发和运营经验。MEET.ONE积极为生态做贡献。2月，开发了首个微信小程序-EOS小助手。帮助用户学习映射并验证结果，保障资产安全，目前已有数十万用户。3月，参与组建了Scholar Testnet。并首先完成BIOS BOOT测试。4月，为全球用户开发了Chrome插件EOS KIT，集合了EOS全球资讯精选、Twitter账户速览、Dapp列表和日尸列表等功能。5月，APP客户端已在内测，可进行EOS资产管理、节点投票、Dapp入口和资讯速览等功能。未来，将发起MEET.ONE侧链，提供EOS开发环境孵化Dapp。MEET.ONE社区秉持共建、贡献、平等的价值观，目前已覆盖数十万用户。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(2,'EOS CANNON','eoscannonchn','http://static.eostoken.im/eosElector/78_0001.png','中国','https://eoscannon.io','Eos的发展将会为区块链以更好的方式改变着我们的生活，我们努力为EOS分散式生态系统做贡献!作为一个具有很高声望的社区，我们愿意连接并促进海外和中国EOS社区之间的共享。此外，我们很乐意带领大多数国内token持有者，参与全球EOS社区的增长。EOS Cannon将是一个可靠的区块生产者，致力为社区服务。此外，我们计划成为积极的EOS生态系统贡献者和项目孵化器。而不仅仅是一个EOS社区，我们将成为中国重要的EOS合作伙伴。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(3,'TOP.ONE','top111111111','http://static.eostoken.im/eosElector/79_0002.png','英属维尔京群岛','https//top.one','我们是一家全语种孵化交换平台TOP.ONE，TOP.ONE很高兴你们来社区分享交流意见。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(4,'EOS Gravity','eosisgravity','http://static.eostoken.im/eosElector/80_0003.png','中国','http://eosgravity.com','总部位于中国上海的EOS Gravity是一个区块链生态社区。聚集了大量区块链和EOS追随者。该社区致力于为区块链技术感兴趣的人士提供帮助，并通过我们对区块链技术和EOS公共链的深入分析和明确指导鼓励更大规模的参与。此外，我们通过提供富有洞察力的评论，最新消息以及专业技术支持，为EOS生态系统的发展做出贡献。未来，EOS Gravity将致力于开发EOS社区。重点关注价值信息、技术讨论、离线研讨会、高峰论坛和生态系统的Dapp等各个方面，这些都为过渡到一个全新的世界。我们的价值观: 安全，团结，高级和繁荣。我们的使命:为快速发展的EOS行业做出贡献。我们的未来前景:成为全球领先的EOS社区。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(5,'EOSLaoMao','eoslaomaocom','http://static.eostoken.im/eosElector/81_0004.png','中国','http://eoslaomao.com','EOSLaoMao，由著名区块链倡导者和INBlockchain合作伙伴老猫发起。目标是在EOS主网6月份上线时成为21个超级节点之一。我们唯一的目标是维护一个公平和稳定的Eos Block Producer节点，由于利润不在我们的考虑范围内，因此我们将用它来以多种方式推广EOS，我们位于东京，我们可以使用最好的云服务来运行稳定的节点。\"','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(6,'EOSBIXIN','eosbixinboot','http://static.eostoken.im/eosElector/82_0005.png','泰国','http://en.eosbixin.com/','EOSBIXIN小组隶属于币信，我们认识到区块链正从底层技术时代迈向产品时代，EOS作为高性能区块链。将承载人类全方位价值的自由发行和流通，所以我们打造EOSBIXIN超级节点和币信。EOS账户两大底层基础，为EOS提供强健的网络，同时让大家更方便使用EOS，实现EOS生态的繁荣发展。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(7,'HeIIoEOS','helloeoscnbp','http://static.eostoken.im/eosElector/83_0006.png','中国','https://www.helloeos.com.cn/','HeIIoEOS，在中国的区块生产者团队之一。具有开发和维护Graphene区块链的能力，以及拥有3万多个用户基础的社区，HeIIoEOS将会最大限度地帮助实现有效的社区治理，和一个充满活力、健康的EOS区块链生态系统。HeIIoEOS是最早将EOS引入华语社区的群体，吸引了相当多的人，从而形成了最早的国内EOS社区。作为比特股的成员，HeIIEOS多年来一直是Bytemaster的坚定支持者。自从比特股1.0启动以来，我们就一直专注于操作和维护节点服务器并提供技术，而比特股区块链网络支持至少有3名活跃的独立操作者。HeIIoEOS团队在梓岑和Alex的带领下，创建和管理了几个社交媒体集团，不仅介绍了EOS，还翻译了许多技术和分析文章，帮助社区更好地理解了EOS。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(8,'EOS Detroit','eosiodetroit','http://static.eostoken.im/eosElector/84_0007.png','美国','https://eosdetroit.io/','Eos的发展将会为区块链以更好的方式改变着我们的生活，我们努力为EOS分散式生态系统做贡献!作为一个具有很高声望的社区，我们愿意连接并促进海外和中国EOS社区之间的共享。此外，我们很乐意带领大多数国内token持有者，参与全球EOS社区的增长。EOS Cannon将是一个可靠的区块生产者，致力为社区服务。此外，我们计划成为积极的EOS生态系统贡献者和项目孵化器。而不仅仅是一个EOS社区，我们将成为中国重要的EOS合作伙伴。数据来源:EOS小助手小程序','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(9,'EOZNz',NULL,'http://static.eostoken.im/eosElector/85_0008.png','新西兰','https://eosnewzealand.com/','EOSNz通过社区的反馈不断完善基础设施，并且致力于开放和创建自由化途径，以便应用程序可以通过经济高效的方式在现实世界中蓬勃发展。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(10,'EOSHCC','eoshcchealth','http://static.eostoken.im/eosElector/86_0009.png','新加坡','https://eoshcc.keybase.pub/','EOS.HCC不仅是一个区块生产者，我们还将是EOS在亚太地区的重要合作伙伴，我们保持积极的EOS社区活动，是EOS的生态贡献者之一，我们相信EOS强大的技术指标和低成本用户具有非常大的应用空间，特别是在观众中，处理大量数据和数据存储;希望EOSHCC成为EOS生态的先驱，并从小众应用领域拓展更多的EOS生态可能性。相信EOS可以更好地改变我们的生活方式。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(11,'EOS.fish','eosfishrocks','http://static.eostoken.im/eosElector/87_0010.png','泰国','https://eos.fish/','eos.fish团队来自泰国曼谷，是由一群经验丰富的加密货币拥护者和区块链信仰者组成。均为区块链行业内的专家。eos.fish将高端硬件与下一代技术紧密结合，打造一个全球化和充满活力的社区。与EOS爱好者共建我们的未来!eos.fish致力于在以下领域发展基于区块链的创新:财务，跨境社区，技术，物流，高级硬件。We are eos.fish from Bangkok, Thailand, our founders comprise anexperienced team of crypto advocates and blockchain believers, with anabundance of expertise.eos.fish combines high-end hardware with next level technology. Weare creating a global and fun community, together we will build ourfuture!Eos.fish aims to develop blockchain一based innovations in: Finance,Cross-border communities, Technologies, Logistics, Premiumhardware.','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(12,'EOSLove','eoslovebirds','http://static.eostoken.im/eosElector/88_0011.png','美国','http://eoslove.io/','EOSLOVE决定致力于改善社会，让我们看到不平等和不公正持续存在的各行各业。在过着合理的生活，达到了我们的职业生涯高峰期后，我们满意地回报社会，为改善人民群众做好技术工作，这是我们工作满意度在我们生活的这一点上所取得的成就。Eos基于生命，自由和财产价值以及技术背后的原则构建的原则呼吁我们作为将我们的使命推进主流的合适工具。对于我们需要在我们的社会效益努力中展示的透明度和敏捷性，区块链可能没有更好的机制。有了这个作为我们人生愿望的基础，我们正在通过以最有效和最具成本效益的方式提供真正需要的帮助，开始改变世界的旅程。我们的团队来自各种各样的背景和文化/国界，这将为我们提供一个独特的视角，看看如何解决各种文化和国家社会众多不同部门的各种问题。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(13,'EOS Silicon Valley','eossv12eossv','http://static.eostoken.im/eosElector/89_0012.png','美国','https://eossv.io/','EOS Silicon Valley，是位于硅谷的EOS爱好者团队，在技术，产品，营销和运营方面拥有丰富的专业知识。凭借我们在技术人才以及社区资源和影响力方面的出色表现，我们希望成为21个区块生产商之一，协助在main net中启动EOS，并使分散式应用程序更易于使用和实用!','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(14,'BIockCC',NULL,'http://static.eostoken.im/eosElector/90_0013.png','中国','https://eos.block.cc/','Block.CC是一个致力于开发区块链服务的团队，使用户能够更轻松，更可靠地在各个领域使用区块链技术。我们提供与区块链相关的技术和数据服务，我们致力于挖掘用户需求，解决用户的痛点，通过区块链技术全面提升用户体验。我们建立了区块链数据社区，希望社区的所有参与者都能体验区块链技术的透明性,开放性和安全性。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(15,'EOS Amsterdam','eosamsterdam','http://static.eostoken.im/eosElector/91_0014.png','荷兰','https://eosamsterdam.net/','EOS Amsterdam我们位于阿姆斯特丹，一个拥有受过良好教育，拥有自由和国际化的人口的繁荣城市。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(16,'EOSVibes','eosvibesbloc','http://static.eostoken.im/eosElector/92_0015.png','荷兰','https://eosvibes.io/','大家好!我们是EOSVibes一”Dapp发展”EOS Block制片人候选人。我们是谁：生活在一个不同的世界各地的地方有才华，上进和创意blockchain爱好者的多元化团队。EOSVibes是一种荣誉和名誉为基础的组织。我们已经创建了我们宪法的初稿。并使其开源社区输入在GitHub上。我们的使命是为EOS社区创造持续的价值。EOSVibes承诺:以最佳方式保护EOS区块链。同时保护在EOS.IO之上创建和部署具有真实世界用例的新Dapp我们的第一个Dapp团队通过免费的Airdrop向所有EOS token持有者发布(在EOS区块链启动后的90天内);通过EOS Vibes创建的所有未来Dapps将遵循类似的模式空投，这将给整个EOS社区建设EOS供电token的投资组合的机会。EOSVibes还将公布来自EOS社区的新Dapp创意，让每个人都有机会将他们梦想到Dapp上发展。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(17,'SuperONE','superoneiobp','http://static.eostoken.im/eosElector/93_0016.png','中国','https://superone.io/','SuperONE项目发起人(社区ID: crazybit)是区块链领域的高级参与者，侧重于区块链领域的管理流程和审美流程，重点关注区块链评估和bistShares社区，并对石墨烯的内核进行了深入研究，作为社区领先的开发团队，SuperONE (superone.io)账户的开发旨在满足商业应用的实用性和便利性需求。为用户提供更安全，更多透明，分散的上链式交易和数字资产管理平台。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(18,'EOSWenzhou','eoswenzhoubp','http://static.eostoken.im/eosElector/94_0017.png','中国','http://eoswz.com/','EOS温州是一个成立于2017年10月份的社区。我们是最早接触区块链技术及EOS.IO技术团队和爱好者，目前拥有核心团队10人，运营团队15人。我们的愿景温州人具有艰苦奋斗的创业精神，闯荡天下、四海为家的开拓精神，善于创新的创造精神。当前约有300万温州商人分布在全世界，我们将支持和促进EOS.IO分布式生态系统软件成功，同时依托及沟通全世界温商，利用我们在温商当中的极大影响力优势，将金融，跨境汇款，借贷，债券，轻工业，制造业，物流，电商，物联网等产业，进行区块链升级，以利用区块链为实体经济服务为宗旨.建设一个更美好的区块链3.0的未来。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(19,'BIockPro.One','blockpro1eos','http://static.eostoken.im/eosElector/95_0018.png','未知','http://blockpro.one/','区块链和块生产在BIockPro.One，我们通过构建区块链的到来感到非常兴奋，并且正在构建新的未来。让全世界成为更安全的玩家。DApps和智能合约我们构建智能应用程序。照顾未来的需求新一代并支持去中心化自治组织，它们构建了社区需要的东西。运营管理高度冗余的处理器架构，存储阵列和高速/高带宽连接，并具有防篡改保护构成我们数据中心架构的核心。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(20,'SaItBlock','saltblockeos','http://static.eostoken.im/eosElector/96_0019.png','美国','https://www.saltblock.io/','大家好，我们是位于美国犹他州盐湖城的盐区块生产者SaItBlock.我们对即将到来的EOS的推出感到非常兴奋，我们想介绍自己并告诉你我们应该如何计划被选为超级节点。我们相信EOS将会把区块链和去中心化技术带入主流，不仅因为EOSIO软件是由Block.One创建的。而且还因为我们很幸运能成为其中一部分的令人惊叹的社区。我们的使命是将我们的知识和技能转化为积极和可靠的平台，同时通过深思熟虑的行动帮助EOS社区实现伟大的事业。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(21,'EOS UK','eosukblocpro','http://static.eostoken.im/eosElector/97_0020.png','英格兰','https://eosuk.io/','EOS UK坐落于伦敦，创始人Roger John Davies，打算为我区块生成者提供EOS网络上最透明和可靠的节点。除了对所有员工的工资合理和标准的运营成本，所有的初始收入将漏投入我们的区块生产基础设施。以确保在弹性极限和达到的性能。未来想建立EOS大学，重点开展“下一代”下一代”在计算机科学，政治和经济问题。以及这些学科通过使用Blockchain技术和断词被从根本上改变。EOS UK作为候选团队和区块生产者的一起推动EOS理念，通过聚会，信息发布和协作与商业和学术机构利用网络来造福英国。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(22,'eosbidu',NULL,'http://static.eostoken.im/eosElector/98_0021.png','中国','http://www.eosbidu.com/','EOSbidu基于每日币读自媒体，为区块链项目提供信息、评级服务。我们致力于建立国内一流的区块链咨询评级媒体。通过普及区块链的基本知识，导出原创内容，创建互动社区，孵化区块链项目，我们将打造中国最具影响力和服务的区块链社区。自2017年4月成立以来。每日币读在微信公众号、微博上获得极高的人气和声誉，截至2018年4月20日，已经积累了超过10万名会员。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(23,'BitSpace','bitspacenode','http://static.eostoken.im/eosElector/99_0022.png','挪威','https://eos.bitspace.no/','比特空间(BitSpace)是挪威领先的区块链技术公司，自2013年以来一直是丹尼尔·拉里默(Daniel Larimer，比特股创始人)愿景的有力支持者。在2017年纽约共识会议上，我们很荣幸地获邀参加袖子操作系统发布会.并从那时起就把重点放在了EOSIO上。2018年,我们举办了一系列活动、专题介绍、编程马拉松和视频来宣传袖子操作系统。此外，我们正在帮助多家公司过渡到袖子操作系统，积极建设社区并形成合作伙伴关系。以扩大国际规模。比特空间是一个成熟的区块链创新网络，在委托权益证明技术方面拥有丰富的知识和经验。通过咨询工作，拥有超过6个月的委托权益证明链，并成为比特股(BitShare)的区块生产商。我们还积极参与比特股和斯蒂姆(Steemit)社区，自2015年以来举办了比特股专题介绍以及2016年首次举办斯蒂姆编程马拉松。目前我们正在测试并运行节点名称为“比特空间”的袖子操作系统节点.我们将继续测试网络环境。我们对袖子操作系统实现主流去中心化应用的潜力感到非常兴奋，并且为袖子操作系统建立了比特空间区块生产者，以帮助确保比特空间建立在以下价值观和原则的基础上:个人主权和自主权，自愿联合以及财务和政治自由。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(24,'EOSCUBE袖子魔方','eoscube11111','http://static.eostoken.im/eosElector/100_0023.png','中国','http//eoscube.io','我们是中国深圳袖子魔方eoseube团队，目前拥有核心成员共20余人技术团队成员大部分来自于世界五百强通信公司华为，拥有多年的云服务器部署和研发经验。目前正在研究跨链技术和EOS侧链，开发EOS跨链账户、EOS区块链浏览器等基础设施。我们在节点部署和维护上拥有巨大的优势。技术团队都来自于华为云服务和运维的金牌团队，有服务全球75个国家120家运营商的经验和技术实力。我们是来自于中国“硅谷”深圳的超级节点。节点标识，\"\"eoseube\"\"，或者“EOSCUBE\"\"，请支持我们。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(25,'EOS Argentina','argentinaeos','http://static.eostoken.im/eosElector/100_0023.png','阿根廷','https://www.eosargentina.io/','EOS阿根廷，主要目标是通过在一个已经以“区块链友好”而闻名的国家推广使用EOS，并在南美其他地区效仿这一成功举措。推动区块链的采用，我们相信我们大陆迫切需要区块链透明度。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(26,'EOS. MediShares','ha4daobxg4ge','http://static.eostoken.im/eosElector/101_0024.png','新加坡','http://eos.medishares.org/','我们致力于将EOS带给大众。实现广泛采用的最佳方式是通过有用的，有趣的和精心设计的产品来展示EOS平台的最佳功能。还可以与EOS开发人员社区共享我们的工具。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(27,'EOS Dublin','eosdublinwow','http://static.eostoken.im/eosElector/102_0025.png','爱尔兰','https://www.eosdublin.com/','EOS Dublin是位于爱尔兰都柏林的超级节点候选人。我们徽标上的四个绿点代表了我们的核心价值观:独立，诚信，诚信和教育。我们相信EOS.IO代表了一个向更好的数字世界迈进的全球范式转变。EOS.IO不仅仅是一项技术进步，而是关于我们融合技术，社区和个人承诺时变得可能的事情。对我们来说，EOS块的生产者就像在美国宇航局扫地的看门人一样。我们在改变世界的风险中扮演一小部分角色。作为区块生产者，我们正在设计未来的基础。我们迫不及待想要了解基金会完成后的未来。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(28,'EOS Land','eosholdings1','http://static.eostoken.im/eosElector/无图片','加拿大','http://www.eosland.ca/','EOS Land (eosland.ca)是一个标志性社区，对于那些认为更美好的世界包括区块链和分散的生态系统的人来说。我们的总部位于美丽的温哥华，不列颠哥伦比亚省，加拿大，这座城市受区块链应用程序启发，如Kittycat。我们得到了加拿大主要大学IT学术界教授的支持，并得到当地DevOps专家的认可，EOS Land是在加拿大根据“不列颠哥伦比亚省合作法”运营的注册合法公司。我们在EOS Land的愿景是为现实世界的人们找到新的机会，我们相信我们正在通过引入区块链技术进入一个崭新的世界，就像克里斯托弗·哥伦布用他的发现改变了世界。我们的使命是以更低的成本建立更好的透明和负责任的标记化区块链社区生态系统.为所有人提供服务。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(29,'EOSMetal','eosmetaliobp','http://static.eostoken.im/eosElector/104_0027.png','冰岛','https://eosmetal.io/','EOSMetal建立在尽可能去中心化和独立的前提下，同时仍然提供强大的基础设施。我们通过“裸机”物理服务器设计来实现这一目标，并通过将我们的基础设施定位于多个地缘、政治边界.而进一步分散。我们完全自给自足，永远不会从Dapp,销售token或交换中获得任何收入。绝不会通过股息或任何其他会损害我们作为无偏见块的制作者的方法购买选票。我们都是EOS投资者，其次是区块生产者，我们尊重这个角色的需求，因为它直接影响我们作为EOS项目的投资者和支持者。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(30,'EOS JRR','jrrcryptoeos','http://static.eostoken.im/eosElector/104_0027.png','中国','http://www.jrrcrypto.io/','EOS JRR由JRR Crypto发起成立。致力于打造基于EOS的应用生态社区，并提供项目孵化、开发培训及社群运营等一系列服务。针对本次超级节点竞选.EOS JRR推出了Dapp专项孵化基金、孵化项目收益空投以及EOS大学等若干方案吸引EOS持有者投票。JRR Crypto是一家致力于打造全球首个区块链世界的“分布式投行”的机构下设投资银行、基金、私人银行等业务板块，于2016年在瑞士注册，曾投资过币安。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(31,'EOSMX',NULL,'http://static.eostoken.im/eosElector/105_0028.png','墨西哥','http://www.eosmx.com/','我们成立EOSMX只有一个目的一一支持，加强和领导EOS社区在拉丁美洲，从墨西哥开始。作为一个EOS区块生产者，我们将为EOS社区提供:一个透明的治理。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(32,'eosDAC','eosdacserver','http://static.eostoken.im/eosElector/106_0029.png','西印度群岛','https://eosdac.io/','eosDAC是一个通过区域链上的智能合约编码，分权自治的社区。通过EOS软件，使社区成为合作社的一种革命性方式成为可能。DAC由它的token持有者和董事会成员控制，他们投票运行它的运营。EOSD.10的愿景是区块生产者，应该开放给每个人贡献和受益。为了实现这一愿景，eosDAC将成为一个不断发展的去中心化自治社区  { DAC)。致力于成为为全球EOS社区提供服务的领先EOS.IO BlockProducers在此过程中，eosDAC将创建所需的工具和智能合约。它将与EOS社区分享这些信息，以帮助其他DAC在EOS.IO区块链上蓬勃发展。eosDAC习寄于2018年6月初在EOS区块链上作为DAC推出。直到eosDAC由其发射团队运行为止。发射团队的成员在过去6个月中，深入参与了EOS社区并在EOS社区测试网上开发了多个模块。目前，eosDACtoken存在于ETH区块链中，一旦它运行，这些token就会转移到EOS区块链上。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(33,'EOS BlockSmith','blocksmithio','http://static.eostoken.im/eosElector/107_0030.png','美国','http://eosblocksmith.io/','OS BIockSmith是第一家通过承诺不向任何EOS团队。投入任何投资来保证完全财务独立的节点生产者。消除了任何潜在的利益冲突，并确保我们永远不会激励任何人在财务上为我们投票。我们进一步保证。我们绝不会采取任何形式的外部投资或风险投资。我们100%自筹资金，并将继续如此。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(34,'EOSphere','eosphereiobp','http://static.eostoken.im/eosElector/108_0031.png','澳大利亚','https://www.eosphere.io/','专业诚信、透明度、承诺、合作和独立性，EOSphere拥有EOS所期望的所有特征。我们相信久经考验的经验。将激发对EOS利益相关方、Dapp开发商和EOS区块链业务的信心，为EOS.IO区块链的社区启动和持续运营以及治理提供支持，同时支持我们地区成为EOS社区的全球成员。虽然基于澳大利亚，我们的目标是服务于整个EOSphere !','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(35,'EOSeoul','eoseouldotio','http://static.eostoken.im/eosElector/109_0032.png','韩国','http://portal.eoseoul.io/','EOSeoul，韩国的节点候选团队。由韩国领先的网络游戏公司NEOPLY和NEOWIZ组成。NEOPLY是韩国NEOWIZ在2008年成立的第一个启动加速器。我们一直在全球范围内开发和管理PC在线和手机游戏，并且在过去的20年中一直是韩国这一领域的领导者之一。NEOWIZ的业务多年来一直在转型和扩张，紧跟互联网和行业发展的历史，不断寻找在这个领域上升的机会。我们相信下一个重大事件就是区块链协议。它将为这个时代带来新的范式。互联网上的用户可以复制和拥有数据，而不会产生任何费用。这种开放性让互联网能够爆炸并扩大。但是，这意味着数据的创建者/所有者没有得到适当的奖励，因为他/她有权获得。区块链协议识别数据的所有权。所有权属于贡献或创建数据的用户，奖励给那些对生态系统有积极贡献的人，从而为他们继续这样做提供动力。但是，还有很多问题需要解决。整个区块链生态系统需要更广泛的公众参与。开发者需要为这个生态系统提供更多有用的服务。从而创造一个良性循环。与此同时，区块生产商需要提供可靠的网络基础设施。我们相信EOS软件可能是我们所面临问题的最接近的解决方案。因此，我们愿意参与EOS生态系统的创新。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(36,'EOS42. io','eos42freedom','http://static.eostoken.im/eosElector/110_0033.png','未知','http://www.eos42.io/','EOS42作为英国伦敦的节点候选团队，我们的主要目标是通过高性能，可靠的网络支持服务和保护EOS网络，投资使其可扩展，保护珍贵的EOS token资源和价值，并赋予社区创建健康和可持续的生态系统的权利。我们将以诚实、正直和道德的态度，来做到这一点。同时保持政治和财务的全面独立。并提供高度的团队和财务透明。此外。我们将承诺一部分净利润用于区块链教育慈善事业。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(37,'CryptoLions','cryptolions1','http://static.eostoken.im/eosElector/111_0034.png','乌克兰','http://cryptolions.io','CryptoLions当它们结合在一个良好平衡的个体中时，会产生其他品质，如节制，适度，谨慎，纯洁和自我控制。尊重:EOS社区及其构成。能力:满足EOS区块生产者的技术、组织和法律要求。诚信:对他人和我们自己诚实做我们说我们会做的事。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(38,'EOS eco','eosecoeoseco','http://static.eostoken.im/eosElector/112_0035.png','中国','https://eoseco.com/','EOS eco致力于孵化和社区发展，为所有基于EOS平台的Dapp开发者提供服务。我们正在启动EOS账户，EOS浏览器，EOS侧链等项目。我们还提供EOSeco基金来支持启动EOS项目。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(39,'Blockgenic','blockgenicbp','http://static.eostoken.im/eosElector/113_0036.png','美国','http://blockgenic.io/','Blockgenic的总部位于西雅图，我们期待成为一个值得信赖和值得信赖的区块生产者。始终支持EOS生态系统，并为开发EOS做出贡献。我们非常重视安全性和透明度，并确保始终使我们的系统完成任务。我们还计划通过关于不同主题的各种聚会将EOS社区聚集在一起。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(40,'EOS WTZ','eoswtzeoswtz','http://static.eostoken.im/eosElector/114_0038.png','中国','http://www.eoswtz.com/','EOS WTZ于2017年11月成立。致力建立最有温度的区块链知识传播平台，通过普及区块链基础知识、输出原创内容、打造圈内互动社区、孵化区块链项目等形式。打造国内最有影响力、服务千万人的区块链社群。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(41,'TOKENIKA','tokenika4eos','http://static.eostoken.im/eosElector/115_0038.png','波兰','https://tokenika.io','Tokenika是位于华沙的东欧区块链公司，专门从事区块链和分布式账本技术的智囊团。我们的成员来自IT,编码、人工智能、法律和监管环境领域所组成，致力于为最有前途的区块链相关技术打下基础。自2012年以来，我们的团队成员一直参与区块链和DLT技术，自推出Bit Shares的第一代BitShares X以来，我们一直坚定支持DanLarimer (EOS的创建者)项目。目前，Tokenika成功部署EOS.io生态系统，这是区块链领域最耀眼的新星之一。作为节点竞选团队，我们非常清楚这项任务的规模和复杂性。我们的主要优势:我们的基础设施由GTG设立并进行了精心调整，而GTG是Steem表现最好的见证节点之一，所以Tokenika的服务器基础设施非常强大。我们使用Tier-3+数据中心。具有故障恢复保护功能的行业级和能保证99.9%的正常运行时间。并在ROGUE的见证下，为EOS社区测试网制作块。我们领导一个积极的EOS社区，并在Steem平台上进行热烈的跟踪，在那里我们就有关EOS.io的技术和实践主题进行教育和提供建议。我们还在为EOS生态系统开发几种解决方案，例如EOSFACTORY,EOSPROJECTS.现有EOS工具和扩展的列表以及EOS块浏览器。就Dapp而言，我们正在构建一个EOS的数字身份识别系统叫SYGNET。此外.为了促进EOS的大规模采用，还与另一位MMO游戏开发人员进行对话，还不时向波兰政府的数字化部门提供有关区块链相关事宜的建议.并及早洞察波兰和欧盟的区块链监管环境。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(42,'EOS TEA','eosteaeostea','http://static.eostoken.im/eosElector/116_0039.png','中国','https://eostea.io/','部署在香港节点将部署在香港，与大陆地区采用CN2网络高速连接，合理的避免了一些不可抗因素，使网络连通性和安全性达到一个接近完美的平衡.由社区驱动，没有内幕，没有阴谋，代表社区成员存在的一个节点.后期将会逐步完善社区的生态治理.利益最大化首先我们会尊重且履行EOS.IO官方及国际社区推行的宪法内容，同时我们会使用合理的方式使参与的社区成员利益最大化(欢迎广大社区成员给我们提议。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(43,'Wancloud','eoswancloud1','http://static.eostoken.im/eosElector/117_0040.png','中国','http://eos.wancloud.cloud','Wancloud is a neutral and innovative Blockchain-as-a-Service platform.We aim to leverage blockchain technology to effectively serve the needsof business and society. Combining blockchain with cloud computing,Wancloud provides community, enterprise users and developers witha suite of blockchain services. Featured by its convenient, flexible andprofessional service, Wancloud is committed to lowering the cost andthreshold of blockchain applications, to the reduction of repetitiveworkload for clients and to the promotion of blockchain technologyadoption.We have helped enterprise clients to deploy a wide range of blockchainpilots, such as financial record processing.As part of our globalization efforts, we have been deploying servers andnodes globally in regions like Hong Kong, the United States, Singapore,South Korea, and Japan. Each Node Center will be equipped withprofessional maintenance team to provide responsive and secureservice to suit the needs of blockchain op','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(44,'EOSStore','eosstorebest','http://static.eostoken.im/eosElector/118_0041.png','中国','http://www.eos.store/','Eos.Store团队是由一群Eos坚定信仰者、区块链技术发烧友组成，立志与EOS持有者一起共建一个自由、开放、平等、互信的高效去中心化社区。同时作为超级节点的竞选者之一，期望可以成为其守护者以及EOS生态繁荣的建设者，并致力构造一个开放、多元、平衡的应用生态。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(45,'eosONO','eosonoeosono','http://static.eostoken.im/eosElector/119_0042.png','中国','https://www.ono.chat/','我们是EOS ONO，一个免费的，分散的社交网络Dapp，它保护和尊重人类社交互动的多样性和多样性,并且即将在EOS平台上推出。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(46,'EOSYS','eosyskoreabp','http://static.eostoken.im/eosElector/120_0043.png','韩国','http://eosys.io','激活EOS生态系统','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(47,'EOSGeek',NULL,'http://static.eostoken.im/eosElector/121_0044.png','中国','https://www.eosgeek.io','EOSGeek团队，总部设在上海。我们将参加超级节点候选竞选。我们团队成员大部分来自互联网金融行业，在金融行业拥有丰富的经验，尤其是在产品，技术，运营维护和推广方面。该团队参与了多个区块链平台的构建，特别是在比特股项目中。目前，资产网winex.pro和证人节点被维护。Winex网关已经在BitShares官方网站上完美运行。我们是一群技术爱好者，他们认为区块链技术可以改变世界。我们的目标是利用EOS分布式网络推动更多实际应用，为广大公众服务，并致力于丰富EOS的生态。同时，我们也希望利用更多的技术为更多的团队提供支持.帮助更多的团队参与,共同构建EOS的生态。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(48,'EOSDR. io',NULL,'http://static.eostoken.im/eosElector/122_0045.png','多米尼加','https://eosdr.io','商业模式:透明的商业模式:不应该每个企业和政府都这样操作吗?其他英国石油公司候选人评论道，我们同意:“节点团队有透明的商业模式。但也有收入和成本基础。能够长期支持EOS社区。”净收入(收入一直接成本)将分配如下:60%-保留资本(基础设施，研究/能力可扩展性)25%-EOS发展基金(或帮助创业公司和有前途的EOS发展项目的类似项目)15%-社区扩展和支持我们保留与其他BP合作的权利，如果他们同意，可以使用其他BP团队作为上述任何奖励分配的代理，例如资金开发项目或共享组织EOS社区活动的费用。我们保留接受任何直接发送的捐款以解决EOSDR.io主页中的链接的权利。这些捐赠将每年或每季度向社区披露,并按上述分配。我们保留随时更改分配百分比的权利，因为仍然有许多关于运行BP节点所需的适当基础设施或任何突然的扩展需求的未知数。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(49,'EOS Asia','eosasia11111','http://static.eostoken.im/eosElector/123_0046.png','中国','https://www.eosasia.one/','我们是BP候选人EOS Asia，拥有世界级技术团队和经验丰富的区块链Dapp开发人员。作为亚洲最具国际和技术性的Block Produce候选人，我们汇集了来自中国，韩国，日本，香港，东南亚等地的EOS超级粉丝。EOS亚洲团队，核心价值观，当前进展，未来发展以及对社区的贡献。我们也将分享我们对EOS生态系统未来的理解和信念。EOS Asia的亮点:世界级的技术团队((Y Combinator，阿里云MVP，在各种技术会议上演讲)、大部分国际团队(中国，欧洲，美国和韩国)、致力于创建主流EOS DApps。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(50,'EOS DEtroit','eosiodetroit','http://static.eostoken.im/eosElector/124_0047.png','美国','https://eosdetroit.io/','EOS Detroit是一家位于密歇根州底特律的基础设施公司，专注于为EOS网络提供资源。这是一种共享的公共数据库及服务。在EOS主网启动后，我们的团队与全球其他BP候选人参与超级节点的竞选。这代表了一个新互联网的曙光，一个高度可用，值得信赖，审查抵制和分散的互联网。我们的团队努力获得EOStoken持有者的支持，他们将通过批准投票共同决定我们是否适合作为节点团队被网络雇佣。在收到他们的支持后，我们的收入将通过EOS网络直接获得。作为区块奖励，并反过来将通过扩展我们的基础架构和团队来引导EOS网络。除了作为网络基础架构的基石之外，我们的愿景是资助EOS.IO分布式应用程序开发，并通过教育，宣传和推广来发展EOS社区。我们的目标是通过我们为EOS网络和共同利益而努力创造相互价值。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(51,'EOS SOCAL','eossocal','http://static.eostoken.im/eosElector/125_0048.png','美国','http://eossocal.io','EOSoCaI是成立于2017年11月的团体，在南加州运营。主要支持和促进EOS.IO软件和社区的成功发布和可行性。我们的目标是为世界这个地区的EOS.IO分散式生态系统的整体成功做出贡献，帮助大众保护生命，自由和财产。建设一个更美好的世界。通过以下方式实现我们的目标:我们是超级节点竞选团队，为EOS区块链贡献可靠和可扩展的区块生产，并为社区贡献回报。现在正在做符合EOS项目的分配开发资源，包括支持，资金，设计和编程。EOS.IO的理念创造更美好的世界。通过提供的教育内容和计划的研讨会帮助教育社区，从初学者到高级用户使用EOS.IO开源软件。通过传播、支持社区和分发内容，帮助他人发现和采用EOS.IO平台。去中心化应用程序的开发者即将发布项目。我们希望能够改变十亿人的生活。正如布罗克皮尔斯所描述的那样。通过向EOS持有者提供“空投”token。独特价值主张:我们的设施开放，用于审计和实地考察。因为是一家自筹资金的机构，不需要向投资者汇报奖励。为援助权力下放提供独立贡献，目标是通过块生产者奖励和问责制来充分透明。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(52,'EOS Tribe','eostribeprod','http://static.eostoken.im/eosElector/126_0049.png','美国','https://eostribe.io/','EOS Tribe是一个专注于区块链基础设施和软件的杰出超级节点候选人。总部位于怀俄明州,在德克萨斯州达拉斯和犹他州盐湖城设有卫星办公室。我们的使命是为启动的EOS网络提供全球性的企业级基础设施，并且建立在共同的基础上.与我们在地球上的时间有目标的目标。我们深信分权制度的力量及其创造积极的社会和经济变革的潜力。我们是EOS节点竞选团队，希望赋予生态系统中为特定目标工作的使命Dapp和项目。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(53,'huobi global','eoshuobipool','http://static.eostoken.im/eosElector/127_0050.png','中国','https://eoshuobipool.com/','火币资产平台，致力于为用户提供方便安全的服务。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(54,'EOS Cafe','eoscafeblock','http://static.eostoken.im/eosElector/128_0051.png','加拿大','http://eoscafecalgary.com','EOS Cafe Calgary是加拿大艾伯塔省卡尔加里市的节点竞选的候选团队。它与EOS Cafe DAC合作，推动DAC成员协作Dapp的加速发展，并建立黑客空间(实体咖啡厅),让志同道合的EOS支持者和应用程序开发人员能够在这里共同协作。除了支持EOS Cafe DAC任务之外，EOS Cafe Calgary将继续支持本地聚会(EOS Calgary)和举办黑客马拉松，并作为我们任务的一部分，以支持EOS平台上更受欢迎的Dapps。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(55,'EOS Galaxy','eosgalaxybp1','http://static.eostoken.im/eosElector/129_0052.png','中国','http://www.eosgalaxy.io/','全球首家区块链媒体火星财经EOS Galaxy节点社区.','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(56,'EOS Canada','eoscanadacom','http://static.eostoken.im/eosElector/130_0053.png','加拿大','https://www.eoscanada.com/','EOS加拿大总部设在加拿大蒙特利尔，由知名加拿大金融企业支持。我们相信EOS.IO将资助系统，因此我们致力于为生态系统的发展做出贡献。我们一直积极参与运行和测试社区测试网络，创建BIOS BOOT序列，以及构建生态系统蓬勃发展所需的各种社区工具。我们的目标是成为公司，企业家和开发人员的中心，他们希望在EOS.IO之上构建基础架构和去中心化应用程序。由于我们深厚的技术背景和连接。我们希望利用我们的网络帮助为EOS.IO网络构建增值产品。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(57,'Bitfinex',NULL,'http://static.eostoken.im/eosElector/131_0054.png','英属维尔京群岛','https://www.bitfinex.com/eos','Bitfinex成立于2012年，是一家数字资产交易平台，为交易商和流动性提供商提供最先进的服务。自成立以来，我们一直致力于识别和提升协议级技术的能力，以满足基于区块链交易平台的大量法律和技术需求，并且我们相信我们可以为EOSIO贡献巨大的价值通过分享这个社区。我们的团队由100多位行业专家组成，其中包括高度灵活的开发团队，技术支持代理，法律专家和热情的行业倡导者。我们的基本目标是通过密集的社区发展，研究和协作，提高去中心化数字资产空间的开源性质。我们作为一个区块生产者的愿景是建立一个协作孵化器。将EOSIO的潜力与我们的经验相结合，将EOSIO的研究，开发和采用提升到另一个层次。我们的团队在此过程中获得了宝贵的行业经验，我们的目标是利用这一点丰富我们周围的团队一尽可能分享信息并提供指导。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(58,'EOS New York','eosnewyorkio','http://static.eostoken.im/eosElector/132_0055.png','美国','http://bp.eosnewyork.io','EOS New York，北美的主要制造商。我们是一个由经验丰富的专业人士组成的自费团队，完全与所有外部利益分离。我们的唯一目标是确保EOS网络的成功。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(59,'EOS Nation','eosnationftw','http://static.eostoken.im/eosElector/133_0056.png','加拿大','http://bp.eosnation.io','我们是一群敬业，热情和雄心勃勃的团队。致力于为保护EOS平台及其社区作出积极贡献。我们是社区的成员，我们坚信这项技术及其对我们社会和我们每个人的有利影响。我们的目标是代表你们每个人，给你最高层的发言权，我们将成为您在社区中的声音，我们将把您的项目放在心上。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(60,'EOS.CYBEX','eoscybexiobp','http://static.eostoken.im/eosElector/134_0057.png','中国','http://eos.cybex.io','EOS.C丫日EX是一支经验丰富的团队，致力于EOS项目孵化和社区发展。我们内部经过考虑和计划制定后，现在已经做好准备好运行EOS超级节点。EOS. C丫日EX旨在为所有基于EOS平台的Dapp开发人员提供无数的一站式服务，包括开发人员随时可以访问的测试链以及复杂的测试工具和服务。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(61,'OracleChain','oraclegogogo','http://static.eostoken.im/eosElector/135_0058.png','中国','https://oraclechain.io/','作为世界上第一个建立在EOS生态圈上的应用程序，Oracle Chain基于EOS区块链技术,为区块链提供实际数据和交叉链数据。我们还在区块链上实现了数据服务提供商和数据使用者之间的有效连接。作为EOS Oracle机器和其他基础架构的构建者，同时我们也是节点竞选候选者之一。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(62,'EOSBeijing','eosbeijingbp','http://static.eostoken.im/eosElector/136_0059.png','中国','http://www.eosbeijing.one/','EOS Beijing将与大学合作研究项目和各种区块链社区团体，支持EOS-related学术研究和促进EOS意识和采用的支持与合作交流、账户、cryptocurrency媒体，和其他元素的生态系统，我们将能够对EOS的成功做出了重大贡献。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(63,'eosio.sg','eosiosg11111','http://static.eostoken.im/eosElector/137_0060.png','新加坡','http://eosio.sg/','eosio.sg来自新加坡，是EOS全球超级节点竞选团队，致力于区块链底层技术研发，发展和构建安全、健康、活跃的EOS生态平台与应用。团队成员均来自世界级名校和互联网、金融、科研等行业的顶级公司，拥有扎实的产品设计、开发和运营经验以及扎实的EOS底层代码研究能力。团队在2018年4月成立以来。在技术理解上获得了迅速的积累，并积极为EOS社区生态的活跃作出贡献。团队在成立第一周，发布了eosio.sg Testnet并率先实现了一键接入等功能;团队成立第二周，在尚未提交节点申请的情况下，提前获邀成为正式的超级节点候选人，并分配在Group1;团队成立第三周。开始凭借对底层代码的深刻理解建立了技术博客，并获得了Daniel Larimer、Thomas Cox等官方人物的关注与互动。目前， eosio.sg正与其他优秀的团队一起，积极探索和开发新的EOS功能与应用。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(64,'EOSSw Eden','eosswedenorg','http://static.eostoken.im/eosElector/138_0061.png','瑞典','http://eossweden.se','来自瑞典的超级节点候选团队，具有高度的诚信和对科技的很好的理解','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(65,'EOS Rio','eosriobrazil','http://static.eostoken.im/eosElector/139_0062.png','\r\n排名','http://eosrio.io','EOS Rio致力于建立区块生产者基础设施，并在巴西和南美洲发展EOS社区。Rede Entropia领导EOS Rio表示，Rede Entropia是一家人工智能和区块链实验室，Venture Builder和Startup Accelerator。其目的是与人员和企业合作，创造积极影响社会的创新解决方案。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(66,'EOS the World LLC','1eostheworld','http://static.eostoken.im/eosElector/140_0063.png','波多黎各','http://eostheworld.io','我们是一个分散式的计算机科学、商业、市场营销和慈善人员团队，对EOS平台非常感兴趣。完全是自筹资金的(并且将永久保留)，没有任何外部利益冲突。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(67,'ONEROOT','onerootlleos','http://static.eostoken.im/eosElector/141_0064.png','中国','https://www.oneroot.io/en','ONEROOT成立于2017年，在众多共同建设者的支持下实现了雄心壮志迈出的第一步。在未来的日子里。我们将尽力履行您的信任和期望，并尽我们所能建立一个全新的经济体系。我们作为一个超级节点候选人的愿景是合作孵化器，将EOS作为共同建设者的一部分。ONEROOT将有助于在上海的封锁之家进行研究和宣传。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(68,'EOS Emerge','emergepoland','http://static.eostoken.im/eosElector/142_0065.png','中国','http://eosemerge.io','我们是一群来自波兰的强大的IT和区块链爱好者。他们拥有在私人公司  (贵金属，共同基金，保险)的背景和IT经验，在金融市场有14年的历史，对大多数金融工具出现在过去的十年中，表明我们有能力适应不断变化的业务环境。因此，我们非常兴奋并准备好利用我们的财务背景，IT知识和创新技能来服务EOS项目。我们是EOS生态系统的大爱好者，我们相信其核心基础，这使我们希望成为其早期采用阶段的积极参与者。利用我们以前的IT和财务经验，我们渴望您的加入社区，为构建EOS的dApps做出贡献，我们认为这可能是一个真正的突破!','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(69,'JEDA','jedaaaaaaaaa','http://static.eostoken.im/eosElector/143_0066.png','日本','http://www.eosjapan.org','JEDA是EOS开发者协会，支持日本EOS生态系统的开发。在EOS上建立一个生态系统。提出线上和线下活动，收集EOS开发者并开发有吸引力的DAPP并开发基于EOS的多种服务。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(70,'EOSREAL','eosrealbpcsg','http://static.eostoken.im/eosElector/144_0067.png','新加坡','http://www.eosreal.com','EOSREAL总部位于新加坡，拥有一支来自世界各地的3人团队，如新加坡和澳大利亚;从发达国家到巴基斯坦等新兴国家法律,大陆法和穆斯林法。在最初阶段。我有能力在同一阶段成立和监管机构。我们计划在巴基斯坦的伊斯兰堡和澳大利亚的墨尔本设立我们自己的托管服务器和本地的云服务器。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(71,'EOS India','eosindiaiobp','http://static.eostoken.im/eosElector/145_0068.png','印度','https://eosindia.io/','EOS India我们的团队汇集了来自各领域领先组织的技术、战略和业务。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(72,'EOSRaychain','eosraychains','http://static.eostoken.im/eosElector/146_0069.png','中国','http://www.eosraychain.com','','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(73,'EOS-NL','dutcheosxxxx','http://static.eostoken.im/eosElector/147_0070.png','荷兰','http://dutcheos.io','EOS-NL致力于阿姆斯特丹和全球，建立和支持蓬勃发展的EOS社区。我们计划如下执行此操作:1.技术在具有冗余的快速设施中运行最佳服务器EOS-NL的主要重点是通过阿姆斯特丹互联网交换((AMS-ix)主机托管尽可能提供CPU，内存和吞吐量方面的EOS网络，尽可能提高最大运行时间和最小延迟。EOS-NL目前拥有一台法兰克福AWS测试服务器，运行Dawn.3作为Jungle测试网中的Raven。在EOS社区测试网发布Dawn.3之后，EOS-NL将把测试环境转移到其带有24个CPU,  64 GB RAM和200 GBSSD,  1 GBps带宽和6ms延迟连接的裸机设备上。EOS-NL将很快发布进一步扩大计划。2.团队和组织作为BP，我们主要提供一个可靠的节点并促进社区的发展和参与。从这些主要目标来看非常简单组织结构出现。3.合作EOS -NL认为应该在BP团队之间分享知识，以便每个BP能够最有效地支持全球和当地的EOS社区。4.仁爱EOS -NL将为其运营的收入和成本提供充分的透明度。5.多样性EOS -NL努力代表荷兰参与全球EOS网络。并使用全球第二大互联网枢纽-阿姆斯特丹互联网交换中心，因为它是ISP。 EOS -NL将设在阿姆斯特丹,这个以其多样性和创业友好环境而闻名的城市。6.政治观点EOS -NL完全支持EOS的愿景，节点生产者主要是为了整个EOS社区而受益。作为一名BP,  EOS-NL认为它应该提供一个知识平台，并通过举力、见面会和会议为讨论提供空间。最终决定应该始终在于社区。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(74,'EOSGreen','eosgreeniobp','http://static.eostoken.im/eosElector/148_0071.png','欧盟','http://eosgreen.io/','EOS Green是一家受区块链技术和可再生能源相结合启发的公司。我们提供适当的设备以参与EOS块的生产，所有需要的能源最终将来自太阳能电池板，水电和生物燃料等可再生资源。未来，我们计划不仅为我们而且为成千上万的潜在客户产生和储存足够的能量。预计未来十年内，美国国家可再生能源市场将继续强劲增长。这将为我们实现成功的商业做出承诺。我们所做的一切，我们都相信挑战我们如何使用该技术的现状。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(75,'91 EOS',NULL,'http://static.eostoken.im/eosElector/149_0072.png','中国','http://www.91eos.io/','\"91 EOS是第一个接触块链技术的团队，我们的团队有很多关于石墨烯技术的经验，并且充满激情和强大的社区运营和节点维护。配备高效设施和危机管理的经验可以帮助我们给每个人更舒适的用户体验。我们的主要目标是传播EOS通过持续教育，知识和资源共享，社区参与以及项目创建带来的社会效益。我们将成为您在社区中的声音，我们将把您的项目放在心上。','2018-06-10 11:05:08','2018-06-10 11:05:08',0),
(76,'EOS Singapore','123singapore',NULL,NULL,'http://eos.vote',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(77,'EOS Authority','eosauthority',NULL,NULL,'http://eosauthority.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(78,'Bitfinex','bitfinexeos1',NULL,NULL,'http://www.bitfinex.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(79,'Liquid EOS','eosliquideos',NULL,NULL,'http://vote.liquideos.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(80,'cypherglasss','cypherglasss',NULL,NULL,'http://cypherglass.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(81,'shEOS','sheos21sheos',NULL,NULL,'http://sheos.org',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(82,'EOS NodeOne','eosnodeonebp',NULL,NULL,'http://www.eosnodeone.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(83,'Libertyblock','libertyblock',NULL,NULL,'http://libertyblock.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(84,'EOS Flytomars','eosflytomars',NULL,NULL,'http://www.bitmars.one',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(85,'EOS Union','eosunion1111',NULL,NULL,'http://eosunion.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(86,'GREY MASS','teamgreymass',NULL,NULL,'http://greymass.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(87,'Genereos','aus1genereos',NULL,NULL,'http://www.genereos.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(88,'eosAfrica','eosafricaone',NULL,NULL,'http://bp.eosio.africa',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(89,'ZB EOS','zbeosbp11111',NULL,NULL,'http://www.zbeos.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(90,'EOS NAIROBI','eosnairobike',NULL,NULL,'http://www.eosnairobi.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(91,'HK EOS','hkeoshkeosbp',NULL,NULL,'http://www.hkeos.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(92,'ATTICLAB','atticlabeosb',NULL,NULL,'http://atticlab.net',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(93,'UnlimitedEOS','unlimitedeos',NULL,NULL,'htttp://www.unlimitedeos.com',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(94,'ONEROOT','onerootlleos',NULL,NULL,'http://oneroot.io',NULL,'2018-06-15 14:14:01','2018-06-15 14:14:01',0),
(95,'SheLeaders','Sheleaders21',NULL,NULL,'http://www.sheleaders.one',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(96,'EOS90','eosninetiess',NULL,NULL,'http://www.eos90s.io',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(97,'Geosoneforbp','geosoneforbp',NULL,NULL,'http://www.geos.one',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(98,'EOSteaeostea','eosteaeostea',NULL,NULL,'http://eostea.io',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(99,'EOSAsia','eosasia11111',NULL,NULL,'http://www.eosasia.one',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(100,'EOSUnion','eosunion1111',NULL,NULL,'http://eosunion.io',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(101,'ORACLE CHAIN','oraclegogogo  ',NULL,NULL,'http://oraclechain.io',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(102,'EOS.LAWYER','eoslawyereos',NULL,NULL,'http://eos.lawyer',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(103,'EOS Singapore','123singapore ',NULL,NULL,'http://eos.vote',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(104,'HelloEOS','helloeoscnbp',NULL,NULL,'http://www.helloeos.com.cn',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0),
(105,'EOS Beijing ','eosbeijingbp',NULL,NULL,'http://www.eosbeijing.one',NULL,'2018-06-22 14:55:09','2018-06-22 14:55:09',0);

/*Table structure for table `eost_record` */

DROP TABLE IF EXISTS `eost_record`;

CREATE TABLE `eost_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `type` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `eost` double(255,4) NOT NULL DEFAULT '0.0000',
  `bid` int(11) DEFAULT NULL COMMENT '业务id',
  `eos_account` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `eost_record` */

/*Table structure for table `html_template` */

DROP TABLE IF EXISTS `html_template`;

CREATE TABLE `html_template` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `template` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `html_template` */

insert  into `html_template`(`id`,`template`) values 
(1,'<!DOCTYPE html>\r\n<html lang=&quot;en&quot;>\r\n <head>\r\n  <meta charset=&quot;UTF-8&quot; />\r\n  <meta http-equiv=&quot;X-UA-Compatible&quot; content=&quot;IE=edge&quot; />\r\n  <meta name=&quot;viewport&quot; content=&quot;width=device-width, initial-scale=1&quot; />\r\n  <style>\r\n    body\r\n      {\r\n	    margin:0;\r\n      }\r\n\r\n</style>\r\n </head>\r\n <body >\r\n  <div style=&quot;margin: 0; padding: 0;&quot;>\r\n   <p style=&quot;margin: 20px 30px 10px&quot;><span style=&quot;font-size: 20px;&quot;><strong>%s</strong></span><br /></p>\r\n   <p style=&quot;line-height:25px; margin: 0 30px 50px;&quot;>%s<br /></p>\r\n   <p style=&quot;margin: 20px 30px 10px&quot;>截至发稿时，EOS价值￥%s（来源：Gate.io）</p>\r\n   <p style=&quot;margin: 0; padding: 0;&quot;><a href=&quot;http://static.eostoken.im/invite/down.html&quot; target=&quot;_blank&quot;><img src=&quot;http://static.eostoken.im/xu/xinwen.png&quot; width=&quot;100%%&quot; /></a></p>\r\n  </div> \r\n </body>\r\n</html>\r\n');

/*Table structure for table `job_manager` */

DROP TABLE IF EXISTS `job_manager`;

CREATE TABLE `job_manager` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `job_name` varchar(64) DEFAULT NULL,
  `status` varchar(64) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `modifydate` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_jobname` (`job_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `job_manager` */

insert  into `job_manager`(`id`,`job_name`,`status`,`createdate`,`modifydate`) values 
(1,'removeRepeat','wait','2018-08-23 11:05:01','2018-08-23 11:05:22');

/*Table structure for table `news` */

DROP TABLE IF EXISTS `news`;

CREATE TABLE `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `content` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `url` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `tid` bigint(20) NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `source` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '',
  `html` mediumtext COLLATE utf8mb4_bin,
  `visable` int(10) NOT NULL DEFAULT '1',
  `otherid` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_otheridsource` (`otherid`,`source`),
  KEY `idx_modifydate` (`modifydate`),
  KEY `idx_titlesource` (`title`,`source`),
  KEY `idx_tidvisabledate` (`tid`,`visable`,`modifydate`),
  KEY `idx_content` (`content`(768))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `news` */

/*Table structure for table `news_statistics` */

DROP TABLE IF EXISTS `news_statistics`;

CREATE TABLE `news_statistics` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `nid` bigint(20) NOT NULL DEFAULT '0',
  `up` bigint(20) NOT NULL DEFAULT '0',
  `down` bigint(20) NOT NULL DEFAULT '0',
  `share` bigint(20) NOT NULL DEFAULT '0',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `view` bigint(20) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nid` (`nid`),
  KEY `idx_createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='资讯统计';

/*Data for the table `news_statistics` */

/*Table structure for table `news_type` */

DROP TABLE IF EXISTS `news_type`;

CREATE TABLE `news_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `url` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL,
  `type` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `news_type` */

insert  into `news_type`(`id`,`name`,`createdate`,`modifydate`,`url`,`type`) values 
(8,'最新资讯','2018-03-09 19:17:45','2018-04-02 14:52:52','666',0),
(9,'柚子百科','2018-03-11 17:51:12','2018-04-02 14:55:24','http://www.baidu.com',1),
(10,'EOS查','2018-03-29 16:05:44','2018-04-02 14:55:04','http://eos.icocha.com',0),
(11,'AAA','2018-04-02 14:50:47','2018-05-23 19:28:05','aaaa',0),
(12,'快讯','2018-04-02 14:55:39','2018-05-24 11:48:25',NULL,2),
(13,'广告','2018-04-02 14:55:39','2018-07-04 21:19:00','',3);

/*Table structure for table `pocket_asset` */

DROP TABLE IF EXISTS `pocket_asset`;

CREATE TABLE `pocket_asset` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL COMMENT '名称',
  `icon` varchar(200) DEFAULT 'http://static.eostoken.im/pocket_asset/asset_default.png' COMMENT '图标',
  `contract_account` varchar(100) NOT NULL COMMENT '合约账号',
  `detail` text COMMENT '详情',
  `create_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_name` (`name`),
  UNIQUE KEY `uk_contractacount` (`contract_account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `pocket_asset` */

/*Table structure for table `sysconf` */

DROP TABLE IF EXISTS `sysconf`;

CREATE TABLE `sysconf` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `desc` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `value` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=726 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `sysconf` */

insert  into `sysconf`(`id`,`name`,`desc`,`value`,`createdate`,`modifydate`) values 
(1,'share_reward','分享每次得分','6','2018-04-19 10:25:54','2018-04-19 10:25:54'),
(2,'up_reward','点赞每次得分','1','2018-04-18 20:46:45','2018-04-18 20:46:45'),
(3,'down_reward','点赞每次踩得分','1','2018-04-18 20:47:45','2018-04-18 20:47:45'),
(4,'acount_send','发送每次得分','5','2018-04-18 20:49:02','2018-04-18 20:49:02'),
(722,'signin_reward','签到送分','1','2018-04-18 20:56:39','2018-04-18 20:56:39'),
(723,'signin_continuity_reward','连续签到增加分数（每天增加多少）','1','2018-04-18 20:59:12','2018-04-18 20:59:12'),
(724,'signin_continuity_max','最多连续多少天持续增加','7','2018-04-18 20:59:12','2018-04-18 20:59:12'),
(725,'create_eos_account_need_point','创建EOS账号需要最低积分','50','2018-07-14 23:34:46','2018-07-14 23:34:46');

/*Table structure for table `upgrade` */

DROP TABLE IF EXISTS `upgrade`;

CREATE TABLE `upgrade` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `url` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `os` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '5',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `intr` varchar(1024) COLLATE utf8mb4_bin DEFAULT NULL,
  `must` int(255) DEFAULT '0' COMMENT '1强制更新',
  PRIMARY KEY (`id`),
  UNIQUE KEY `os` (`os`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `upgrade` */

insert  into `upgrade`(`id`,`version`,`url`,`os`,`createdate`,`modifydate`,`intr`,`must`) values 
(1,'1111','22222','android','2018-03-07 10:59:02','2018-03-24 03:19:05','3333',0),
(2,'5555','http://www.baidu.com','ios','2018-03-15 19:11:54','2018-03-27 11:45:54','5555',0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `password` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `salt` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '随机码',
  `nickname` varchar(255) COLLATE utf8mb4_bin NOT NULL COMMENT '昵称',
  `photo` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像',
  `eost` double(20,4) NOT NULL DEFAULT '0.0000' COMMENT 'EOS数量',
  `status` int(11) DEFAULT '0' COMMENT '状态',
  `point` int(11) NOT NULL DEFAULT '0' COMMENT '积分',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`),
  KEY `uname` (`username`) USING BTREE,
  KEY `idx_nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `user` */

/*Table structure for table `user_ext` */

DROP TABLE IF EXISTS `user_ext`;

CREATE TABLE `user_ext` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `uid` bigint(255) NOT NULL COMMENT '用户UID',
  `eos_account` varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'EOS账户',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `modifydate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uid` (`uid`),
  UNIQUE KEY `uk_eos_account` (`eos_account`),
  KEY `idx_createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户扩展信息';

/*Data for the table `user_ext` */

/*Table structure for table `user_invite` */

DROP TABLE IF EXISTS `user_invite`;

CREATE TABLE `user_invite` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `uid` bigint(255) NOT NULL,
  `code` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `max_use` int(255) NOT NULL DEFAULT '10' COMMENT '最多使用次数',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `used` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '0' COMMENT '已经使用次数',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`) USING BTREE,
  UNIQUE KEY `uid` (`uid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `user_invite` */

/*Table structure for table `user_invite_record` */

DROP TABLE IF EXISTS `user_invite_record`;

CREATE TABLE `user_invite_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `iid` varchar(255) COLLATE utf8mb4_bin NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `user_invite_record` */

/*Table structure for table `user_receive_eos_record` */

DROP TABLE IF EXISTS `user_receive_eos_record`;

CREATE TABLE `user_receive_eos_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `uid` bigint(20) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `eos_account` varchar(50) DEFAULT NULL,
  `status` bigint(20) DEFAULT NULL,
  `createdata` datetime DEFAULT CURRENT_TIMESTAMP,
  `updatedate` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `user_receive_eos_record` */

/*Table structure for table `user_signin_log` */

DROP TABLE IF EXISTS `user_signin_log`;

CREATE TABLE `user_signin_log` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint(20) NOT NULL COMMENT 'UID',
  `nickname` varchar(255) NOT NULL COMMENT '昵称',
  `signdate` date NOT NULL COMMENT '签到日期',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_uidsigndate` (`uid`,`signdate`),
  KEY `idx_uid` (`uid`),
  KEY `idx_createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户签到记录';

/*Data for the table `user_signin_log` */

/*Table structure for table `userpoint` */

DROP TABLE IF EXISTS `userpoint`;

CREATE TABLE `userpoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` int(11) NOT NULL,
  `signin` int(11) NOT NULL,
  `share` int(11) NOT NULL,
  `interact` int(11) NOT NULL,
  `store` int(11) NOT NULL,
  `turnin` int(11) NOT NULL,
  `turnout` int(11) NOT NULL,
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `nickname` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `userpoint` */

/*Table structure for table `userpoint_record` */

DROP TABLE IF EXISTS `userpoint_record`;

CREATE TABLE `userpoint_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uid` varchar(255) NOT NULL,
  `nickname` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `point` int(11) NOT NULL DEFAULT '0',
  `createdate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modifydate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_uid` (`uid`),
  KEY `idx_uidtypecreatedate` (`uid`,`type`,`createdate`),
  KEY `idx_createdate` (`createdate`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `userpoint_record` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

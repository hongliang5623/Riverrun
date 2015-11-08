/*
MySQL Data Transfer
Source Host: 122.5.51.234
Source Database: homegate
Target Host: 122.5.51.234
Target Database: homegate
Date: 2015/1/5 14:19:23
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for multigw_filter_rules
-- ----------------------------
DROP TABLE IF EXISTS `multigw_filter_rules`;
CREATE TABLE `multigw_filter_rules` (
  `rule_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '规则id',
  `rule` varchar(20000) NOT NULL DEFAULT '' COMMENT '规则',
  `rule_name` varchar(20) NOT NULL COMMENT '规则名字',
  `status` int(2) NOT NULL,
  `update_time` datetime DEFAULT NULL,
  `update_flag` int(2) NOT NULL COMMENT '0 不需要下更新，1需要下更新',
  PRIMARY KEY (`rule_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3295 DEFAULT CHARSET=utf8 COMMENT='规则表';

-- ----------------------------
-- Table structure for singlegw_rules_iphost
-- ----------------------------
DROP TABLE IF EXISTS `singlegw_rules_iphost`;
CREATE TABLE `singlegw_rules_iphost` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `simid` varchar(32) NOT NULL,
  `ip` varchar(32) DEFAULT NULL,
  `host` varchar(255) DEFAULT NULL,
  `proto` varchar(5) DEFAULT '0' COMMENT '1是post;2是get;3是两个都有',
  `status` int(1) NOT NULL COMMENT '0 失效, 1有效',
  `insert_time` datetime DEFAULT NULL,
  `macid` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10941 DEFAULT CHARSET=utf8 COMMENT='单机iphost规则';

-- ----------------------------
-- Table structure for singlegw_rules_keyword
-- ----------------------------
DROP TABLE IF EXISTS `singlegw_rules_keyword`;
CREATE TABLE `singlegw_rules_keyword` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `simid` varchar(32) NOT NULL,
  `macid` varchar(60) DEFAULT NULL,
  `rule` varchar(20000) NOT NULL DEFAULT '' COMMENT '关键字',
  `rule_name` varchar(20) NOT NULL,
  `status` int(1) NOT NULL COMMENT '0 失效, 1有效',
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10895 DEFAULT CHARSET=utf8 COMMENT='单机关键字规则';

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `multigw_filter_rules` VALUES ('3287', '@text(张艺谋 周星驰)', '导演', '1', '2014-10-17 16:53:44', '0');
INSERT INTO `multigw_filter_rules` VALUES ('3290', '@text(梁朝伟 刘德华)', '男演员', '1', '2014-10-17 16:55:50', '0');
INSERT INTO `multigw_filter_rules` VALUES ('3292', '@text(章子怡 巩俐)', '女演员', '1', '2014-10-17 17:15:39', '0');
INSERT INTO `singlegw_rules_iphost` VALUES ('10916', 'vtk7mil2a2g6crjztofsr4ay3', '1.1.1.1', 'wqwq', null, '1', '2014-10-14 14:09:21', null);
INSERT INTO `singlegw_rules_iphost` VALUES ('10925', 'vtk7mil2a2g6crjztofsr4ay2', '', '58.com', '3', '1', '2014-10-21 17:46:21', null);
INSERT INTO `singlegw_rules_iphost` VALUES ('10929', 'vtk7mil2a2g6crjztofsr4a22', '10.1.1.1', 'post.com.cn', '1', '1', '2014-10-21 17:59:50', null);
INSERT INTO `singlegw_rules_iphost` VALUES ('10939', 'vtk7mil2a2g6crjztofsr4a22', '', '', '0', '1', '2014-10-21 19:44:18', 'null');
INSERT INTO `singlegw_rules_iphost` VALUES ('10940', 'vtk7mil2a2g6crjztofsr4a22', '', '', '0', '1', '2014-10-21 19:47:10', '60:DE:44:73:E4:94');
INSERT INTO `singlegw_rules_keyword` VALUES ('10889', 'vtk7mil2a2g6crjztofsr4ay3', '', '@text(饭店 放入) @text(哈哈|好了)', '测试123', '1', '2014-10-16 16:57:26');
INSERT INTO `singlegw_rules_keyword` VALUES ('10892', 'vtk7mil2a2g6crjztofsr4ay2', null, '@text(张艺谋| 章子怡)', '演员脏', '1', '2014-10-21 17:46:21');
INSERT INTO `singlegw_rules_keyword` VALUES ('10893', 'vtk7mil2a2g6crjztofsr4a22', null, '@text(汪峰|那英)', '测试', '1', '2014-10-21 17:59:50');

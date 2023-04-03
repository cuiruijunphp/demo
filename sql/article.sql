/*
 Navicat Premium Data Transfer

 Source Server         : localhost1
 Source Server Type    : MySQL
 Source Server Version : 80012
 Source Host           : 127.0.0.1:3306
 Source Schema         : jtest

 Target Server Type    : MySQL
 Target Server Version : 80012
 File Encoding         : 65001

 Date: 03/04/2023 14:31:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) NOT NULL,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '内容',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `sort` int(10) NULL DEFAULT NULL,
  `type` tinyint(2) NOT NULL DEFAULT 1,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80020
 Source Host           : localhost:3306
 Source Schema         : besure

 Target Server Type    : MySQL
 Target Server Version : 80020
 File Encoding         : 65001

 Date: 14/12/2020 21:57:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_cs
-- ----------------------------
DROP TABLE IF EXISTS `t_cs`;
CREATE TABLE `t_cs`
(
    `idP`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `auCS` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `cskP` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`idP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_cs
-- ----------------------------

-- ----------------------------
-- Table structure for t_ehr
-- ----------------------------
DROP TABLE IF EXISTS `t_ehr`;
CREATE TABLE `t_ehr`
(
    `idP`          varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `stage`        int(0)                                                         NOT NULL,
    `c_rou_y_rou`  varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `ck_rou_y_rou` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `PB_l`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `Bl_l`         varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `sigma_PB_l`   varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`idP`, `stage`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ehr
-- ----------------------------

-- ----------------------------
-- Table structure for t_h
-- ----------------------------
DROP TABLE IF EXISTS `t_h`;
CREATE TABLE `t_h`
(
    `idP` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `auH` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    PRIMARY KEY (`idP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_h
-- ----------------------------

-- ----------------------------
-- Table structure for t_ks
-- ----------------------------
DROP TABLE IF EXISTS `t_ks`;
CREATE TABLE `t_ks`
(
    `idP`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci   NOT NULL,
    `pwP_star` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci  NOT NULL,
    `au`       varchar(10240) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
    `eP`       int(0)                                                          NULL DEFAULT NULL,
    PRIMARY KEY (`idP`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_ks
-- ----------------------------

-- ----------------------------
-- Table structure for t_params
-- ----------------------------
DROP TABLE IF EXISTS `t_params`;
CREATE TABLE `t_params`
(
    `P`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `P_pub` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `s`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `k`     varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_params
-- ----------------------------
INSERT INTO `t_params`
VALUES ('+¥7Ì&xï­m	ºÎ¢Unì2û}A!=®à{ä¥ÎTGu¬\"hS)ÍR2Ý;Ù¬×(	,\r&gñoÉXj«Ë?&é°Væ8ë®ì«\ZÉ2¯Ç8ò>g\\;ýß&CoJ\\úS~æÄwñª',
        '01AÂÉÞÃ¡ÂKó¼^ç]AEÐÑ¯Åó1Ãá@>È¢åò¡2Ù;5ÜÖr§@:ä-p¶ØÑáôÚoð§à?ã¶!âù¼\r¢/\nÙ1ðJy%g®Ú+ýÇqL± *1ùÃ÷ØÂbpjºaei¡¹¡QAyÌ',
        'GlDÁÞ\\±Þ_=	ñ`¶%3¨÷§HÍê«K;îjgÃTÚ1	WÈÏ%-[2\"iÏædIj+',
        'GlDÁÞ\\±Þ_=	ñ`¶%3¨÷§HÍê«K;îjgÃTÚ1	WÈÏ%-[2\"iÏædIj+');

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `uid`      int(0)                                                  NOT NULL AUTO_INCREMENT,
    `uname`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
    PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 156
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

# CREATE DATABASE IF NOT EXISTS `apistream_database`;
# drop database if exists `mybatis_plus`;
# DROP TABLE IF EXISTS `user`;
#
# CREATE TABLE `apistream_database`.`user` (
#                         `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
#                         `name` VARCHAR(30) DEFAULT NULL COMMENT '姓名',
#                         `age` INT DEFAULT NULL COMMENT '年龄',
#                         `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
#                         PRIMARY KEY (`id`)
# ) ENGINE=INNODB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
#
#
# INSERT  INTO `apistream_database`.`user`(`id`,`name`,`age`,`email`) VALUES
#                                                    (1,'小李',18,'admin1@baomidou.com'),
#                                                    (2,'小陈',20,'admin2@baomidou.com'),
#                                                    (3,'小徐',28,'admin3@baomidou.com'),
#                                                    (4,'小贾',21,'admin4@baomidou.com'),
#                                                    (5,'小张',24,'admin5@baomidou.com');
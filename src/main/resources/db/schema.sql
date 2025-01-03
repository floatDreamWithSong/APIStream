# DROP TABLE IF EXISTS `apistream-module-data`;
# DROP TABLE IF EXISTS `api_stream_projects`;
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
create database if not exists `apistream_database`;
use `apistream_database`;
CREATE TABLE if not exists `apistream-module-data` (
                                                       `id` CHAR(36) NOT NULL, -- UUID 通常以 36 个字符（包括 4 个短横线）的字符串形式存储
                                                       `is_disabled` TINYINT(1) DEFAULT 0, -- 布尔值在 MySQL 中可以用 TINYINT(1) 表示
                                                       `total_call_times` BIGINT DEFAULT 0, -- 调用次数，使用 BIGINT 类型
                                                       `avg_runtime` BIGINT DEFAULT 0, -- 平均运行时间，使用 INT 类型
                                                       `max_runtime` BIGINT DEFAULT 0, -- 最大运行时间，使用 INT 类型
                                                       `min_runtime` BIGINT DEFAULT 0, -- 最小运行时间，使用 INT 类型
                                                       `project_id` CHAR(36) NOT NULL,
                                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE if not exists `api_stream_projects` (
                                                     `project_name` VARCHAR(255) NOT NULL, -- 假设项目名称最大长度为 255 个字符
                                                     `project_id` CHAR(36) NOT NULL,
                                                     PRIMARY KEY (`project_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
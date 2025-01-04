CREATE TABLE if not exists `apistream-module-data` (
                                                       `id` CHAR(36) NOT NULL, -- UUID 通常以 36 个字符（包括 4 个短横线）的字符串形式存储
                                                       `is_disabled` TINYINT(1) DEFAULT 0, -- 布尔值在 MySQL 中可以用 TINYINT(1) 表示
                                                       `total_call_times` BIGINT DEFAULT 0, -- 调用次数，使用 BIGINT 类型
                                                       `avg_runtime` BIGINT DEFAULT 0, -- 平均运行时间，使用 INT 类型
                                                       `max_runtime` BIGINT DEFAULT 0, -- 最大运行时间，使用 INT 类型
                                                       `min_runtime` BIGINT DEFAULT 0, -- 最小运行时间，使用 INT 类型
                                                       `project_id` CHAR(36) NOT NULL,
                                                       `module_path` VARCHAR(255) NOT NULL, -- 假设模块名称最大长度为 255 个字符
                                                       `error_count` BIGINT DEFAULT 0, -- 假设方法名称最大长度为 255 个字符
                                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE if not exists `api_stream_projects` (
                                                     `project_name` VARCHAR(255) NOT NULL, -- 假设项目名称最大长度为 255 个字符
                                                     `project_id` CHAR(36) NOT NULL,
                                                     PRIMARY KEY (`project_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
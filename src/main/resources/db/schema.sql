CREATE TABLE `module_detail` (
    `id` CHAR(36) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `path` VARCHAR(255) NOT NULL,
    `project_name` VARCHAR(255) NOT NULL,
    FOREIGN KEY (`project_name`) REFERENCES `api_stream_projects`(`project_name`),
    PRIMARY KEY (`id`)
);

CREATE TABLE `api_endpoints` (
    `id` CHAR(36) NOT NULL,
    `module_id` CHAR(36) NOT NULL,
    `path` VARCHAR(255) NOT NULL,
    `method` VARCHAR(10) NOT NULL,
    `description` TEXT,
    FOREIGN KEY (`module_id`) REFERENCES `module_detail`(`id`),
    PRIMARY KEY (`id`)
);
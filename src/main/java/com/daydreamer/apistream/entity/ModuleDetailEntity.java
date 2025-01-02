package com.daydreamer.apistream.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("module_detail")
public class ModuleDetailEntity {
    @TableId
    private String id;
    private String name;
    private String description;
    private String path;
    private String projectName;
}

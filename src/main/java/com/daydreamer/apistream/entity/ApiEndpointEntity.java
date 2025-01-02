package com.daydreamer.apistream.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("api_endpoints")
public class ApiEndpointEntity {
    @TableId
    private String id;
    private String moduleId;
    private String path;
    private String method;
    private String description;
}
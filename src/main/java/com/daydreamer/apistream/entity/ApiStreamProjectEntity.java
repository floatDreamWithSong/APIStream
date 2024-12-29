package com.daydreamer.apistream.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.UUID;

@Data
@TableName("api_stream_projects")
public class ApiStreamProjectEntity {
    @TableId
    private String projectName;
}

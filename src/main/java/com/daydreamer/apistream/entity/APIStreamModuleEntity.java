package com.daydreamer.apistream.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.UUID;

@Data
@TableName("`apistream-module-data`")
public class APIStreamModuleEntity {
    @TableId
    private String id;
    private boolean isDisabled;
}

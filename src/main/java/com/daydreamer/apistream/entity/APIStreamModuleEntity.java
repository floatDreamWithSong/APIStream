package com.daydreamer.apistream.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("`apistream-module-data`")
public class APIStreamModuleEntity {
    @TableId
    private String id;
    private boolean isDisabled;
    private long totalCallTimes;
    private int avgRuntime;
    private int maxRuntime;
    private int minRuntime;
}

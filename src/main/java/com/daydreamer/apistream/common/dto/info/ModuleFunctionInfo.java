package com.daydreamer.apistream.common.dto.info;

import lombok.AllArgsConstructor;

import java.util.UUID;

@AllArgsConstructor
public class ModuleFunctionInfo {
    public String function_name;
    public UUID function_id;
    public String function_content;
    public UsageInfo function_usage;
}

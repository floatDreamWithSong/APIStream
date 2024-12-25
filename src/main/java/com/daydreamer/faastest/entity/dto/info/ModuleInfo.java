package com.daydreamer.faastest.entity.dto.info;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.UUID;

@AllArgsConstructor
public class ModuleInfo {
    public String module_name;
    public UUID module_id;
    public ArrayList<ModuleFunctionInfo> module_functions;
    public UsageInfo module_usage;
}

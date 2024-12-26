package com.daydreamer.faastest.entity.dto.receive.sdk;

import com.daydreamer.faastest.entity.dto.common.Options;

import java.util.ArrayList;

public class AddModuleServiceSDKJsonEntity {
    public String path;
    public String initCode;
    public ArrayList<ModuleServiceFunction> functions;
    public Options options;
}

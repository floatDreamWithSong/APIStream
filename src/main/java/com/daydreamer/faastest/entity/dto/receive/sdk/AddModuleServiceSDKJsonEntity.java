package com.daydreamer.faastest.entity.dto.receive.sdk;

import java.util.ArrayList;

public class AddModuleServiceSDKJsonEntity {
    public String path;
    public String initCode;
    public Integer MaxConcurrency;
    public ArrayList<ModuleServiceFunction> functions;
}

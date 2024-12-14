package com.daydreamer.faastest.entity.dto.manage.receive;

import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.dto.manage.common.Options;

import java.util.ArrayList;

public class AddFunctionServiceJsonEntity {
    public String name;
    public String code;
    public ArrayList<ServiceArgument> args;
    public String method;
    public String path;
    public Options options;
}

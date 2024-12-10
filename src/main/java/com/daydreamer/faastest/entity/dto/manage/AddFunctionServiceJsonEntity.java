package com.daydreamer.faastest.entity.dto.manage;

import com.daydreamer.faastest.entity.ServiceArgument;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
public class AddFunctionServiceJsonEntity {
    public String name;
    public String code;
    public ArrayList<ServiceArgument> args;
    public String method;
    public String path;
    public Options options;
    public static class Options {
        public Integer maxCps;
    }
}

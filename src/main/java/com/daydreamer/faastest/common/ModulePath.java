package com.daydreamer.faastest.common;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ModulePath {
    public static ResolvedPath resolvePath(String path){
        System.out.println(path);
        String modulePath;
        String functionName;
        String projectName = path.substring(1, path.indexOf("/",1));
        int lastIndex = path.lastIndexOf("::");
        if(lastIndex == -1){
            modulePath = path.substring(projectName.length()+1);
            functionName = "";
        }else {
            modulePath = path.substring(projectName.length()+1, lastIndex);
            functionName = path.substring(lastIndex+2);
        }
        return new ResolvedPath(modulePath, functionName,projectName);
    }
}

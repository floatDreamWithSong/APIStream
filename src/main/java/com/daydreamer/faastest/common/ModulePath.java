package com.daydreamer.faastest.common;

public class ModulePath {
    public static ResolvedPath resolvePath(String path){
        String modulePath;
        String functionName;
        int lastIndex = path.lastIndexOf("::");
        modulePath = path.substring(0, lastIndex);
        functionName = path.substring(lastIndex+2);
        return new ResolvedPath(modulePath, functionName);
    }
}

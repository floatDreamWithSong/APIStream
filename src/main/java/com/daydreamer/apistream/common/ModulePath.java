package com.daydreamer.apistream.common;

import com.daydreamer.apistream.common.modules.ResolvedPath;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

@Slf4j
public class ModulePath {

    public static boolean removeLog(String _path){
        Path path = Paths.get("logs/"+_path);
        try {
            if(!path.toFile().exists()){
                return true;
            }
            if(path.toFile().isFile()){
                path.toFile().delete();
                return true;
            }
            deleteDirectory(path);
        } catch (IOException e) {
            log.error("removeLog error", e.getMessage());
            return false;
        }
        return true;
    }

    public static ResolvedPath resolvePath(String path){
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

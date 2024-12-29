package com.daydreamer.apistream;

import com.daydreamer.apistream.configuration.SDKConfig;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.daydreamer.apistream.mapper")
public class ApiStreamApplication {
    private APIStreamModuleMapper apiStreamModuleMapper;
    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        this.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    public static void main(String[] args) {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("--key") && i+1<args.length) {
                SDKConfig.sdkToken = args[++i];
            }
        }
        SpringApplication.run(ApiStreamApplication.class, args);

    }
}

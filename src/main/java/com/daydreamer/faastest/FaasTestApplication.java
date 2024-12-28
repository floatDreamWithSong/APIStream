package com.daydreamer.faastest;

import com.daydreamer.faastest.common.dto.receive.SDKConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.daydreamer.faastest.mapper")
public class FaasTestApplication {
    public static void main(String[] args) {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        for(int i=0;i<args.length;i++) {
            if(args[i].equals("--key") && i+1<args.length) {
                SDKConfig.sdkToken = args[++i];
            }
        }
        SpringApplication.run(FaasTestApplication.class, args);

    }
}

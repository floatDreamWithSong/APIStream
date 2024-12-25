package com.daydreamer.faastest;

import com.daydreamer.faastest.entity.SDKConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class FaasTestApplication {
    public static void main(String[] args) {
        System.setProperty("polyglot.engine.WarnInterpreterOnly", "false");
        for(int i=0;i<args.length;i++) {
            System.out.println(args[i]);
            if(args[i].equals("--key") && i+1<args.length) {
                SDKConfig.sdkToken = args[++i];
            }
        }
        SpringApplication.run(FaasTestApplication.class, args);

    }
}

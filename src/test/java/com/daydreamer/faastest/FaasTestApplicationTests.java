package com.daydreamer.faastest;

import org.graalvm.polyglot.Source;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;


@SpringBootTest
class FaasTestApplicationTests {

    @Test
    void tryJsTask() throws IOException {

        String currentDir = System.getProperty("user.dir");
        System.out.println("当前工作目录是: " + currentDir);

        // 也可以使用File类来获取当前目录
        File currentDirFile = new File(".");
        System.out.println("当前工作目录是: " + currentDirFile.getAbsolutePath());
    }

}

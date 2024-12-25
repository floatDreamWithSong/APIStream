package com.daydreamer.faastest;


import com.daydreamer.faastest.entity.dto.common.Options;
import com.daydreamer.faastest.entity.dto.response.UniResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    void tryUni(){
        UniResponse a = new UniResponse(1,"1");
        UniResponse<Options> b = new UniResponse<Options>(1,"2", new Options(1));
        System.out.println(a);
        System.out.println(b);
    }


}

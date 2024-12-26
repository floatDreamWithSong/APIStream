package com.daydreamer.faastest;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.common.ModulePath;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;


@SpringBootTest
class FaasTestApplicationTests {

    @Test
    void tryJsTask() throws IOException {
        System.out.print(JsonProcessor.gson.toJson(ModulePath.resolvePath("/project/modu/test/test::testfn")));

    }
}

package com.daydreamer.apistream;

import com.daydreamer.apistream.common.JsonProcessor;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.entity.User;
import com.daydreamer.apistream.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;


@SpringBootTest
class ApiStreamApplicationTests {

    @Test
    void tryJsTask() throws IOException {
        System.out.print(JsonProcessor.gson.toJson(ModulePath.resolvePath("/project/modu/test/test::testfn")));
    }

}

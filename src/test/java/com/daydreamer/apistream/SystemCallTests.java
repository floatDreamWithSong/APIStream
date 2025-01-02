package com.daydreamer.apistream;

import com.daydreamer.apistream.common.systemcall.SystemSQLRunner;
import lombok.extern.slf4j.Slf4j;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

@Slf4j
@SpringBootTest
public class SystemCallTests {

    @Test
    public void testSystemCall() {

    }

    @Test
    public void test() {
        log.info("test start");
        try (Context context = Context.newBuilder().allowAllAccess(true).build()) {
            // 评估JS代码
            context.eval("js", "print('Hello from JavaScript!');");
            // 调用Java方法
            context.getBindings("js").putMember("systemCall", new SystemSQLRunner());
//            context.eval("js", "console.log(systemCall.execSql('create table test (id int, name varchar(255))'))");
            context.eval("js", "console.log(systemCall.execSql('insert into test (id, name) values (1, \"test\")'))");
            context.eval("js", "console.log(systemCall.select('select * from test'))");
        }
        log.info("test end");
    }
}

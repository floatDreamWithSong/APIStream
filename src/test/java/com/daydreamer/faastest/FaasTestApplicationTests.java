package com.daydreamer.faastest;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.common.ModulePath;
import com.daydreamer.faastest.common.enums.SQLType;
import com.daydreamer.faastest.entity.User;
import com.daydreamer.faastest.mapper.UserMapper;
import com.daydreamer.faastest.systemcall.SystemSQLRunner;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.IOException;
import java.util.List;


@SpringBootTest
class FaasTestApplicationTests {

    @Test
    void tryJsTask() throws IOException {
        System.out.print(JsonProcessor.gson.toJson(ModulePath.resolvePath("/project/modu/test/test::testfn")));

    }
    @Autowired
    private UserMapper userMapper;

    @Test
    public void testSelect() {
        System.out.println(("----- selectAll method test ------"));
        List<User> userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
//        User user = new User();
//        user.setName("张三goubawanyi");
//        user.setAge(18);
//        user.setEmail("123@qq.com");
//        userMapper.insert(user);
        userList = userMapper.selectList(null);
        userList.forEach(System.out::println);
    }

    @Test
    public void testInsert() {
        String res = SystemSQLRunner.dropDatabase("faas_test");
        System.out.println(res);
    }
}

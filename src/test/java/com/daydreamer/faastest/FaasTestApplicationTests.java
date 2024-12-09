package com.daydreamer.faastest;

import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
class FaasTestApplicationTests {

    @Test
    void tryJsTask() {
        ArrayList<ServiceArgument> serviceArguments = new ArrayList<>();
        serviceArguments.add(new ServiceArgument("a",1));
        serviceArguments.add(new ServiceArgument("b",2));
        serviceArguments.add(new ServiceArgument("c",3));
        ServiceFunction f = new ServiceFunction("test","var res = a+b+c;console.log(res);return 1;");
        f.runService(serviceArguments);
    }

}

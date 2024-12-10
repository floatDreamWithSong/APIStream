package com.daydreamer.faastest;

import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.entity.ServiceFunction;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
class OBJ {
    public Integer value = 1;
}
@SpringBootTest
class FaasTestApplicationTests {

    @Test
    void tryJsTask() {
        ArrayList<ServiceArgument> serviceArguments = new ArrayList<>();
        serviceArguments.add(new ServiceArgument("a",new OBJ().toString()));
        serviceArguments.add(new ServiceArgument("b",2));
        serviceArguments.add(new ServiceArgument("c",3));
        ServiceFunction f = new ServiceFunction("test","const res = a.value+b+c;console.log(a);return 1;");
        f.runService(serviceArguments);
    }

}

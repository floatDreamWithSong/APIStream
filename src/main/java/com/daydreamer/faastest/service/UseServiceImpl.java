package com.daydreamer.faastest.service;

import com.daydreamer.faastest.common.JsonProcessor;
import com.daydreamer.faastest.controller.interfaces.UseService;
import com.daydreamer.faastest.entity.ServiceArgument;
import com.daydreamer.faastest.service.common.ServiceModulePool;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class UseServiceImpl implements UseService {

    @Override
    public String useServiceFunction(HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        if (ServiceModulePool.instance.hasServiceOnPath(path)) {
            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            return ServiceModulePool.instance.callModule(path, args);
        }
        return null;
    }
}

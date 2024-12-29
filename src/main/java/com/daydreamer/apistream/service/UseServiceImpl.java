package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.UseService;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Slf4j
@Service
public class UseServiceImpl implements UseService {

    @Override
    public String useServiceFunction(@NotNull HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        ResolvedPath _path = ModulePath.resolvePath(path);
        if (ServiceProjectPool.instance.hasModule(_path.projectName, _path.modulePath, _path.functionName)) {
            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            return ServiceProjectPool.instance.callModule(_path.projectName,_path.modulePath,_path.functionName, args);
        }
        return "";
    }
}

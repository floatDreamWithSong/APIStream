package com.daydreamer.apistream.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.dto.response.ServiceResult;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.UseService;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.service.projects.UseServiceWriter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class UseServiceImpl implements UseService {


    private final ServiceProjectPool serviceProjectPool;

    @Autowired
    public UseServiceImpl(ServiceProjectPool serviceProjectPool) {
        this.serviceProjectPool = serviceProjectPool;
    }

    @Override
    public String useServiceFunction(@NotNull HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        ResolvedPath _path = ModulePath.resolvePath(path);
        if (serviceProjectPool.hasModule(_path.projectName, _path.modulePath, _path.functionName)) {
            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            UUID moduleId = serviceProjectPool.getModuleId(_path.projectName, _path.modulePath);
            if (moduleId == null) {
                return null;
            }
            long startTime = System.currentTimeMillis();
            ServiceResult serviceResult = serviceProjectPool.callModule(_path.projectName,_path.modulePath,_path.functionName, args);
            if(serviceResult.errorMessage!=null){
                log.debug("调用模块:{} 函数:{} 失败:{}",_path.modulePath,_path.functionName,serviceResult.errorMessage);
                UseServiceWriter.submit(moduleId,-1);
                return serviceResult.toString();
            }
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            UseServiceWriter.submit(moduleId,costTime);
//            log.debug("调用模块:{} 函数:{} 耗时:{}ms",_path.modulePath,_path.functionName,costTime);
            return serviceResult.toString();
        }
        return null;
    }


}

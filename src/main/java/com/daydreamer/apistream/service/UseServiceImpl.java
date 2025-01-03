package com.daydreamer.apistream.service;

import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.UseService;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import com.daydreamer.apistream.service.projects.ServiceProjectPool;
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

    private APIStreamModuleMapper apiStreamModuleMapper;

    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        this.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    @Override
    public String useServiceFunction(@NotNull HttpServletRequest request, Map<String, Object> body) {
        String path = request.getRequestURI();
        ResolvedPath _path = ModulePath.resolvePath(path);
        if (ServiceProjectPool.instance.hasModule(_path.projectName, _path.modulePath, _path.functionName)) {
            ArrayList<ServiceArgument> args = new ArrayList<>();
            for (Map.Entry<String, Object> entry : body.entrySet()) {
                args.add(new ServiceArgument(entry.getKey(), entry.getValue()));
            }
            UUID moduleId = ServiceProjectPool.instance.getModuleId(_path.projectName, _path.modulePath);
            if (moduleId == null) {
                return null;
            }
            // 计算调用时间
            long startTime = System.currentTimeMillis();
            String serviceResult = ServiceProjectPool.instance.callModule(_path.projectName,_path.modulePath,_path.functionName, args);
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            // 记录调用日志
            APIStreamModuleEntity lastModuleEntity = apiStreamModuleMapper.selectById(moduleId.toString());
            APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
            moduleEntity.setId(moduleId.toString());
            long totalCallTimes = lastModuleEntity.getTotalCallTimes() + 1L;
            moduleEntity.setTotalCallTimes(totalCallTimes);
            if(lastModuleEntity.getMinRuntime()==0){
                moduleEntity.setMinRuntime(costTime);
            }else {
                moduleEntity.setMinRuntime(Math.min(lastModuleEntity.getMinRuntime(),costTime));
            }
            moduleEntity.setMaxRuntime(Math.max(lastModuleEntity.getMaxRuntime(),costTime));
            double avgRuntime = (lastModuleEntity.getAvgRuntime() + ((double) costTime) / totalCallTimes)/(1+1.0/totalCallTimes);
            moduleEntity.setAvgRuntime((long) Math.ceil(avgRuntime));
            log.debug("调用模块:{} 函数:{} 耗时:{}ms",_path.modulePath,_path.functionName,costTime);
            log.debug("总调用次数:{} 平均耗时:{}ms 最小耗时:{}ms 最大耗时:{}ms",totalCallTimes+1,avgRuntime,moduleEntity.getMinRuntime(),moduleEntity.getMaxRuntime());
            apiStreamModuleMapper.updateById(moduleEntity);
            return serviceResult;
        }
        return null;
    }
}

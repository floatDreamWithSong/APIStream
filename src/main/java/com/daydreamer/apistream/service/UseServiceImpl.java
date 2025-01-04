package com.daydreamer.apistream.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daydreamer.apistream.common.ModulePath;
import com.daydreamer.apistream.common.dto.response.ServiceResult;
import com.daydreamer.apistream.common.modules.ResolvedPath;
import com.daydreamer.apistream.controller.interfaces.UseService;
import com.daydreamer.apistream.common.modules.ServiceArgument;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
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

    private final APIStreamModuleMapper apiStreamModuleMapper;
    private final ServiceProjectPool serviceProjectPool;

    @Autowired
    public UseServiceImpl(APIStreamModuleMapper apiStreamModuleMapper, ServiceProjectPool serviceProjectPool) {
        this.apiStreamModuleMapper = apiStreamModuleMapper;
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
            // 计算调用时间
            long startTime = System.currentTimeMillis();
            ServiceResult serviceResult = serviceProjectPool.callModule(_path.projectName,_path.modulePath,_path.functionName, args);
            if(serviceResult.errorMessage!=null){
                log.debug("调用模块:{} 函数:{} 失败:{}",_path.modulePath,_path.functionName,serviceResult.errorMessage);
                UpdateWrapper<APIStreamModuleEntity> updateWrapper = new UpdateWrapper<>();
                updateWrapper.setSql("error_count = error_count + 1");
                updateWrapper.setSql("total_call_times = total_call_times + 1");
                updateWrapper.eq("id", moduleId.toString());
                apiStreamModuleMapper.update(updateWrapper);
                return serviceResult.toString();
            }
            long endTime = System.currentTimeMillis();
            long costTime = endTime - startTime;
            // 记录调用日志
            APIStreamModuleEntity lastModuleEntity = apiStreamModuleMapper.selectById(moduleId.toString());
//            APIStreamModuleEntity moduleEntity = new APIStreamModuleEntity();
//            moduleEntity.setId(moduleId.toString());
            long totalCallTimes = lastModuleEntity.getTotalCallTimes() + 1L;
            long minRuntime = lastModuleEntity.getMinRuntime()!=0?lastModuleEntity.getMinRuntime():costTime;
            long maxRuntime = Math.max(lastModuleEntity.getMaxRuntime(), costTime);
//            moduleEntity.setTotalCallTimes(totalCallTimes);
//            if(lastModuleEntity.getMinRuntime()==0){
//                moduleEntity.setMinRuntime(costTime);
//            }else {
//                moduleEntity.setMinRuntime(Math.min(lastModuleEntity.getMinRuntime(),costTime));
//            }
            long avgRuntime = (lastModuleEntity.getAvgRuntime()*(totalCallTimes-1) + costTime)/totalCallTimes;
//            moduleEntity.setAvgRuntime(avgRuntime);
//            moduleEntity.setErrorCount(lastModuleEntity.getErrorCount());
            UpdateWrapper<APIStreamModuleEntity> updateWrapper = new UpdateWrapper<>();

            updateWrapper.set("total_call_times", totalCallTimes);
            updateWrapper.set("min_runtime", minRuntime);
            updateWrapper.set("max_runtime",maxRuntime);
            updateWrapper.set("avg_runtime",avgRuntime);
            log.debug("调用模块:{} 函数:{} 耗时:{}ms",_path.modulePath,_path.functionName,costTime);
            log.debug("总调用次数:{} 平均耗时:{}ms 最小耗时:{}ms 最大耗时:{}ms",totalCallTimes,avgRuntime,minRuntime,maxRuntime);
            updateWrapper.eq("id", moduleId.toString());
            apiStreamModuleMapper.update(updateWrapper);
            return serviceResult.toString();
        }
        return null;
    }
}

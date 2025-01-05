package com.daydreamer.apistream.service.projects;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.daydreamer.apistream.entity.APIStreamModuleEntity;
import com.daydreamer.apistream.mapper.APIStreamModuleMapper;
import kotlin.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Service
public class UseServiceWriter {

    private static APIStreamModuleMapper apiStreamModuleMapper;
    private static final BlockingQueue<Pair<UUID, Long>> taskQueue = new LinkedBlockingQueue<>();

    @Autowired
    public void setApiStreamModuleMapper(APIStreamModuleMapper apiStreamModuleMapper) {
        UseServiceWriter.apiStreamModuleMapper = apiStreamModuleMapper;
    }

    public static void submit(UUID moduleId, long costTime) {
        taskQueue.offer(new Pair<>(moduleId, costTime));
    }

    private static void write(UUID moduleId, long costTime) {
        UpdateWrapper<APIStreamModuleEntity> updateWrapper = new UpdateWrapper<>();
        log.debug("!!!!!!!!!!!!!!处理模块:{} 耗时:{}ms", moduleId, costTime);
        if (costTime < 0) {
            updateWrapper.setSql("total_call_times = total_call_times + 1, error_count = error_count + 1");
            updateWrapper.eq("id", moduleId.toString());
            apiStreamModuleMapper.update(updateWrapper);
            return;
        }
        APIStreamModuleEntity apiStreamModuleEntity = apiStreamModuleMapper.selectById(moduleId.toString());
        long totalCallTimes = apiStreamModuleEntity.getTotalCallTimes() + 1L;
        long minRuntime = apiStreamModuleEntity.getMinRuntime() != 0 ? apiStreamModuleEntity.getMinRuntime() : costTime;
        long maxRuntime = Math.max(apiStreamModuleEntity.getMaxRuntime(), costTime);
        long avgRuntime = (apiStreamModuleEntity.getAvgRuntime() * (totalCallTimes - 1) + costTime) / totalCallTimes;
        updateWrapper.set("total_call_times", totalCallTimes);
        updateWrapper.set("min_runtime", minRuntime);
        updateWrapper.set("max_runtime", maxRuntime);
        updateWrapper.set("avg_runtime", avgRuntime);
        log.debug("总调用次数:{} 平均耗时:{}ms 最小耗时:{}ms 最大耗时:{}ms", totalCallTimes, avgRuntime, minRuntime, maxRuntime);
        updateWrapper.eq("id", apiStreamModuleEntity.getId().toString());
        apiStreamModuleMapper.update(updateWrapper);
    }

    static {
        new Thread(() -> {
            while (true) {
                Pair<UUID, Long> task = null;
                try {
                    task = taskQueue.take();
                    if (task != null) {
                        log.debug("处理模块:{} 耗时:{}ms, 剩余任务：{}", task.getFirst(), task.getSecond(), taskQueue.size());
                        write(task.getFirst(), task.getSecond());
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }
}

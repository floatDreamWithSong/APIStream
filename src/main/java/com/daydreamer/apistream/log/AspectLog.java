package com.daydreamer.apistream.log;

import com.daydreamer.apistream.common.modules.ResolvedPath;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.daydreamer.apistream.common.ModulePath;

@Aspect
@Component
@Slf4j
public class AspectLog {
    
    private static final String LOG_PATH = "logs/";
    
    @Around("execution(* com.daydreamer.apistream.service.UseServiceImpl.useServiceFunction(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object[] args = joinPoint.getArgs();
        StringBuilder functionArgs = new StringBuilder("{");
        if (args != null && args.length ==2) {
            Map<String, Object> body = (Map<String, Object>) args[1];
            if (body != null) {
                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    functionArgs.append(entry.getKey()).append(": ").append(entry.getValue()).append(",");
                }
            }
        }
        functionArgs.append("}");

        String path = request.getRequestURI();
        // 跳过静态资源请求的日志记录
        if (path.startsWith("/APIStreamStaticResources")) {
            return joinPoint.proceed();
        }
        // 创建日志目录
        String logDir = LOG_PATH + getLogDirectory(path);
        log.debug(logDir);
        // 是否已存在文件夹
        File file = new File(logDir);
        if (!file.exists()&&!new File(logDir).mkdirs()) {
            log.debug("创建日志目录失败");
            return null;
        }
        // 构建日志文件路径
        ResolvedPath _path = ModulePath.resolvePath(path);
        String logFile = LOG_PATH + _path.projectName+ _path.modulePath+ ".log";
        log.debug(logFile);
        // 记录执行时间
        long startTime = System.currentTimeMillis();
        // 执行原方法
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        // 记录请求信息
        String logContent = createLogContent(request)
                .append("ExecutionTime: ").append(executionTime).append(" ms\n")
                .append("FunctionArgs: ").append(functionArgs).append("\n")
                .append("Result: ").append(result!=null ?result.toString():"").append("\n\n").toString();
        
        // 写入日志文件
        writeLog(logFile, logContent);
        
        return result;
    }
    
    private String getLogDirectory(String path) {
        ResolvedPath _path = ModulePath.resolvePath(path);
        return _path.projectName + _path.modulePath.substring(0, _path.modulePath.lastIndexOf('/'));
    }
    
    private StringBuilder createLogContent(HttpServletRequest request) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(sdf.format(new Date())).append("\n");
        sb.append("URL: ").append(request.getRequestURI()).append("\n");
        sb.append("IP: ").append(request.getRemoteAddr()).append("\n");
        return sb;
    }
    
    private void writeLog(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content);
        } catch (IOException e) {
            log.error("写入日志文件失败: {}", e.getMessage());
        }
    }
}

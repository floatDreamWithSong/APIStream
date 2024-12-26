package com.daydreamer.faastest.log;

import com.daydreamer.faastest.common.ResolvedPath;
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

import com.daydreamer.faastest.common.ModulePath;

@Aspect
@Component
@Slf4j
public class AspectLog {
    
    private static final String LOG_PATH = "logs/";
    
    @Around("execution(* com.daydreamer.faastest.service.UseServiceImpl.useServiceFunction(..))")
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
        new File(logDir).mkdirs();
        // 构建日志文件路径
        String logFile = LOG_PATH + ModulePath.resolvePath(path).modulePath.substring(1) + ".log";
        log.debug(logFile);
        // 记录执行时间
        long startTime = System.currentTimeMillis();
        // 执行原方法
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        // 记录请求信息
        String logContent = createLogContent(request, joinPoint)
                .append("Execution Time: ").append(executionTime).append(" ms\n")
                .append("Function Args: ").append(functionArgs).append("\n")
                .append("Result: ").append(result.toString()).append("\n\n").toString();
        
        // 写入日志文件
        writeLog(logFile, logContent);
        
        return result;
    }
    
    private String getLogDirectory(String path) {
        ResolvedPath _path = ModulePath.resolvePath(path);
        return _path.projectName + _path.modulePath.substring(1);
    }
    
    private StringBuilder createLogContent(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(sdf.format(new Date())).append("\n");
        sb.append("URL: ").append(request.getRequestURI()).append("\n");
//        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("IP: ").append(request.getRemoteAddr()).append("\n");
//        sb.append("Class Method: ").append(joinPoint.getSignature()).append("\n");
        return sb;
    }
    
    private void writeLog(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content);
        } catch (IOException e) {
            log.error("写入日志文件失败: " + e.getMessage());
        }
    }
}

package com.daydreamer.faastest.log;

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
import com.daydreamer.faastest.common.ModulePath;

@Aspect
@Component
@Slf4j
public class AspectLog {
    
    private static final String LOG_PATH = "logs/";
    
    @Around("execution(* com.daydreamer.faastest.controller.ServiceController.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
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
        // 记录请求信息
        String logContent = createLogContent(request, joinPoint);
        
        // 执行原方法
        Object result = joinPoint.proceed();
        
        // 追加响应信息
        logContent += "\nResponse: " + result.toString() + "\n\n";
        
        // 写入日志文件
        writeLog(logFile, logContent);
        
        return result;
    }
    
    private String getLogDirectory(String path) {
        path = ModulePath.resolvePath(path).modulePath;
                //以字符串的最后一个'/'为分隔符分成两个字符串，返回前一个部分
        int lastIndex = path.lastIndexOf('/');
        if (lastIndex != -1) {
            return path.substring(1, lastIndex);
        }
        return path;
    }
    
    private String createLogContent(HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Time: ").append(sdf.format(new Date())).append("\n");
        sb.append("URI: ").append(request.getRequestURI()).append("\n");
        sb.append("Method: ").append(request.getMethod()).append("\n");
        sb.append("IP: ").append(request.getRemoteAddr()).append("\n");
        sb.append("Class Method: ").append(joinPoint.getSignature()).append("\n");
        return sb.toString();
    }
    
    private void writeLog(String filePath, String content) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write(content);
        } catch (IOException e) {
            log.error("写入日志文件失败: " + e.getMessage());
        }
    }
}

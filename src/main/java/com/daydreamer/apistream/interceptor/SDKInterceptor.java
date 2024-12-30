package com.daydreamer.apistream.interceptor;

import com.daydreamer.apistream.common.KeyPath;
import com.daydreamer.apistream.configuration.SDKConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class SDKInterceptor implements HandlerInterceptor {

    private boolean isSDKService(String path){
        for(String url : KeyPath.keyUrls){
            if(path.startsWith(url)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if(HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }
        boolean flag = true;
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        if(log.isDebugEnabled()) {
            if (handler instanceof ResourceHttpRequestHandler) {
                log.debug("preHandle:这是一个静态资源！");
            } else if (handler instanceof HandlerMethod handlerMethod) {
                String ip = request.getRemoteAddr();
                Method method = handlerMethod.getMethod();
                log.debug("用户IP:{},访问目标:{}.{}", ip, method.getDeclaringClass().getName(), method.getName());
            }
        }
        String path = request.getRequestURI();
        if(isSDKService(path)) {
            String userToken = request.getHeader("Authorization");
            if(!SDKConfig.sdkToken.isEmpty() && !SDKConfig.sdkToken.equals(userToken)) {
                response.setStatus(401);
                return false;
            }
        }
        return flag;
    }

    @Override
    public void postHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, ModelAndView modelAndView) {
        if (handler instanceof ResourceHttpRequestHandler) {
            if (log.isDebugEnabled()) {
                log.debug("postHandle:这是一个静态资源访问！");
            }
        } else if (handler instanceof HandlerMethod) {
            long startTime = (long) request.getAttribute("requestStartTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            int time = 100;
            String path = request.getRequestURI();
            if(isSDKService(path)){
                    log.info("<{}> 耗时 : {}ms, 完成时间 : {}", path, executeTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime)));
                    return;
            }
            if (executeTime > time) {
                log.warn("[{}] 执行耗时 : {}ms", path, executeTime);
            } else {
                log.info("[{}] 执行耗时 : {}ms", path, executeTime);
            }
        }
    }

}

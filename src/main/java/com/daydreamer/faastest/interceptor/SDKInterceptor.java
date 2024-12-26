package com.daydreamer.faastest.interceptor;

import com.daydreamer.faastest.entity.SDKConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class SDKInterceptor implements HandlerInterceptor {

    private boolean isSDKService(String path){
        return switch (path) {
            case "/APIStreamModuleDetailQueryService", "/APIStreamModuleQueryService",
                 "/APIStreamProjectQueryService" -> !log.isDebugEnabled();
            case "/APIStreamModuleServiceSDK" -> true;
            default -> false;
        };
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 浏览器的预检请求直接放行
        if(HttpMethod.OPTIONS.toString().equals(request.getMethod())) {
            return true;
        }

        boolean flag = true;
        // 记录请求开始时间
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);

        if(log.isDebugEnabled()) {
            if (handler instanceof ResourceHttpRequestHandler) {
                log.debug("preHandle:这是一个静态资源方法！");
            } else if (handler instanceof HandlerMethod handlerMethod) {
                String ip = request.getRemoteAddr();
                Method method = handlerMethod.getMethod();
                log.debug("用户IP:{},访问目标:{}.{}", ip, method.getDeclaringClass().getName(), method.getName());
            }
        }
//        User user = (User) request.getSession().getAttribute("user");
//        if (null == user) {
//            //重定向到登录页面
//            response.sendRedirect("toLogin");
//            flag = false;
//        }
        String path = request.getRequestURI();
        if(isSDKService(path)) {
            String userToken = request.getHeader("Authorization");
            if(!SDKConfig.sdkToken.isEmpty() && !SDKConfig.sdkToken.equals(userToken)) {
                return false;
            }
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (handler instanceof ResourceHttpRequestHandler) {
            if (log.isDebugEnabled()) {
                log.debug("postHandle:这是一个静态资源访问！");
            }
        } else if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
            long startTime = (long) request.getAttribute("requestStartTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;
            int time = 1000;
            String path = request.getRequestURI();
            if(isSDKService(path)){
                    log.info("<{}> 部署耗时 : {}ms, 完成时间 : {}", path, executeTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(endTime)));
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

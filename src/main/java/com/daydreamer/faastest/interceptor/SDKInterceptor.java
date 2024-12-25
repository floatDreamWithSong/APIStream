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

@Slf4j
public class SDKInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(HttpMethod.OPTIONS.equals(request.getMethod())) {
            log.info("options请求");
            return true;
        }
        boolean flag = true;
        String ip = request.getRemoteAddr();
        long startTime = System.currentTimeMillis();
        request.setAttribute("requestStartTime", startTime);
//        if (handler instanceof ResourceHttpRequestHandler) {
//            System.out.println("preHandle这是一个静态资源方法！");
//        } else if (handler instanceof HandlerMethod) {
//            HandlerMethod handlerMethod = (HandlerMethod) handler;
//            Method method = handlerMethod.getMethod();
//            System.out.println("用户:" + ip + ",访问目标:" + method.getDeclaringClass().getName() + "." + method.getName());
//        }

        //如果用户未登录
//        User user = (User) request.getSession().getAttribute("user");
//        if (null == user) {
//            //重定向到登录页面
//            response.sendRedirect("toLogin");
//            flag = false;
//        }
        String userToken = request.getHeader("Authorization");
        log.info("userToken:{}", userToken);
        log.info("sdkToken:{}", SDKConfig.sdkToken);
        if(!SDKConfig.sdkToken.isEmpty() && !SDKConfig.sdkToken.equals(userToken)) {
//            flag = false;
        }
        return flag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if (handler instanceof ResourceHttpRequestHandler) {
            System.out.println("postHandle这是一个静态资源方法！");
        } else if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            long startTime = (long) request.getAttribute("requestStartTime");
            long endTime = System.currentTimeMillis();
            long executeTime = endTime - startTime;

            int time = 1000;
            //打印方法执行时间
            if (executeTime > time) {
                System.out.println("[" + method.getDeclaringClass().getName() + "." + method.getName() + "] 执行耗时 : "
                        + executeTime + "ms");
            } else {
                System.out.println("[" + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "] 执行耗时 : "
                        + executeTime + "ms");
            }
        }
    }

}

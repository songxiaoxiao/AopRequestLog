package com.web.core.app.interceptors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class BasicsLogInterceptor extends HandlerInterceptorAdapter {

    private Long start_time;
    private Logger logger = LoggerFactory.getLogger(BasicsLogInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("---------------------=基于拦截器记录日志请求开始----------------------");
        start_time = (new Date()).getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

        logger.info( "[" + format.format(new Date()) + "] " + getIpAddr(request) + "  "  + request.getMethod() + "  " + request.getRequestURI());

        logger.info("[HEADER]  "  + getHeadersInfo(request));

        logger.info("[PARAMS]  "  + getBodyInfo(request));

        logger.info("[ParameterMap]  "  + request.getParameterMap().keySet());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        logger.info( "[运行时间] " + ((new Date()).getTime() - start_time) + "ms");
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    private Map<String, String> getHeadersInfo(HttpServletRequest request) {
        Map<String, String> map = new HashMap<>();
        Enumeration headerNames = request.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;

    }

    private Map<String, String> getBodyInfo(HttpServletRequest request){
        Map<String, String> map = new HashMap<>();

        Enumeration bodyNames = request.getParameterNames();

        while (bodyNames.hasMoreElements()) {
            String key = (String) bodyNames.nextElement();
            String value = request.getParameter(key);
            map.put(key, value);
        }
        return map;
    }

    /**
     * 获取登录用户远程主机ip地址
     *
     * @param request
     * @return
     */
    private String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}

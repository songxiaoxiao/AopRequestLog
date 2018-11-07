package com.web.core.app.aspect;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
/**
 * aop切面实现项目接口统一日志记录
 * @author winter
 *
 * */
@Aspect
@Component
public class BasicsLogAspect {

    private Long start_time;
    private static Logger logger = LoggerFactory.getLogger(BasicsLogAspect.class);

    /**
     * 定义切点
     * @deprecated 配置切点，注意.*代表匹配任意
     * @deprecated 执行处罚         返回类型  类                方法(参数)
     * */
    @Pointcut("execution(public * com.web.core.app.controller.*.*(..))")
    public void webRequestLog(){

    }

    /**
     *  目标方法之前执行
     * @author winter
     *
     * */
    @Before("webRequestLog()")
    public void doBefore(JoinPoint joinPoint){
        try{
            logger.info("===========================基于AOP切面记录日志请求开始============================");
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();


            start_time = (new Date()).getTime();
            SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

            logger.info( "[" + format.format(new Date()) + "] " + getIpAddr(request) + "  "  + request.getMethod() + "  " + request.getRequestURI());

            logger.info("[HEADER]  "  + getHeadersInfo(request));

            logger.info("[PARAMS]  "  + getBodyInfo(request));

            if ("POST".equals(request.getMethod())){
                Object[] paramsArray = joinPoint.getArgs();
                logger.info("[POST_PARAMS]  " + argsArrayToString(paramsArray));
            }
        }catch (Exception excception){
            logger.info("***记录日志失败doBefore***");
        }

    }
    /**
     * 目标方法返回后调用
     *
     * @author winter
     *
     * */
    @AfterReturning(returning = "result", pointcut = "webRequestLog()")
    public void doAfterReturning(Object result){
        try{
            logger.info("[RESPONSE] " + JSON.toJSONString(result));
            logger.info( "[运行时间] " + ((new Date()).getTime() - start_time) + "ms");
        }catch (Exception exception){
            logger.info("***记录日志失败doAfterReturning***");
        }
    }

    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                Object jsonObj = JSON.toJSON(paramsArray[i]);
                params += jsonObj.toString() + " ";
            }
        }
        return params.trim();
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

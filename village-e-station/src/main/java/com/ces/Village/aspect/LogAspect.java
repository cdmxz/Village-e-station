package com.ces.village.aspect;

import com.ces.village.utils.JsonConvertUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 日志切面，用于打印请求和响应的日志
 */

@Component
@Aspect
@Log4j2
public class LogAspect {

    @Pointcut("execution(public * com.ces.village.controller.user.*.*(..))")
    public void logExecution() {

    }

    @Around("logExecution()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        String methodName = point.getSignature().toShortString();
        Object[] parameterValues = point.getArgs();
        String[] parameterNames = ((MethodSignature) point.getSignature()).getParameterNames();
        // 打印方法名和参数值
        String jsonString = JsonConvertUtil.toJsonString(assembleParameter(parameterNames, parameterValues));
        log.info("方法：" + methodName + "请求参数：" + jsonString);

        Object result;
        try {
            result = point.proceed();
        } catch (Exception e) {
            log.error("方法：" + methodName + "请求参数：" + jsonString);
            throw e;
        }
        // debug下打印响应数据
        if (log.isDebugEnabled()) {
            log.debug("方法：{}, 响应值：{}", methodName, JsonConvertUtil.toJsonString(result));
        }
        log.info("方法：{}, 响应值：{}", methodName, JsonConvertUtil.toJsonString(result));
        return result;
    }

    /**
     * 拼接方法参数和值
     *
     * @param parameterNames
     * @param parameterValues
     * @return
     */
    private Map<String, Object> assembleParameter(String[] parameterNames, Object[] parameterValues) {
        Map<String, Object> parameterNameAndValues = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            Object value = parameterValues[i];
            // 不实例化servlet对象
            if (!(value instanceof HttpServletRequest) && !(value instanceof HttpServletResponse)) {
                parameterNameAndValues.put(parameterNames[i], parameterValues[i]);
            }
        }

        return parameterNameAndValues;
    }
}
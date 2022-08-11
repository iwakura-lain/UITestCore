package com.qalain.ui.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author lain
 * @Description
 * @create 2022-01-22
 */
@Slf4j
public class BeanUtil {

    public static Object invokeMethod(Class clazz, Object instance, String methodName, Class[] paramTypes, Object[] paramValues) {
        Method method;
        if (paramTypes == null || paramTypes.length == 0) {
            method = BeanUtils.findMethod(clazz, methodName);
        } else {
            method = BeanUtils.findMethod(clazz, methodName, paramTypes);
        }
        Object result = null;
        try {
            result = method.invoke(instance, paramValues);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }
}

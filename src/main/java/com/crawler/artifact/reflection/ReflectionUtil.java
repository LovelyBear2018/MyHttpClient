package com.crawler.artifact.reflection;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.crawler.artifact.annocation.*;
import com.crawler.artifact.type.ParamaType;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;

import java.util.Map;
import java.util.HashMap;
/**
 * Created by liuzhixiong on 2018/9/18.
 */
public class ReflectionUtil {

    /**
     * 根据Parama注解获取注解值与实参映射关系
     * 注:Parama注解必须写在形参列表最前边,中间不能夹杂无注解形参
     * @param method
     * @param args
     * @return
     */
    public static Map<ParamaType, Object> getMethodParameterNamesByAnnotation(Method method, Object[] args) {

        Map<ParamaType, Object> map = new HashMap<ParamaType, Object>();

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        if (parameterAnnotations == null || parameterAnnotations.length == 0) {
            return map;
        }
        int i = 0;
        for (Annotation[] parameterAnnotation : parameterAnnotations) {
            for (Annotation annotation : parameterAnnotation) {
                if (annotation instanceof Url) {
                    map.put(ParamaType.URL, args[i++]);
                }
                if (annotation instanceof Headers) {
                    map.put(ParamaType.HEADERS, args[i++]);
                }
                if (annotation instanceof Params) {
                    map.put(ParamaType.PARAMS, args[i++]);
                }
                if (annotation instanceof Cookies) {
                    map.put(ParamaType.COOKIES, args[i++]);
                }
                if (annotation instanceof Proxy) {
                    map.put(ParamaType.PROXY, args[i++]);
                }
            }
        }
        return map;
    }

    /**
     * 获取形参名-实参值键值对关系
     * @param clazz
     * @param method
     * @param args
     * @return
     */
    public static Map<String, Object> getTypeAndValues(Class clazz, Method method, Object[] args) {

        Map<String, Object> typeAndValue = new HashMap<String, Object>();

        try {
            ClassPool pool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(clazz);
            pool.insertClassPath(classPath);
            CtClass cc = pool.get(clazz.getName());
            String name = method.getName();
            CtMethod ctm = cc.getDeclaredMethod(name);
            MethodInfo methodInfo = ctm.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attribute = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            int pos = Modifier.isStatic(ctm.getModifiers()) ? 0 : 1;
            for (int i = 0; i < ctm.getParameterTypes().length; i++) {
                typeAndValue.put(attribute.variableName(i + pos).toLowerCase(), args[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return typeAndValue;
    }

    /**
     * 根据clazz和fieldName获取对象的成员变量信息
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Field getFieldByName(Class<?> clazz, String fieldName){
        Field[] fields = clazz.getDeclaredFields();
        for (Field field:fields) {
            if(field.getName().toLowerCase().contains("mapper")){
                return field;
            }
        }
        return  null;
    }
}

package com.crawler.artifact.aspect;

import com.crawler.artifact.annocation.Session;
import com.crawler.artifact.reflection.ReflectionUtil;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * Created by liuzhixiong on 2018/9/20.
 * 注解拦截器
 */

@Aspect
@Resource
public class SessionInterceptor {


    /**
     * 切点
     *
     * @execution 切点表达式
     * @anyMethod 签名
     */
    @Pointcut("execution(* com..*.service..*.*(..))")
    private void anyMethod() {
    }

    /**
     * 环绕通知
     * 执行前:扫描Context注解,并将sessionID存入线程本地变量
     * 执行后:扫描Proxy注解,并根据服务类型,航司类型,代理类型更新代理成功或者失败率
     *
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("anyMethod()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {

        Object target = pjp.getTarget();

        Class targetClazz = target.getClass();

        if (!targetClazz.isInterface()) {
            Signature signature = pjp.getSignature();
            MethodSignature ms = (MethodSignature) signature;
            Method currentMethod = targetClazz.getMethod(ms.getName(), ms.getParameterTypes()); //SVC中的method

            Annotation[] annotations = currentMethod.getAnnotations();

            Object[] args = pjp.getArgs();

            Object sessionID = null;
            if (annotations != null && annotations.length > 0) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Session) {
                        Map<String, Object> typeAndValue = ReflectionUtil.getTypeAndValues(targetClazz, currentMethod, args); //获取形参名字-实参值对应键值对
                        sessionID = typeAndValue.get("sessionid");
                        if ((sessionID == null || !(sessionID instanceof String)) && typeAndValue.size() == 1){
                            for(Map.Entry<String, Object> entry:typeAndValue.entrySet()){
                                Object obj = entry.getValue();
                                Class clazz = obj.getClass();
                                Method[] methods = clazz.getMethods();
                                for(Method method:methods){
                                    if(method.getName().toLowerCase().equals("getsessionid")){
                                        sessionID = method.invoke(obj);
                                        break;
                                    }
                                }
                            }
                        }
                        if(sessionID != null){
                            SessionSwitcher.setSessionId((String) sessionID);
                        }
                    }
                }
            }

            Object result = pjp.proceed(); //实际方法执行

            return result;
        }
        return null;
    }
}

package com.legend.factory;

import com.legend.utils.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author Legend
 * @data by on 18-10-28.
 * @description
 */
public abstract class AbsMethodAdvance implements MethodInterceptor {

    /**
     * 要被代理的目标对象
     */
    private Object targetObject;

    /**
     * 被代理的方法名
     */
    private String proxyMethodName;

    public Object createProxyObject(Object target) {
        this.targetObject = target;
        // 该类用于生成代理对象
        Enhancer enhancer = new Enhancer();
        // 设置目标类为代理对象的父类
        enhancer.setSuperclass(this.targetObject.getClass());
        // 设置回调对象为本身
        enhancer.setCallback(this);

        return enhancer.create();
    }

    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        String proxyMethod = getProxyMethodName();
        if (StringUtils.isEmpty(proxyMethod) || !proxyMethod.equals(method.getName())) {
            return methodProxy.invokeSuper(proxy, args);
        }
        doBefore();
        // 执行拦截的方法
        Object object = methodProxy.invokeSuper(proxy, args);
        doAfter();
        return object;
    }

    public abstract void doBefore();

    public abstract void doAfter();

    public String getProxyMethodName() {
        return proxyMethodName;
    }

    public void setProxyMethodName(String proxyMethodName) {
        this.proxyMethodName = proxyMethodName;
    }
}

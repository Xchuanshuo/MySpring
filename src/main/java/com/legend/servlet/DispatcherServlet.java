package com.legend.servlet;

import com.legend.factory.InitBean;
import com.legend.springmvc.*;
import com.legend.utils.JsonUtils;
import com.legend.utils.TypeUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * @author Legend
 * @data by on 18-10-9.
 * @description
 */
@WebServlet(name = "DispatcherServlet")
@Slf4j
public class DispatcherServlet extends HttpServlet {

    private static final String REQUEST_MAPPING = "bindingRequestMapping";
    private static final String CONTAINER_MAP = "beanContainerMap";

    /**
     * 初始化Servlet 将Bean容器和HandlerMapping放入到Servlet的全局变量中
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        InitBean initBean = new InitBean();
        initBean.initBeans();
        // 根据Bean容器中注册的Bean获得HandlerMapping
        Map<String, Method> bindingRequestMapping = Handler.bindingRequestMapping(initBean.beanContainerMap);
        ServletContext servletContext = this.getServletContext();
        servletContext.setAttribute(CONTAINER_MAP, initBean.beanContainerMap);
        servletContext.setAttribute(REQUEST_MAPPING, bindingRequestMapping);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            doDispatch(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("控制器处理一次");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    // 接收到请求后转发到相应的方法上
    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException, IOException {
        ServletContext servletContext = this.getServletContext();
        Map<String,Method> bindingRequestMapping = (Map<String, Method>) servletContext.getAttribute(REQUEST_MAPPING);
        Map<String,Object> beanContainerMap = (Map<String, Object>) servletContext.getAttribute(CONTAINER_MAP);
        String url = request.getServletPath();
        // 获取url所映射的方法
        Method method = bindingRequestMapping.get(url);
        if (method == null) return;
        List<Object> resultParameters = Binding.bindingMethodParameters(method.getParameters(), request);
        // 方法返回值类型
        Class<?> returnType = method.getReturnType();
        // 从bean容器获取该方法的对象
        Object object = beanContainerMap.get(method.getDeclaringClass().getName());
        // 如果返回值是ModelAndView 进行绑定
        if ("ModelAndView".equals(returnType.getSimpleName())) {
            // 获取springmvc.xml中配置的视图解析器
            ViewResolver resolver = (ViewResolver) beanContainerMap.get("com.legend.springmvc.ViewResolver");
            String prefix = resolver.getPrefix();
            String suffix = resolver.getSuffix();
            ModelAndView modelAndView = (ModelAndView) method.invoke(object, resultParameters.toArray());
            // 将request和model中的数据进绑定
            BindingRequestAndModel.bindingRequestAndModel(modelAndView, request);
            String returnViewName = modelAndView.getView();
            // 返回的路径
            String resultAddress = prefix + returnViewName + suffix;
            try {
                request.getRequestDispatcher(resultAddress).forward(request, response);
            } catch (ServletException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Object cur = method.invoke(object, resultParameters.toArray());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            if (TypeUtils.isBasicType(returnType.getSimpleName())) {
                response.getWriter().print(cur);
            } else {
                String str = JsonUtils.objToJson(cur);
                response.getWriter().print(str);
            }
        }
    }
}
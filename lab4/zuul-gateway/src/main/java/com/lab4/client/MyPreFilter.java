package com.lab4.client;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

/**
 * @author liyijia
 * @create 2023-11-2023/11/18
 * Zuul 的过滤器：在Zuul包含有两大核心功能，第一个是路由，另一个就是过滤器了，
 * 路由可以对客户端访问的URL进行跳转，过滤器则是对整个请求的一些拦截操作；
 * 在Zuul中提供有一系列内置的过滤器，也可以让用户自定义编写过滤器来实现
 * 自身业务的功能；
 * 在Zuul中提供有4种过滤器，分别为pre、route、post、error
 *
 */

@Component
public class MyPreFilter extends ZuulFilter {
    /**
     * 执行时机
     * pre:     在路由到下游微服务之前执行
     * route:   在路由到下游微服务之前执行(在pre过滤器之后)执行
     * post:    执行完了下游微服务的逻辑之后来到post过滤器
     * error:   在其他过滤器执行出现异常的时候执行error过滤器
     *
     * @return "pre"表示这是一个前置过滤器.作为前置过滤器，MyPreFilter 在请求路由之前执行。
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器执行逻辑
     *
     * @return 返回null表示放行，访问对应的微服务
     * @throws ZuulException
     * zuul不允许直接添加或修改传入请求中的HTTP头部，
     * 要向http头部添加请求头，应使用RequestContext 的addZuulRequestHeader()
     * 方法，该方法将维护一个单独的HTTP头部映射，当zuul服务器调用目标服务是，
     * 包含在ZuulRequestHeader映射中的数据将被合并。
     *
     */
    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();

        System.out.println("[PreFilter] Request Method: " + ctx.getRequest().getMethod());
        System.out.println("Request URL: " + ctx.getRequest().getRequestURL().toString());
        System.out.println("Add header to request\n");

        ctx.addZuulRequestHeader("My-Test-Header", "My Zuul filter is adding new HTTP header to HTTP request...");
        return null;
    }

    /**
     * 是否执行该过滤器
     * true:执行
     * false:不执行
     *
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 执行顺序
     * 数字越大，优先级越低
     *
     * @return 一个很大的数字，说明执行的优先级较低
     */
    @Override
    public int filterOrder() {
        return 0;
    }


}

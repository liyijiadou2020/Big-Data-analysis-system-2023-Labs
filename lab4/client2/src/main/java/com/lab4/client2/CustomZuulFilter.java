package com.lab4.client2;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.stereotype.Component;

/**
 * @author liyijia
 * @create 2023-11-2023/11/18
 */

@Component
public class CustomZuulFilter extends ZuulFilter {
    @Override
    public Object run() {
        final RequestContext ctx = RequestContext.getCurrentContext();
        ctx.addZuulRequestHeader("Test", "TestSample");
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 999;
    }

    @Override
    public String filterType() {
        return "pre";
    }
}

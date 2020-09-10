package com.example.filter;

import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.util.UUID;

// TODO: クラスをライブラリモジュールに移動させる。

/**
 * 独自のFilterクラス.
 */
public class CustomFilter implements Filter {
    @Override
    public void init(final FilterConfig filterConfig) {
    }

    @Override
    public final void doFilter(final ServletRequest servletRequest,
                               final ServletResponse servletResponse,
                               final FilterChain filterChain)
            throws IOException, ServletException {

        try {
            MDC.put("requestID", UUID.randomUUID().toString());
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove("requestID");
        }
    }

    @Override
    public void destroy() {
    }
}

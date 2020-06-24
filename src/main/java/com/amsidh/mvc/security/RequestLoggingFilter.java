package com.amsidh.mvc.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static java.lang.String.format;

@Slf4j
public class RequestLoggingFilter extends GenericFilterBean {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String remoteAddress = "";
        if (request != null) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            log.info("httpServletRequest.getHeader(\"X-FORWARDED-FOR\") :"+ httpServletRequest.getHeader("X-FORWARDED-FOR"));
            remoteAddress = httpServletRequest.getHeader("X-FORWARDED-FOR");
            if (remoteAddress == null || "".equals(remoteAddress)) {
                remoteAddress = request.getRemoteAddr();
            }
            log.info(format("Requested Address :", remoteAddress));
        }
        chain.doFilter(request,response);
    }
}

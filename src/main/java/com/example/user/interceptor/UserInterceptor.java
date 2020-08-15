package com.example.user.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
@Component
public class UserInterceptor implements HandlerInterceptor {

    private static Logger logger = LoggerFactory.getLogger(UserInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        InputStream resStream = request.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(resStream, "UTF-8"));
        StringBuffer resBuffer = new StringBuffer();
        String resTemp = "";
        while ((resTemp = br.readLine()) != null) {
            resBuffer.append(resTemp);
        }
        String requestString = resBuffer.toString();
        logger.info(request.getRequestURL()+"请求" + requestString);
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}

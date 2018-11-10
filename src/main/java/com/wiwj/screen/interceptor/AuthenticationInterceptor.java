package com.wiwj.screen.interceptor;

import com.wiwj.screen.annotation.LoginRequired;
import com.wiwj.screen.model.User;
import com.wiwj.screen.util.VideoUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是映射到方法直接通过
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod(); // 判断接口是否需要登录
        LoginRequired methodAnnotation = method.getAnnotation(LoginRequired.class); // 有 @LoginRequired 注解，需要认证
        if (methodAnnotation != null) {
            HttpSession session = request.getSession();
            if (session != null) {
                User user = (User) session.getAttribute("user");
                if (user != null && user.getToken() != null) {
                    String dateStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
                    String token = VideoUtil.md5(user.getUserid() + user.getPass() + dateStr);
                    if (token.equalsIgnoreCase(user.getToken())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

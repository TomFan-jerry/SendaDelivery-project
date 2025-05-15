package com.senda.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.senda.constant.MessageConstant;
import com.senda.context.JwtUserContext;
import com.senda.enumeration.TokenType;
import com.senda.result.Result;
import com.senda.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Slf4j
@Component
public class JwtTokenAdminInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    private void sendError(HttpServletResponse response, String message) throws IOException {
        Result<String> error = Result.error(message);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSONObject.toJSONString(error));
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取请求url
        String url = request.getRequestURI();
        log.info("请求的url:{}", url);

        //获取请求头中的jwt令牌(token)
        String jwt = request.getHeader("token");

        //判断令牌是否存在
        if (!StringUtils.hasLength(jwt)) {
            log.info("请求头token为空，返回未登录错误信息");
            Result<String> error = Result.error(MessageConstant.NOT_LOGIN);
            response.setContentType("application/json;charset=UTF-8"); // 设置编码
            String notLogin = JSONObject.toJSONString(error);
            response.getWriter().write(notLogin);
            return false;
        }

        //解析令牌
        try {
            jwtUtil.parseToken(jwt, TokenType.ADMIN);
        } catch (ExpiredJwtException e) {
            log.info("令牌已过期，返回未登录信息");
            sendError(response, MessageConstant.TOKEN_TIMEOUT);
            return false;
        } catch (JwtException e) {
            log.info("令牌解析失败，返回错误信息", e);
            sendError(response, MessageConstant.TOKEN_ERROR);
            return false;
        }

        //合法的令牌,放行
        log.info("令牌合法，放行");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        JwtUserContext.clear();
    }
}

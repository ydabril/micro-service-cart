package com.emazon.mscart.adapters.driving.http.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class ArticleCartUtils {
    public static Long extractUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return (Long) request.getAttribute(ArticleCartConstants.USER_ID);
    }
}

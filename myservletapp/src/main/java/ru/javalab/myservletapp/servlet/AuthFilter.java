package ru.javalab.myservletapp.servlet;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter("/")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        if (httpRequest.getSession().getAttribute("authorized") != null && !httpRequest.getSession().getAttribute("authorized").equals("false")){
            filterChain.doFilter(servletRequest, servletResponse);
            //return;
        } else {
            httpResponse.sendRedirect(httpRequest.getServletContext().getContextPath() + "/login");
       // return;
        } //  filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}

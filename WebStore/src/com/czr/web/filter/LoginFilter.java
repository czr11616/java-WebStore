package com.czr.web.filter;

import java.io.IOException;
import java.net.URLDecoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.czr.domain.User;
import com.czr.service.UserService;

public class LoginFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//ǿת
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		//��ȡrequest�е�cookie
		Cookie[] cookies = httpRequest.getCookies();
		if(cookies != null) {
			String username = null;
			String password = null;
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals("username")) {
					username = URLDecoder.decode(cookie.getValue(), "utf-8");
				}
				if(cookie.getName().equals("password")) {
					password = cookie.getValue();
				}
			}
			//����õ��û���������������Զ���¼
			if(username != null && password != null) {
				
				User user = null;
				try {
					user= new UserService().login(username, password);
				} catch (Exception e) {
					e.printStackTrace();
				}
				//�����½�ɹ�
				if(user != null) {
					//��½�ɹ���user����sessino����
					HttpSession session = httpRequest.getSession();
					session.setAttribute("user", user);
				}
			}
		}
		//���filter����
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
	}

}

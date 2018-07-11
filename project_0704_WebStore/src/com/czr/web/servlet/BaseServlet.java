package com.czr.web.servlet;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseServlet extends HttpServlet {
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		req.setCharacterEncoding("utf-8");
		try {
			/*
			 * ���÷���ͨ����������ִ����Ӧ����
			 */
			//��÷�����
			String methodName= req.getParameter("method");
			//��õ�ǰִ�ж����class����
			Class clazz = this.getClass();
			//ͨ��class�����ȡ��Ӧ�ķ���
			Method method = clazz.getMethod(methodName, HttpServletRequest.class,HttpServletResponse.class);
			//ͨ����������ִ�з���
			method.invoke(this, req,resp);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
package com.czr.web.servlet;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;

import com.czr.domain.User;
import com.czr.service.UserService;

public class UserServlet extends BaseServlet{
	
	/*
	 * У���û����Ƿ����
	 */
	public void checkUsername(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		request.setCharacterEncoding("utf-8");
		String username = request.getParameter("username");
		UserService service = new UserService();
		boolean flag = service.checkUsername(username);
		String json = "{\"isExit\":"+flag+"}";
		response.getWriter().write(json);
	}
	/*
	 * �û�ע��
	 */
	public void register(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		
		//���Ƚ�����֤���У��
		String code1 = request.getParameter("checkCode");
		String code2 = (String) request.getSession().getAttribute("checkcode_session");
		request.getSession().removeAttribute("checkcode_session");
		//У�鲻�ɹ������ش�����Ϣ���ɹ��������ע��
		if(!code1.equals(code2)) {
			request.getSession().setAttribute("register_info", "��֤�����");
			response.sendRedirect("register.jsp");
		}else {	
			try {
				//��װ�ͻ����û�����
				Map<String, String[]> parameterMap = request.getParameterMap();
				User user = new User();
				BeanUtils.populate(user, parameterMap);
				user.setUid(UUID.randomUUID().toString());
				user.setCode(UUID.randomUUID().toString());
				user.setState(0);
				user.setTelephone(null);
				//����service�㷽������ע��
				UserService service = new UserService();
				boolean flag = service.register(user);
				//ע��ɹ�����ת���ȴ���¼ҳ�棬���򷵻�ע��ҳ��
				if(flag == true) {
					request.getRequestDispatcher("/register_success_info.jsp").forward(request, response);
				}else {
					request.getRequestDispatcher("/register.jsp").forward(request, response);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	/*
	 * �û���¼
	 */
	public void login(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException{
		
		//���Ƚ�����֤���У��
		String code1 = request.getParameter("checkcode");
		String code2 = (String) request.getSession().getAttribute("checkcode_session");
		request.getSession().removeAttribute("checkcode_session");
		//У�鲻�ɹ������ش�����Ϣ���ɹ�������е�¼
		if(!code1.equals(code2)) {
			request.getSession().setAttribute("login_info", "��֤�����");
			response.sendRedirect("login.jsp");
		}else {	
			//��ȡ�û���¼��Ϣ
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String autoLogin = request.getParameter("autoLogin");//��ȡ�Ƿ�ѡ�Զ���¼
			UserService service = new UserService();
			User user = null;
			try {
				user = service.login(username,password);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//��½�ɹ�
			if(user != null) {
				HttpSession session = request.getSession();
				session.setAttribute("user", user);
				//�ж��Ƿ�ѡ�Զ���¼
				if(autoLogin != null) {
					//����cookie
					Cookie cookie_username = new Cookie("username", URLEncoder.encode(username, "utf-8"));
					Cookie cookie_password = new Cookie("password",password);
					//����cookie�ĳ־û�ʱ��
					cookie_username.setMaxAge(2*60*60);
					cookie_password.setMaxAge(2*60*60);
					//����cookie����Ч·��
					cookie_username.setPath(request.getContextPath());
					cookie_username.setPath(request.getContextPath());
					//����cookie
					response.addCookie(cookie_username);
					response.addCookie(cookie_password);
				}
				response.sendRedirect(request.getContextPath());
			//��¼ʧ��
			}else {
				request.setAttribute("log_info", "�û������������");
				request.getRequestDispatcher("login.jsp").forward(request, response);
			}
		}
	}
	/*
	 * �˳���¼
	 */
	public void logOut(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		/*
		 * ������ɾ���ͻ���Я����cookie�Լ�ɾ��session���е�user
		 */
		Cookie cookie1 = new Cookie("username",null);
		cookie1.setMaxAge(0);
		Cookie cookie2 = new Cookie("password",null);
		cookie2.setMaxAge(0);
		//�������cookieһ��Ҫ��д���ͻ���
		response.addCookie(cookie1);
		response.addCookie(cookie2);
		request.getSession().removeAttribute("user");
		response.sendRedirect(request.getContextPath()+"/login.jsp");
	}
}

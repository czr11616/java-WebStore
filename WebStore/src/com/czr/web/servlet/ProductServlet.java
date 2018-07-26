package com.czr.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.czr.domain.Category;
import com.czr.domain.PageBean;
import com.czr.domain.Product;
import com.czr.service.CategoryService;
import com.czr.service.ProductService;
import com.google.gson.Gson;

public class ProductServlet extends BaseServlet {
	
	/*
	 * �������л�ȡ�����б�ķ���
	 */
	public void getCategoryList(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//�����ݿ��������Ϣ�ļ���
		CategoryService service = new CategoryService();
		List<Category> categoryList = service.fingAllCategory();
		//�����Ϸ�װ��json��ʽ�ַ���
		Gson gson = new Gson();
		String json = gson.toJson(categoryList);
		//���ص�ǰ��ҳ��
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(json);
	}
	/*
	 * ��ȡ��ҳ����ʾ����Ʒ��Ϣ
	 */
	public void getIndexProduct(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		/*
		 * ���������Ʒ����
		 */
		ProductService service = new ProductService();
		List<Product> hotProductList = service.findHotProduct();
		/*
		 * ���������Ʒ����
		 */
		List<Product> newProductList = service.findNewProduct();
		request.setAttribute("hotProductList", hotProductList);
		request.setAttribute("newProductList", newProductList);
		request.getRequestDispatcher("index.jsp").forward(request, response);
	}
	/*
	 * ��ȡ��Ʒ��ϸ��Ϣ
	 */
	public void getProduct_info(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		String pid = request.getParameter("pid");
		ProductService service = new ProductService();
		Product product = service.getProduct_info(pid);
		request.setAttribute("product", product);
		request.getRequestDispatcher("product_info.jsp").forward(request, response);
	}
	/*
	 * ��ö�Ӧ�������Ʒ�б�
	 */
	public void getProductList(HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
		
		//��ҳ���ȡ�����Ϣ,��д�뵽request����
		String cid = request.getParameter("cid");
		request.setAttribute("cid", cid);
		/*
		 * ��װ��ҳ��ϢpageBean
		 */
		PageBean pageBean = new PageBean();
		//ÿҳ��ʾ����
		int pageContent = 12;
		pageBean.setPageContent(pageContent);
		//��ǰҳ
		int pageIndex;
		if(request.getParameter("pageIndex") == null) {
			pageIndex = 1;
		}else {
			pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		}
		pageBean.setPageIndex(pageIndex);
		//�ܼ�¼��
		ProductService service = new ProductService();
		int totalRecord = service.totalRecord(cid);
		pageBean.setTotalRecord(totalRecord);
		//��ҳ��
		int totalPage = (int) Math.ceil(1.0*totalRecord/pageContent);
		pageBean.setTotalPage(totalPage);
		//��pageBean����request����
		request.setAttribute("pageBean", pageBean);
		/*
		 * ����pageBean�������Ϣȥ���ݿ��в�����Ʒ��Ϣ
		 */
		List<Product> productList = service.findCategoryProduct(pageBean,cid);
		request.setAttribute("productList", productList);
		//ת����ҳ����ʾ
		request.getRequestDispatcher("product_list.jsp").forward(request, response);
	}
}

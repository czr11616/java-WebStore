package com.czr.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.czr.domain.Cart;
import com.czr.domain.CartItem;
import com.czr.domain.Order;
import com.czr.domain.OrderItem;
import com.czr.domain.User;
import com.czr.service.ProductService;
import com.czr.service.TransactService;

public class TransactionServlet extends BaseServlet{
	
	/*
	 * ���빺�ﳵ
	 */
	public void addToCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//��ȡ������
		String pid = request.getParameter("pid");
		String pimage = request.getParameter("pimage");
		String pname = request.getParameter("pname");
		double shop_price = Double.parseDouble(request.getParameter("shop_price"));
		int orderNum = Integer.parseInt(request.getParameter("orderNum"));
		double sum_price = shop_price * orderNum;
		//�жϹ��ﳵ�Ƿ���ڣ�û���򴴽�
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart == null) {
			cart = new Cart();
		}
		/*
		 * �ڼ��빺�ﳵ֮ǰ�жϹ��ﳵ���Ƿ���ڸ���Ʒ
		 */
		//������ڸ���Ʒ������ԭ��Ʒ������������������������ڣ���װ����Ʒ�����빺�ﳵ
		Map<String,CartItem> map = cart.getMap();
		if(map.containsKey(pid)) {
			map.get(pid).setOrderNum(map.get(pid).getOrderNum()+orderNum);
			cart.setSumMoney(cart.getSumMoney()+sum_price);
			map.get(pid).setSum_price(map.get(pid).getSum_price()+sum_price);
			request.getSession().setAttribute("cart", cart);
		}else {
			/*
			 * ��װcartItem����
			 */
			CartItem item = new CartItem();
			item.setPid(pid);
			item.setPimage(pimage);
			item.setPname(pname);
			item.setShop_price(shop_price);
			item.setOrderNum(orderNum);
			item.setSum_price(sum_price);
			//���빺�ﳵ
			map.put(pid, item);
			cart.setSumMoney(sum_price+cart.getSumMoney());
			request.getSession().setAttribute("cart", cart);
		}
		response.sendRedirect("cart.jsp");
	}
	/*
	 * �ӹ��ﳵ��ɾ��
	 */
	public void removeItem(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//��ȡ���ﳵ��Ҫɾ������Ʒid
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		String pid = request.getParameter("pid");
		//ɾ��
		cart.setSumMoney(cart.getSumMoney()-cart.getMap().get(pid).getSum_price());
		cart.getMap().remove(pid);
		//���·���session���в��ض���
		request.getSession().setAttribute("cart", cart);
		response.sendRedirect("cart.jsp");
	}
	/*
	 * ��չ��ﳵ
	 */
	public void clearCart(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		request.getSession().removeAttribute("cart");
		response.sendRedirect("cart.jsp");
	}
	/*
	 * �ύ����
	 */
	public void subOrder(HttpServletRequest request,HttpServletResponse response) throws IOException {
		
		//�жϹ��ﳵ���Ƿ�����Ʒ
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		if(cart == null) {
			response.sendRedirect("cart.jsp");
		}else {
			/*
			 * ��װorder����
			 */
			Order order = new Order();
			String oid = UUID.randomUUID().toString();
			String ordertime = new SimpleDateFormat("yyyy-MM-dd,HH:mm:ss").format(new Date());
			double total = cart.getSumMoney();
			int state = 0;
			User user = (User) request.getSession().getAttribute("user");
			order.setOid(oid);
			order.setOrdertime(ordertime);
			order.setTotal(total);
			order.setState(state);
			order.setUser(user);
			//��װorderItem���󲢷���order��
			List<OrderItem> orderItems = order.getOrderItems();
			Map<String, CartItem> itemMap = cart.getMap();
			for(String key:itemMap.keySet()) {
				CartItem cartItem = itemMap.get(key);
				OrderItem orderItem = new OrderItem();
				orderItem.setItemid(UUID.randomUUID().toString());
				orderItem.setCount(cartItem.getOrderNum());
				orderItem.setSubtotal(cartItem.getSum_price());
				orderItem.setProduct(new ProductService().getProduct_info(cartItem.getPid()));
				orderItem.setOrder(order);
				orderItems.add(orderItem);
			}
			//order�����װ��ϣ������ݱ��浽���ݿ���
			TransactService service = new TransactService();
			boolean flag = service.subOrder(order);
			//�����ύ�ɹ�����չ��ﳵ����ת������ҳ�棬ʧ���򷵻ع��ﳵ
			if(flag == true) {
				request.getSession().removeAttribute("cart");
				request.getSession().setAttribute("order", order);
				response.sendRedirect("order_info.jsp");
			}else {
				response.sendRedirect("cart.jsp");
			}
		}
	}
	/*
	 * ���Ҷ���
	 */
	public void findOrder(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		
		TransactService service = new TransactService();
		User user = (User) request.getSession().getAttribute("user");
		List<Order> orderList = service.findOrderList(user);
		request.setAttribute("orderList", orderList);
		request.getRequestDispatcher("order_list.jsp").forward(request, response);
	}
}
package com.czr.domain;

import java.util.ArrayList;
import java.util.List;

public class Order {
	
	private String oid;//�ö����Ķ�����
	private String ordertime;//�µ�ʱ��
	private double total;//�ö������ܽ��
	private int state;//����֧��״̬ 1�����Ѹ��� 0����δ����
	
	private String address;//�ջ���ַ
	private String name;//�ջ���
	private String telephone;//�ջ��˵绰
	
	private User user;//�ö��������ĸ��û�
	
	//�ö������ж��ٶ�����
	List<OrderItem> orderItems = new ArrayList<OrderItem>();

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
}

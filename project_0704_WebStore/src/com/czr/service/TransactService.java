package com.czr.service;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;

import com.czr.dao.TransactDao;
import com.czr.domain.Order;
import com.czr.domain.User;
import com.czr.utils.DataSourceUtils;

public class TransactService {
	
	/*
	 * ������orders���orderItem���������ʱ����ͬʱ�ɹ���������Ҫ�����������
	 */

	public boolean subOrder(Order order) {
		
		boolean flag = false;
		
		TransactDao dao = new TransactDao();
		try {
			//��������
			DataSourceUtils.startTransaction();
			//�涩��
			dao.addOrder(order);
			//�涩����
			dao.addOrderItem(order);
			//�ߵ��������ʾ�ύ�ɹ�
			flag = true;
			
		} catch (SQLException e) {
			//��������κ�һ��ִ��ʧ�ܣ���ع�����
			try {
				DataSourceUtils.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			//ʼ���ύ����
			try {
				DataSourceUtils.commitAndRelease();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return flag;
	}

	public List<Order> findOrderList(User user) {
		
		TransactDao dao = new TransactDao();
		List<Order> orderList = null;
		try {
			orderList =  dao.findOrderList(user);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return orderList;
	}

}

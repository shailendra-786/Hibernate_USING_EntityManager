package com.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import com.hibernate.entity.Customers;
import com.hibernate.entity.Orders;

public class JOINQUERY {
	
	public static void criteriaQ(EntityManager em) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);

		Root<Customers> customersRoot = criteriaQuery.from(Customers.class);
		Join<Customers, Orders> ordersJoin = customersRoot.join("orders",JoinType.INNER);

		criteriaQuery
		    .select(builder.array(customersRoot, ordersJoin))
		    .where(builder.equal(customersRoot.get("CustomerID"), ordersJoin.get("customers").get("CustomerID")));
		
//		criteriaQuery.select(builder.array(customersRoot, ordersJoin)).where(
//				builder.equal(customersRoot.get("CustomerID"), ordersJoin.get("customers").get("CustomerID")),
//				builder.equal(customersRoot.get("ContactName"), "Ana Trujillo"));


		List<Object[]> resultList = em.createQuery(criteriaQuery).getResultList();

		for (Object[] result : resultList) {
		    Customers customer = (Customers) result[0];
		    Orders order = (Orders) result[1];

		    // Now you have access to both the Customers and Orders objects from the join
		    System.out.println("Customer: " + customer.getCustomerName() + ", Order: " + order.getOrderID());
		}

	}
	
	public static void hqlQuery(EntityManager em) {
		// select c from Customers c inner join Orders o on c.CustomerID= o.CustomerID
				Query q = em.createQuery(
						"select c from Customers c inner join Orders o on c.CustomerID = o.customers.CustomerID where c.ContactName = :contactName");
				q.setParameter("contactName", "Ana Trujillo");
				List<Customers> li = q.getResultList();
				for (Customers c : li) {
					System.out.println(c.getOrders().get(0).getOrderDate());
				}
	}
	
	public static void withoutJoin(EntityManager em) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = builder.createQuery(Object[].class);

		Root<Customers> customersRoot = criteriaQuery.from(Customers.class);
		Root<Orders> ordersRoot = criteriaQuery.from(Orders.class);

		criteriaQuery
		    .select(builder.array(customersRoot, ordersRoot))
		    .where(builder.equal(customersRoot.get("CustomerID"), ordersRoot.get("customers").get("CustomerID")));

		List<Object[]> resultList = em.createQuery(criteriaQuery).getResultList();

		for (Object[] result : resultList) {
		    Customers customer = (Customers) result[0];
		    Orders order = (Orders) result[1];

		    // Now you have access to both the Customers and Orders objects from the join
		    System.out.println("Customer: " + customer.getCustomerName() + ", Order: " + order.getOrderID());
		}

	}

}

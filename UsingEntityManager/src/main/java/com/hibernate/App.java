package com.hibernate;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.hibernate.entity.Customers;

/**
 * Hello world!
 *
 */
@PersistenceContext
public class App {
	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("Used_Entity_Manager");

		EntityManager em = emf.createEntityManager();
		JOINQUERY.criteriaQ(em);

	}

	public void getEgByAggregator(EntityManager em) {
		Query q = em.createQuery(
				"select count(c.CustomerID) as counter , c.Country from Customers c group by c.Country order by counter,c.Country");
		List<Object[]> resultList = q.getResultList();

		for (Object[] result : resultList) {
			Long count = (Long) result[0];
			String country = (String) result[1];

			System.out.println("Count: " + count + ", Country: " + country);
		}
		// with cb
		CriteriaBuilder cb = em.getCriteriaBuilder();

		CriteriaQuery<Tuple> query = cb.createTupleQuery();

		Root<Customers> root = query.from(Customers.class);
		query.multiselect(cb.count(root.get("CustomerID")), root.get("Country"));
		query.groupBy(root.get("Country"));

		List<Tuple> results = em.createQuery(query).getResultList();

		for (Tuple result : results) {
			Long count = result.get(0, Long.class); // Get the count result
			String country = (String) result.get(1);
			System.out.println("Count: " + count + ", Country: " + country);
		}
	}
}

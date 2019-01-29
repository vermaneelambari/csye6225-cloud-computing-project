package com.cloud.webapp.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cloud.webapp.entity.User;


@Repository
public class UserDaoImpl implements UserDao {

	private EntityManager entityManager;

	@Autowired
	public UserDaoImpl(EntityManager theEntityManager) {
		entityManager = theEntityManager;
	}

	@Override
	public List<User> findAll() {

		Session currentSession = entityManager.unwrap(Session.class);

		Query<User> query = currentSession.createQuery("from User", User.class);

		List<User> users = query.getResultList();

		return users;
	}

	@Override
	public User findById(int id) {

		Session currentSession = entityManager.unwrap(Session.class);

		User user = currentSession.get(User.class, id);

		return user;
	}

	@Override
	public User findByEmail(String email) {
		
		Session currentSession = entityManager.unwrap(Session.class);

		Query<User> theQuery = currentSession.createQuery("from User where email=:theEmail", User.class);
		theQuery.setParameter("theEmail", email);
		User theUser = null;
		
		try {
			theUser = theQuery.getSingleResult();
		}
		catch (Exception e) {
			theUser = null;
		}
		
		return theUser;
	}

	@Override
	public User save(User user) {
		
		Session currentSession = entityManager.unwrap(Session.class);
		
		currentSession.saveOrUpdate(user);
		
		return user;

	}

	@Override
	public void delete(int id) {
		
		Session currentSession = entityManager.unwrap(Session.class);
		
		Query<User> query = currentSession.createQuery("delete from User where id=:userId");
		
		query.setParameter("userId", id);
		
		query.executeUpdate();

	}

}

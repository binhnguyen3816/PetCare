package com.binh.user.dao;

import com.binh.user.entity.User;
import com.binh.utils.BaseDAO;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class UserDAO implements BaseDAO<User, Long> {
    private SessionFactory sessionFactory;

    public UserDAO() {
        Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
        sessionFactory = configuration.buildSessionFactory();
    }

    @Override
    public User findById(Long aLong) {
        return null;
    }

    @Override
    public List<User> findAll() {
        List<User> Users = null;
        Session session = sessionFactory.openSession();
        try {
            Users = session.createQuery("from User", User.class).getResultList();
        } catch (Exception ex) {
            System.out.println("Error " + ex.getMessage());
            return null;
        } finally {
            session.close();
        }
        return Users;
    }

    @Override
    public User save(User User) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.persist(User);
            t.commit();
            return User;
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error " + ex.getMessage());
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public User update(User User) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            session.merge(User);
            t.commit();
            return User;
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error " + ex.getMessage());
            return null;
        } finally {
            session.close();
        }
    }

    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction t = session.beginTransaction();
        try {
            User User = session.find(User.class, id);
            if (User == null) {
                return;
            }
            session.remove(User);
            t.commit();
        } catch (Exception ex) {
            t.rollback();
            System.out.println("Error " + ex.getMessage());
        } finally {
            session.close();
        }
    }
}

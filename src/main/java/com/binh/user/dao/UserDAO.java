package com.binh.user.dao;

import com.binh.user.entity.User;
import com.binh.utils.HibernateFactory;
import com.binh.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Optional;

@Slf4j
@Stateless
public class UserDAO extends ImplementBaseDAO<User, Long> {

    public UserDAO() {
        super(User.class);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateFactory.getSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();
            return Optional.ofNullable(user);
        } catch (Exception e) {
            log.error("Not found user by email");
            return Optional.empty();
        }
    }


}

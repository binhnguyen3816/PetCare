package com.binh.criteria.dao;

import com.binh.criteria.entity.Criteria;
import com.binh.utils.HibernateFactory;
import com.binh.utils.ImplementBaseDAO;
import jakarta.ejb.Stateless;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

@Stateless
public class CriteriaDAO extends ImplementBaseDAO<Criteria, Long> {

    public CriteriaDAO() {
        super(Criteria.class);
    }

    public boolean existsByName(String name) {
        Session session = HibernateFactory.getSession();
        Query<Long> query = session.createQuery("SELECT COUNT(c) FROM Criteria c WHERE c.name = :name", Long.class);
        query.setParameter("name", name);
        Long count = query.getSingleResult();
        return count > 0;
    }

    public void saveAll(List<Criteria> criteriaListToImport) {
        criteriaListToImport.forEach(this::save);
    }
}

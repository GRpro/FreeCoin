package com.project.dao;

import com.project.businesslogic.Complaint;
import com.project.businesslogic.meta.ComplaintState;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ComplaintDAO implements CRUD<Complaint> {

    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long create(Complaint object) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(object);
        session.flush();
        return object.getId();
    }

    @Override
    public Complaint get(long id) {
        Session session = sessionFactory.getCurrentSession();
        return (Complaint) session.get(Complaint.class, id);
    }


    public List<Complaint> getByAdminId(long id, ComplaintState state) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Complaint.class, "c");
        criteria.createAlias("c.adminUser", "a");
        criteria.add(Restrictions.eq("a.id", id));
        criteria.add(Restrictions.eq("c.complaintState", state));
        criteria.addOrder(Order.desc("c.date"));
        return criteria.list();
    }

    public List<Complaint> getComplaintsOnJob(long jobId) {
        Session session = sessionFactory.getCurrentSession();
        return session.createQuery("from Complaint c where c.problemJob.id = :id ")
                .setParameter("id", jobId).list();
    }


    @Override
    public void update(Complaint object) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(object);
    }

    @Override
    public void delete(Complaint object) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(object);
    }
}

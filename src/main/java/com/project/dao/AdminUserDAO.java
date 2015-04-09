package com.project.dao;

import com.project.businesslogic.Complaint;
import com.project.businesslogic.meta.ComplaintState;
import com.project.businesslogic.user.AdminUser;
import com.project.security.CustomUserDetails;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Repository
@Transactional
public class AdminUserDAO implements CRUD<AdminUser> {
    
    private SessionFactory sessionFactory;
    
    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public long create(AdminUser object) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(object);
        return object.getId();
    }

    @Transactional
    public void realUpdate(Long realUserId, AdminUser tmpUser, CustomUserDetails customUserDetails){
        Session session = sessionFactory.getCurrentSession();
        AdminUser realUser = (AdminUser) session.get(AdminUser.class, realUserId);
        realUser.setSnf(tmpUser.getSnf());
        customUserDetails.setSnf(tmpUser.getSnf());
        customUserDetails.setPassword(tmpUser.getPassword());
        realUser.setPassword(tmpUser.getPassword());
        realUser.setBirthday(tmpUser.getBirthday());
        if (tmpUser.getImage()!=null) {
            if (realUser.getImage()==null) realUser.setImage(tmpUser.getImage());
            else realUser.getImage().setImage(tmpUser.getImage().getImage());
        }
        session.merge(realUser);
    }

    @Override
    @Transactional(readOnly = true)
    public AdminUser get(long id) {
        Session session = sessionFactory.getCurrentSession();
        return (AdminUser) session.get(AdminUser.class, id);
    }

    /**
     * This method returns AdminUser with least number ACTIVE complaints
     * @return AdminUser
     */
    @Transactional(readOnly = true)
    public AdminUser getFreeAdmin() {
        Session session = sessionFactory.getCurrentSession();
        List<AdminUser> adminUsers = session.createCriteria(AdminUser.class).list();
            AdminUser adminUser = null;
            long min = Integer.MAX_VALUE;
            long temp;
            for (AdminUser admin : adminUsers) {
                if ((temp = numOfActiveComplaints(admin.getComplaints())) < min) {
                    min = temp;
                    adminUser = admin;
                }
            }
            return adminUser;
    }

    private long numOfActiveComplaints(List<Complaint> complaints) {
        int num = 0;
        for (Complaint c : complaints ) {
            if (c.getComplaintState().equals(ComplaintState.ACTIVE))
                num++;
        }
        return num;
    }


    @Transactional(readOnly = true)
    public AdminUser getByEmail(String email) {
        Session session = sessionFactory.getCurrentSession();
        return (AdminUser) session.createQuery("from AdminUser d where d.email = :email")
                .setParameter("email", email).uniqueResult();
    }

    @Override
    public void update(AdminUser object) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(object);
    }

    @Override
    public void delete(AdminUser object) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(object);
    }
}

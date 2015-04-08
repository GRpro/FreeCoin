package com.project.services;

import com.project.businesslogic.Complaint;
import com.project.businesslogic.Job;
import com.project.businesslogic.meta.ComplaintState;
import com.project.businesslogic.user.AdminUser;
import com.project.businesslogic.user.User;
import com.project.dao.AdminUserDAO;
import com.project.dao.ComplaintDAO;
import com.project.dao.UserDAO;
import com.project.exceptions.NoAdminRegisteredYetException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ComplaintService {

    @Autowired
    private ComplaintDAO complaintDAO;
    @Autowired
    private JobService jobService;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private AdminUserDAO adminUserDAO;

    public long create(long userId, long jobId, String text) {
        Job job = jobService.get(jobId);
        User user = userDAO.get(userId);

        Complaint complaint = new Complaint();
        complaint.setProblemJob(job);
        complaint.setUser(user);
        complaint.setText(text);
        complaint.setDate(new Date());
        complaint.setComplaintState(ComplaintState.ACTIVE);
        AdminUser adminUser = adminUserDAO.getFreeAdmin();
        if (adminUser == null)
            throw new NoAdminRegisteredYetException();
        complaint.setAdminUser(adminUser);
        return complaintDAO.create(complaint);
    }

    public Complaint get(long id) {
        return complaintDAO.get(id);
    }

    public List<Complaint> getComplaintsOnJob(long jobId) {
        return complaintDAO.getComplaintsOnJob(jobId);
    }

    public List<Complaint> getByAdminId(long id, ComplaintState state) {
        return complaintDAO.getByAdminId(id, state);
    }

    public void update(Complaint object) {
        complaintDAO.update(object);
    }

    public void delete(Complaint object) {
        complaintDAO.delete(object);
    }






}

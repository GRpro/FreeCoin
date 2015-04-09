package com.project.businesslogic;

import com.project.businesslogic.meta.ComplaintState;
import com.project.businesslogic.user.AdminUser;
import com.project.businesslogic.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "complaint")
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    
    @ManyToOne
    private User user;
    
    private String text;

    @Enumerated(EnumType.STRING)
    private ComplaintState complaintState;

    /**
     * Admin who responsible for solving this complaint
     */
    @ManyToOne
    private AdminUser adminUser;
    
    @ManyToOne
    private Job problemJob;

    @Temporal(TemporalType.DATE)
    private Date date;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Job getProblemJob() {
        return problemJob;
    }

    public void setProblemJob(Job problemJob) {
        this.problemJob = problemJob;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AdminUser getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ComplaintState getComplaintState() {
        return complaintState;
    }

    public void setComplaintState(ComplaintState complaintState) {
        this.complaintState = complaintState;
    }

    @Override
    public String toString() {
        return "Complaint{" +
                "id=" + id +
                ", user=" + user +
                ", text='" + text + '\'' +
                ", complaintState=" + complaintState +
                ", adminUser=" + adminUser +
                ", problemJob=" + problemJob +
                ", date=" + date +
                '}';
    }
}

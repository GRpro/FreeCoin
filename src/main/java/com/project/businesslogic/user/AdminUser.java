package com.project.businesslogic.user;

import com.project.businesslogic.Complaint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin_user")
public class AdminUser extends User {

    @OneToMany(mappedBy = "problemJob", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Complaint> complaints = new ArrayList<>();

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }
}

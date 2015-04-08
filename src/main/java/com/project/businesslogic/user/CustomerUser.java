package com.project.businesslogic.user;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customer_user")
public class CustomerUser extends User {

    private double rating;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}

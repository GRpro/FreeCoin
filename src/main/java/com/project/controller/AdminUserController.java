package com.project.controller;

import com.project.businesslogic.Complaint;
import com.project.businesslogic.Image;
import com.project.businesslogic.Job;
import com.project.businesslogic.meta.ComplaintState;
import com.project.businesslogic.meta.UserType;
import com.project.businesslogic.user.AdminUser;
import com.project.businesslogic.user.CustomerUser;
import com.project.businesslogic.user.DeveloperUser;
import com.project.dao.AdminUserDAO;
import com.project.security.CustomUserDetails;
import com.project.services.ComplaintService;
import com.project.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.event.ComponentListener;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usr/admin")
public class AdminUserController {

    private AdminUserDAO adminUserDAO;
    private JobService jobService;
    private ComplaintService complaintService;

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    public void setAdminUserDAO(AdminUserDAO adminUserDAO) {
        this.adminUserDAO = adminUserDAO;
    }

    @Autowired
    public void setComplaintService(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }



    @RequestMapping(value = "/profileDetails", method = RequestMethod.GET)
    public ModelAndView redirectToProfileDetails(@RequestParam(value = "userId") Long userId) {
        ModelAndView modelAndView = new ModelAndView("private/admin/profile_details");
        AdminUser adminUser = adminUserDAO.get(userId);
        modelAndView.addObject("user", adminUser);
        return modelAndView;
    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute AdminUser adminUser) {
        adminUser.setUserType(UserType.ADMIN);
        System.out.println(adminUser);
        try {
            long id = adminUserDAO.create(adminUser);
            ModelAndView modelAndView =  new ModelAndView("public/on_success");
            modelAndView.addObject("titleMessage", "Admin register success");
            modelAndView.addObject("successMessage", "Admin user was successfully registered!");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView =  new ModelAndView("public/admin_register");
            modelAndView.addObject("error", "Your attempt to register has failed! Please try again");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView adminUserProfile(Principal principal) {
        try {
            String email = principal.getName();
            AdminUser adminUser = adminUserDAO.getByEmail(email);
            long userId = adminUser.getId();

            List<Complaint> activeCompliants = complaintService.getByAdminId(adminUser.getId(), ComplaintState.ACTIVE);
            List<Complaint> solvedCompliants = complaintService.getByAdminId(adminUser.getId(), ComplaintState.SOLVED);

            for (Complaint c : activeCompliants) {
                System.out.println(c);
            }
            ModelAndView modelAndView = new ModelAndView("private/admin/profile");
            modelAndView.addObject("activeCompliants", activeCompliants);
            modelAndView.addObject("solvedCompliants", solvedCompliants);
            modelAndView.addObject("adminUser", adminUser);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("public/error");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute AdminUser tmpUser,
                             @RequestParam(value = "lastId") Long lastId,
                             @RequestParam(value = "userImage") MultipartFile userImage,
                             Principal principal) {
        try {
            if (userImage.getSize()!=0) {
                Image image = new Image();
                image.setImage(userImage.getBytes());
                tmpUser.setImage(image);
            }
            adminUserDAO.realUpdate(lastId, tmpUser, (CustomUserDetails) ((Authentication) principal).getPrincipal());

            ModelAndView modelAndView =  new ModelAndView("public/on_success");
            modelAndView.addObject("titleMessage", "Profile edit success");
            modelAndView.addObject("successMessage", "Your profile was successfully edited!");
            return modelAndView;
        } catch (IOException e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("public/error");
            return modelAndView;
        }
    }
}

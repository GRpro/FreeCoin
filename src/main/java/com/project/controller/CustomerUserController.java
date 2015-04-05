package com.project.controller;

import com.project.businesslogic.Job;
import com.project.businesslogic.meta.UserType;
import com.project.businesslogic.user.CustomerUser;
import com.project.dao.CustomerUserDAO;
import com.project.security.CustomUserDetails;
import com.project.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/usr/customer")
public class CustomerUserController {

    private CustomerUserDAO customerUserDAO;
    private JobService jobService;

    @Autowired
    public void setJobService(JobService jobService) {
        this.jobService = jobService;
    }

    @Autowired
    public void setCustomerUserDAO(CustomerUserDAO customerUserDAO) {
        this.customerUserDAO = customerUserDAO;
    }

    @RequestMapping(value = "/profileDetail", method = RequestMethod.GET)
    public ModelAndView redirectToProfileDetail(@RequestParam(value = "userId") Long userId) {
        ModelAndView modelAndView = new ModelAndView("private/customer/profile_detail");
        CustomerUser customerUser = customerUserDAO.get(userId);
        modelAndView.addObject("user", customerUser);
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView edit(@ModelAttribute CustomerUser tmpUser,
                             @RequestParam(value = "lastId") Long lastId,
                             @RequestParam(value = "userImage") MultipartFile userImage,
                             Principal principal) {
        try {
            if (userImage.getSize()!=0) tmpUser.setImage(userImage.getBytes());
            CustomUserDetails customUserDetails = (CustomUserDetails) ((Authentication) principal).getPrincipal();
            customerUserDAO.realUpdate(lastId, tmpUser, customUserDetails);

            ModelAndView modelAndView =  new ModelAndView("public/success/on_register_success");
            modelAndView.addObject("titleMessage", "Profile edit success");
            modelAndView.addObject("successMessage", "Your profile was successfully edited!");
            return modelAndView;
        } catch (IOException e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("public/error/error");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView create(@ModelAttribute CustomerUser customerUser) {
        customerUser.setUserType(UserType.CUSTOMER);
        System.out.println(customerUser);
        try {
            long id = customerUserDAO.create(customerUser);
            ModelAndView modelAndView =  new ModelAndView("public/success/on_register_success");
            modelAndView.addObject("titleMessage", "Customer register success");
            modelAndView.addObject("successMessage", "Customer user was successfully registered!");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView =  new ModelAndView("public/customer_register");
            modelAndView.addObject("error", "Your attempt to register has failed! Please try again");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ModelAndView developerUserProfile(Principal principal) {
        try {
            String email = principal.getName();
            CustomerUser customerUser = customerUserDAO.getByEmail(email);
            long userId = customerUser.getId();
            List<Job> jobs = jobService.getByCustomerId(userId, 0, 20);
            ModelAndView modelAndView = new ModelAndView("private/customer/profile");
            modelAndView.addObject("jobs", jobs);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("customerUser", customerUser);
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("public/error/error");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ModelAndView redirectToMessages() {
        try {
            ModelAndView modelAndView = new ModelAndView("private/customer/messages");
            return modelAndView;
        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("public/error/error");
            return modelAndView;
        }
    }

    //not my code - from SteakOverFlow
    @RequestMapping(value = "/image/{userId}"/*, produces = MediaType.IMAGE_JPEG_VALUE*/)
    public ResponseEntity<byte[]> getCustomerImage(@PathVariable("userId") Long userId) throws IOException {

        CustomerUser customerUser = customerUserDAO.get(userId);
        byte[] imageContent = customerUser.getImage();
        if (imageContent==null) imageContent = customerUserDAO.getDefaultUser().getImage();
        final HttpHeaders headers = new HttpHeaders();
        //headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

}

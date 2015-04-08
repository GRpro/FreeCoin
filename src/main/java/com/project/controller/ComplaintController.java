package com.project.controller;

import com.project.businesslogic.Complaint;
import com.project.businesslogic.meta.ComplaintState;
import com.project.exceptions.NoAdminRegisteredYetException;
import com.project.services.ComplaintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    @RequestMapping(value = "/job/complaint", method = RequestMethod.POST)
    public ModelAndView addComplaint(@RequestParam("userId") long userId,
                                     @RequestParam("jobId") long jobId,
                                     @RequestParam("complaintText") String text) {
        ModelAndView modelAndView = null;
        try {
            complaintService.create(userId, jobId, text);
            modelAndView =  new ModelAndView("public/on_success");
            modelAndView.addObject("titleMessage", "complaint success");
            modelAndView.addObject("successMessage", "Complaint was successfully set");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView =  new ModelAndView("public/error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/job/complaint/solve", method = RequestMethod.POST)
    public ModelAndView setCompliantSolved(@RequestParam("compliantId") long compliantId) {
        ModelAndView modelAndView = null;
        try {
            Complaint complaint = complaintService.get(compliantId);
            complaint.setComplaintState(ComplaintState.SOLVED);
            complaintService.update(complaint);
            modelAndView =  new ModelAndView("public/on_success");
            modelAndView.addObject("titleMessage", "complaint success");
            modelAndView.addObject("successMessage", "Complaint was successfully marked as solved");
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView =  new ModelAndView("public/error");
        }
        return modelAndView;
    }

    @RequestMapping(value = "/job/complaint/options", method = RequestMethod.POST)
    public ModelAndView complaintOptions(@RequestParam("compliantId") long compliantId) {
        ModelAndView modelAndView = null;
        try {
            Complaint complaint = complaintService.get(compliantId);

            modelAndView =  new ModelAndView("private/admin/compliant_details");
            modelAndView.addObject("complaint", complaint);
        } catch (Exception e) {
            e.printStackTrace();
            modelAndView =  new ModelAndView("public/error");
        }
        return modelAndView;
    }

}

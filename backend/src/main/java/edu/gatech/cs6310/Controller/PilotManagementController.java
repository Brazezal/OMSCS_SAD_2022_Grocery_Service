package edu.gatech.cs6310.Controller;

import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Entity.Pilot;
import edu.gatech.cs6310.Repo.*;
import edu.gatech.cs6310.utility.WebMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/pilotManagement")
public class PilotManagementController {
    Logger logger = LogManager.getLogger(AdminController.class);


    @Autowired
    @Qualifier("customerRepo")
    private CustomerRepo customerRepo;
    @Autowired
    @Qualifier("pilotRepo")
    private PilotRepo pilotRepo;

    @Autowired
    @Qualifier("storeRepo")
    private StoreRepo storeRepo;

    @Autowired
    @Qualifier("itemRepo")
    private ItemRepo itemRepo;

    @Autowired
    @Qualifier("droneRepo")
    private DroneRepo droneRepo;


    @Autowired
    @Qualifier("roleRepo")
    private RoleRepo roleRepo;


    @PostMapping("/addPilot")
    public String addPilot(Pilot pilot, Model model) {
        if (pilotRepo.findPilotByLicenseID(pilot.getLicenseID()) == null) {
            pilotRepo.save(pilot);
            logger.info(WebMessages.SuccessMessage(
                    "Pilot_" + pilot.getAccountName() + " added!"));
            model.addAttribute("Message", WebMessages.SuccessMessage(
                    "Pilot_" + pilot.getAccountName() + " added!"));
        } else {
            logger.info(WebMessages.ErrorMessage(
                    "Pilot_" + pilot.getAccountName() + " exist!"));
            model.addAttribute("Message", WebMessages.ErrorMessage(
                    "Pilot_" + pilot.getAccountName() + " exist!"));
        }

        List<Pilot> pilots = pilotRepo.findAll();
        model.addAttribute("pilots", pilots);
        model.addAttribute("newPilot", new Pilot());
        return "admin_pilotList";
    }


    @PostMapping("/deletePilot/{licienceId}")
    public String deletePilot(@PathVariable("licienceId") String licienceId, Model model) {
        if (pilotRepo.findPilotByLicenseID(licienceId) != null) {
            Pilot pilot = pilotRepo.findPilotByLicenseID(licienceId);
            pilotRepo.delete(pilot);
                logger.info(WebMessages.SuccessMessage(
                        "Pilot_" + pilot.getAccountName() + " deleted!"));
                model.addAttribute("Message", WebMessages.SuccessMessage(
                        "Pilot_" + pilot.getAccountName() + " deleted!"));
        }
        List<Pilot> pilots = pilotRepo.findAll();
        model.addAttribute("pilots", pilots);
        model.addAttribute("newPilot", new Pilot());
        return "admin_pilotList";
    }

//    @PostMapping("/editCustomer/{customerId}")
//    public String editStore(@PathVariable("customerId") String customerId, Model model) {
//        Customer currentCustomer = customerRepo.findCustomerByCustomerId(customerId);
//
//        List<Customer> customers = customerRepo.findAll();
//
//        List<Pilot> pilots = pilotRepo.findAll();
//        model.addAttribute("pilots", pilots);
//        model.addAttribute("newCustomer", new Pilot());
//        return "admin_pilotList";
//    }


}
package edu.gatech.cs6310.Controller;


import edu.gatech.cs6310.Entity.*;
import edu.gatech.cs6310.Repo.*;
import edu.gatech.cs6310.Service.GroceryStoreService;
import edu.gatech.cs6310.Service.PilotService;
import edu.gatech.cs6310.utility.Messages;
import edu.gatech.cs6310.utility.WebMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pilot")
public class PilotController {

    Logger logger = LogManager.getLogger(PilotController.class);

    @Autowired
    @Qualifier("customerRepo")
    private CustomerRepo customerRepo;

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
    @Qualifier("pilotRepo")
    private PilotRepo pilotRepo;

    @Autowired
    @Qualifier("orderRepo")
    private OrderRepo orderRepo;

    @GetMapping("/fail")
    public String loginFailPage() {
        return "login_fail";
    }

    @Autowired
    private PilotService pilotService;

    @Autowired
    private GroceryStoreService storeService;

    @GetMapping("/login_authen")
    public String jumpToLogin(Model model) {
        model.addAttribute("pilot", new Pilot());
        return "pilot_login";
    }


    @GetMapping("/registration")
    public String pilotRegisteration(Model model) {
        model.addAttribute("pilot", new Pilot());
        model.addAttribute("role", new Role());
        return "pilot_signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(Pilot pilot, Model model) {

        if (pilotRepo.findPilotByAccountName(pilot.getAccountName()) != null) {
            logger.error(WebMessages.ErrorMessage("pilot_identifier_already_exists"));
        model.addAttribute("Message", WebMessages.ErrorMessage("pilot_identifier_already_exists"));
        model.addAttribute("pilot", new Pilot());
            return "pilot_signup_form";

        } else if (pilotRepo.findPilotByLicenseID(pilot.getLicenseID()) != null) {
            logger.error(WebMessages.ErrorMessage("pilot_license_already_exists"));
        model.addAttribute("Message", WebMessages.ErrorMessage("pilot_license_already_exists"));
        model.addAttribute("pilot", new Pilot());
            return "pilot_signup_form";
        }else {

        pilotRepo.save(pilot);
        logger.info(WebMessages.SuccessMessage("Pilot registration success!"));
        model.addAttribute("Message", "Success!");
        }
        model.addAttribute("pilot", new Pilot());

        return "pilot_login";
    }

    @PostMapping("/process_login")
    public String processLogin(Pilot pilot, Model model) {
        Pilot authPilot = pilotRepo.findPilotByLicenseID(pilot.getLicenseID());
        if (authPilot == null || !pilot.getPassword().equals(authPilot.getPassword())) {
            logger.error(WebMessages.ErrorMessage("Pilot login fail!"));
            return "login_fail";
        }
        logger.info(WebMessages.SuccessMessage("Pilot_" + authPilot.getAccountName() + " login success!"));
        Drone currDrone = pilot.getDrone();
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("stores", stores);
        model.addAttribute("newDrone", new Drone());
        model.addAttribute("newPilot", new Pilot());
        model.addAttribute("currDrone", currDrone);
        model.addAttribute("pilot", authPilot);
        return "pilot_main";

    }

    @PostMapping("/editPilotInfo/{pilotId}")
    public String pilotInfoEdition(@PathVariable("pilotId") String pilotLicenseId,
                                   Pilot newPilot, Model model) {
        Pilot oldPilot = pilotRepo.findPilotByLicenseID(pilotLicenseId);
        if (newPilot.getAccountName()!=""){
            oldPilot.setAccountName(newPilot.getAccountName());
        }
        if (!newPilot.getPassword().equals("New Password")) {
            oldPilot.setPassword(newPilot.getPassword());
        }
        if (newPilot.getFirstName() != "") {
            oldPilot.setFirstName(newPilot.getFirstName());
        }
        if (newPilot.getLastName() != "") {
            oldPilot.setLastName(newPilot.getLastName());
        }
        if (newPilot.getPhoneNumber() != "") {
            oldPilot.setPhoneNumber(newPilot.getPhoneNumber());
        }
        pilotRepo.save(oldPilot);
        logger.info(WebMessages.SuccessMessage("Pilot profile changed Completed!"));
        Drone currDrone = oldPilot.getDrone();
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("stores", stores);
        model.addAttribute("newDrone", new Drone());
        model.addAttribute("newPilot", new Pilot());
        model.addAttribute("currDrone", currDrone);
        model.addAttribute("pilot", oldPilot);
        model.addAttribute("Message",WebMessages.SuccessMessage("Pilot profile changed Completed!"));
        return "pilot_main";

    }
    @PostMapping("/flyDrone/{pilotLicenceId}/{storeName}")
    public String selectDrone(@RequestParam("droneId") int droneId,
                              @PathVariable("pilotLicenceId") String pilotLicenceId,
                              @PathVariable("storeName") String storeName,
                              Model model) {
        List<GroceryStore> stores = storeRepo.findAll();
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        Pilot pilot = pilotRepo.findPilotByLicenseID(pilotLicenceId);
        Drone currDrone = droneRepo.findDroneByStoreAndDroneId(store, droneId);
        if (pilot.getDrone() != null) {
            if (!pilot.getDrone().equals(currDrone)) {
                storeService.flyDrone(currDrone, pilot);
                logger.info("Pilot_" + pilot.getAccountName() + " is assigned with drone_" + droneId
                        + "from store_" + storeName);
            }
        }
        storeService.flyDrone(currDrone, pilot);
        logger.info("Pilot_" + pilot.getAccountName() + " is assigned with drone_" + droneId
                + "from store_" + storeName);

        model.addAttribute("currDrone", currDrone);
        model.addAttribute("newDrone", new Drone());
        model.addAttribute("pilot", pilot);
        model.addAttribute("newPilot", new Pilot());

        model.addAttribute("stores", stores);
        model.addAttribute("errorMessage", "Change Complete!");
        return "pilot_main";
    }


}

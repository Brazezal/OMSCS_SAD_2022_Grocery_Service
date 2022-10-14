package edu.gatech.cs6310.Controller;


import edu.gatech.cs6310.Entity.*;
import edu.gatech.cs6310.Repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
@Controller
@RequestMapping("/admin")
public class AdminController {

    Logger logger = LogManager.getLogger(AdminController.class);

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

    @GetMapping("/admin/fail")
    public String loginFailPage(){
        return "login_fail";
    }


    @GetMapping("/login_authen")
    public String jumpToLogin(Model model){
        model.addAttribute("user", new Customer());
        return "admin_login";
    }

    @PostMapping("/process_login")
    public String processLogin(Customer customer){
        if (customer.getCustomerId().toUpperCase(Locale.ROOT).equals("ADMIN")&&customer.getPassword().toUpperCase(Locale.ROOT).equals("ADMIN")){
        logger.info("Admin login success!");
        return "admin_success";
        }else {
            logger.info("Admin login fail!");
            return "login_fail";
        }
    }

    @GetMapping("/main")
    public String viewAdminMain(Model model){
        List<GroceryStore>stores =storeRepo.findAll();
        List<Customer> customers=customerRepo.findAll();

        model.addAttribute("stores", stores);
        model.addAttribute("customers", customers);
        return "admin_main";
    }
    @GetMapping("/storeManagement")
    public String viewStoreManagePage(Model model){
        List<GroceryStore> stores =storeRepo.findAll();
        model.addAttribute("newStore", new GroceryStore());
        model.addAttribute("newDrone", new Drone());
        model.addAttribute("newItem", new Item());
        model.addAttribute("stores", stores);
        return "admin_store_manage";
    }
    @GetMapping("/customerManagement")
    public String viewCustomerManagePage(Model model){
        List<Customer> customers = customerRepo.findAll();

        model.addAttribute("customers", customers);

        model.addAttribute("newCustomer", new Customer());
        return "admin_customerList";
    }
    @GetMapping("/pilotManagement")
    public String viewPilotManagePage(Model model){

        List<Pilot> pilots = pilotRepo.findAll();

        model.addAttribute("pilots", pilots);

        model.addAttribute("newPilot", new Pilot());
        return "admin_pilotList";
    }
    @GetMapping("/orderManagement")
    public String viewOrderManagePage(Model model){

        List<OrderEntity> orders=orderRepo.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("newOrder", new OrderEntity());
        return "admin_orderList";
    }






}

package edu.gatech.cs6310.Controller;

import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Entity.Drone;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Item;
import edu.gatech.cs6310.Repo.*;
import edu.gatech.cs6310.utility.WebMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/customerManagement")
public class CustomerManagementController {

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
    @Qualifier("roleRepo")
    private RoleRepo roleRepo;


    @PostMapping("/addCustomer")
    public String addCustomer(Customer customer, Model model) {
        if (customerRepo.findCustomerByCustomerId(customer.getCustomerId()) == null) {
            customerRepo.save(customer);
            logger.info(WebMessages.SuccessMessage("Customer_" + customer.getCustomerId() + " added!"));
            model.addAttribute("Message",
                    WebMessages.SuccessMessage("Customer_" + customer.getCustomerId() + " added!"));
        } else {
            logger.info(WebMessages.ErrorMessage("Customer exists!"));
            model.addAttribute("Message",
                    WebMessages.ErrorMessage("Customer exists!"));
        }
        List<Customer> customers = customerRepo.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("newCustomer", new Customer());
        return "admin_customerList";
    }

    @PostMapping("/deleteCustomer/{customerId}")
    public String deleteStore(@PathVariable("customerId") String customerId, Model model) {
        if (customerRepo.findCustomerByCustomerId(customerId) != null) {
            Customer customer = customerRepo.findCustomerByCustomerId(customerId);
            customerRepo.delete(customer);
            logger.info(WebMessages.SuccessMessage("Customer_" + customerId + " deleted!"));
        }
        List<Customer> customers = customerRepo.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("newCustomer", new Customer());
        return "admin_customerList";
    }
//        @PostMapping("/editCustomer/{customerId}")
//        public String editStore(@PathVariable("customerId") String customerId, Model model) {
//             Customer currentCustomer = customerRepo.findCustomerByCustomerId(customerId);
//
//                List<Customer> customers = customerRepo.findAll();
//                model.addAttribute("customers", customers);
//                model.addAttribute("currentCustomer",currentCustomer);
//                model.addAttribute("newCustomer", new Customer());
//                return "admin_customerList";
//        }


}
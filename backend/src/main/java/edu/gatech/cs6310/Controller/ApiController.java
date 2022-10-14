package edu.gatech.cs6310.Controller;


import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Role;
import edu.gatech.cs6310.Repo.CustomerRepo;
import edu.gatech.cs6310.Repo.ItemRepo;
import edu.gatech.cs6310.Repo.RoleRepo;
import edu.gatech.cs6310.Repo.StoreRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class ApiController {

    Logger logger = LogManager.getLogger(AdminController.class);

    @Autowired
    @Qualifier("customerRepo")
    private CustomerRepo customerRepo;

    @Autowired
    @Qualifier("storeRepo")
    private StoreRepo storeRepo;

//GO to main page
    @GetMapping("")
    public String viewHomePage(){
        return "index";
    }
//Customer login and registration
    @GetMapping("register")
    public String showSignUpform(Model model){
        model.addAttribute("customer",new Customer());
        model.addAttribute("role",new Role());
        return "customer_signup_form";
    }

    @PostMapping("process_register")
    public String processRegister(Customer customer,Model model){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (customerRepo.findCustomerByCustomerId(customer.getCustomerId())==null){
        String password=customer.getPassword();
        String encodedPassword = encoder.encode(password);
        customer.setPassword(encodedPassword);
        customer.setRating(5);
        customerRepo.save(customer);
        logger.info("Customer register success!");
        return "user_register_success";
        }
        model.addAttribute("customer",new Customer());
        model.addAttribute("role",new Role());
        logger.error("Customer Id already exist!");
        return "customer_signup_form";

    }

    @GetMapping("customer_main")
    public String viewUserList( Model model){
        List<GroceryStore> stores=storeRepo.findAll();
        model.addAttribute("stores", stores);
        logger.info("Customer login success!");
        return "customer_main";
    }

}

package edu.gatech.cs6310.Controller;

import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.OrderEntity;
import edu.gatech.cs6310.Entity.Pilot;
import edu.gatech.cs6310.Repo.*;
import edu.gatech.cs6310.utility.WebMessages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/orderManagement")
public class OrderManagementController {
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
    @Qualifier("orderRepo")
    private OrderRepo orderRepo;


    @PostMapping("/addOrder/{storeName}")
    public String addOrder(OrderEntity order,
                           @PathVariable("storeName") String storeName, Model model) {
        if (orderRepo.findOrdersByStoreAndOrderName(storeRepo.findStoreByStoreName(storeName),
                order.getOrderName()) == null) {
            orderRepo.save(order);
            logger.info(WebMessages.SuccessMessage("Order_" +
                    order.getOrderName() + " added!"));
            model.addAttribute("Message",
                    WebMessages.SuccessMessage("Order_" + order.getOrderName() + " added!"));
        }else {
                logger.info(WebMessages.ErrorMessage("Order_" +
                        order.getOrderName() + " exists!"));
                model.addAttribute("Message",
                        WebMessages.ErrorMessage("Order_" + order.getOrderName() + " exists!"));
        }
        List<OrderEntity> orders = orderRepo.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("newOrder", new OrderEntity());
        return "admin_orderList";
    }

    @PostMapping("/deleteOrder/{orderName}/{storeName}")
    public String deletePilot(@PathVariable("orderName") String orderName,
                              @PathVariable("storeName") String storeName, Model model) {
        GroceryStore oldStore = storeRepo.findStoreByStoreName(storeName);
        OrderEntity oldOrder = orderRepo.findOrdersByStoreAndOrderName(oldStore, orderName);
        if (oldOrder != null) {
            orderRepo.delete(oldOrder);
                logger.info(WebMessages.SuccessMessage("Order_" +
                        oldOrder.getOrderName() + " deleted"));
                model.addAttribute("Message",
                        WebMessages.SuccessMessage("Order_" + oldOrder.getOrderName() + " deleted!"));
        }
        List<OrderEntity> orders = orderRepo.findAll();
        model.addAttribute("orders", orders);
        model.addAttribute("newOrder", new OrderEntity());
        return "admin_orderList";
    }





}
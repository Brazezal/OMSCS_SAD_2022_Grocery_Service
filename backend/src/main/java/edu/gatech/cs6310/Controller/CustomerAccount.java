package edu.gatech.cs6310.Controller;


import edu.gatech.cs6310.Entity.*;
import edu.gatech.cs6310.Repo.*;
import edu.gatech.cs6310.Service.CustomerService;
import edu.gatech.cs6310.utility.WebMessages;
import net.bytebuddy.utility.RandomString;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerAccount {

    //Logger function from Yanbo
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
    @Qualifier("lineRepo")
    private LineRepo lineRepo;
    @Autowired
    @Qualifier("orderRepo")
    private OrderRepo orderRepo;


    @Autowired
    @Qualifier("roleRepo")
    private RoleRepo roleRepo;

    @GetMapping("/customer")
    public String viewHomePage() {
        return "customer_main";
    }


    //Customer account and order display

    @PostMapping("/account")
    public String viewAccountDetail(
            @AuthenticationPrincipal CustomUserDetails customerUserDetails, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerUserDetails.getUsername());
        model.addAttribute("customer", customer);
        model.addAttribute("newCustomer", new Customer());
        return "customer_account";
    }

    @PostMapping("/deposit/{customerId}")
    public String customerDepositCredit(@PathVariable("customerId") String customerId,
                                        Customer newCustomer, Model model) {
        Customer oldCustomer = customerRepo.findCustomerByCustomerId(customerId);
        oldCustomer.setRestCredits(newCustomer.getRestCredits() + oldCustomer.getRestCredits());
        customerRepo.save(oldCustomer);
        model.addAttribute("customer", oldCustomer);
        model.addAttribute("customerId", oldCustomer.getCustomerId());
        model.addAttribute("newCustomer", new Customer());
        logger.info(WebMessages.SuccessMessage("Customer_" + customerId +
                ". reload: " + newCustomer.getRestCredits()));
        model.addAttribute("Message", "Congratulations! " +
                "You got " + newCustomer.getRestCredits() + " credits");
        return "customer_account";
    }

    @PostMapping("/editCustomerInfo/{customerId}")
    public String customerInfoEdition(@PathVariable("customerId") String customerId,
                                      Customer newCustomer, Model model) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Customer oldCustomer = customerRepo.findCustomerByCustomerId(customerId);
        if (newCustomer.getCustomerId()!=("")){
            oldCustomer.setCustomerId(newCustomer.getCustomerId());
        }
        if (!newCustomer.getPassword().equals("New Password")) {
            oldCustomer.setEncodedPassword(newCustomer.getPassword());
        }
        if (newCustomer.getFirstName() != "") {
            oldCustomer.setFirstName(newCustomer.getFirstName());
        }
        if (newCustomer.getLastName() != "") {
            oldCustomer.setLastName(newCustomer.getLastName());
        }
        if (newCustomer.getPhoneNumber() != "") {
            oldCustomer.setPhoneNumber(newCustomer.getPhoneNumber());
        }
        customerRepo.save(oldCustomer);
        model.addAttribute("customer", oldCustomer);
        model.addAttribute("customerId", oldCustomer.getCustomerId());
        model.addAttribute("newCustomer", new Customer());
        logger.info(WebMessages.SuccessMessage("Customer_" + oldCustomer.getCustomerId() +
                " changed profile!"));
        model.addAttribute("Message", WebMessages.SuccessMessage("Customer_" + oldCustomer.getCustomerId() +
                " changed profile!"));
        return "customer_account";
    }

    @PostMapping("/shopping/{storeName}")
    public String customerItemList(@AuthenticationPrincipal CustomUserDetails customerUserDetails,
                                   @PathVariable("storeName") String storeName, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerUserDetails.getUsername());
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        List<Item> items = itemRepo.findByStore(store);
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);

        if (orders.size() > 0) {
            OrderEntity currOrder = orders.get(0);
            model.addAttribute("currOrder", currOrder);
            List<Line> lineItems = currOrder.getLines();
            model.addAttribute("lineItems", lineItems);
        }

        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);
        model.addAttribute("newOrder", new OrderEntity());
        return "customer_shopping";
    }

    @PostMapping("/shopping/{storeName}/{customerId}")
    public String startOrder(@PathVariable("customerId") String customerId,
                             @PathVariable("storeName") String storeName, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        List<Item> items = itemRepo.findByStore(store);
        Drone drone = store.getDroneForOrder(0);
        if (drone == null) {
            logger.error(WebMessages.ErrorMessage("NO drone available now!"));
            model.addAttribute("Message", "NO drone is available for store_"+storeName);
        } else {
            OrderEntity order = new OrderEntity("Order_" + storeName + "_" + RandomString.make(4),
                    store, customer, store.getDroneForOrder(0));
            customerRepo.save(customer);
            orderRepo.save(order);
            logger.info(WebMessages.SuccessMessage("Order_" + order.getOrderName() + " added"));
            model.addAttribute("Message", "Order added!");
            model.addAttribute("currOrder", order);
            List<Line> lineItems = order.getLines();
            model.addAttribute("lineItems", lineItems);
        }
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);
        model.addAttribute("newOrder", new OrderEntity());
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);

        return "customer_shopping";
    }

    @PostMapping("/shopping/alterOrder/{storeName}/{customerId}")
    public String alterOrder(@PathVariable("customerId") String customerId,
                             @PathVariable("storeName") String storeName,
                             @RequestParam("orderName") String orderName, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        List<Item> items = itemRepo.findByStore(store);
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);
        OrderEntity currOrder = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        List<Line> lineItems = currOrder.getLines();
        model.addAttribute("lineItems", lineItems);
        model.addAttribute("currOrder", currOrder);
        model.addAttribute("newOrder", new OrderEntity());
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);

        return "customer_shopping";
    }

    @PostMapping("/shopping/requestItem/{storeName}/{customerId}/{orderName}/{itemName}")
    public String requestLineItem(@PathVariable("customerId") String customerId,
                                  @PathVariable("storeName") String storeName,
                                  @PathVariable("orderName") String orderName,
                                  @PathVariable("itemName") String itemName,
                                  @RequestParam("itemQuantity") int itemQuantity, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        OrderEntity order = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        Item item = itemRepo.findByStoreAndName(store, itemName);
        List<Item> items = itemRepo.findByStore(store);
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);
        Line lineItem = new Line(item, order, itemQuantity, item.getUnitePrice());
        String result = customer.requestItemWeb(order, lineItem, order.getDrone());
        if (result.equals("Line Item added successfully!")) {
            orderRepo.save(order);
            logger.info(WebMessages.SuccessMessage("Item_" + item.getName() + "_quantity:" +
                    itemQuantity + "is added to the order_" + orderName));
        } else {
            logger.error("Item_" + itemName + " failed  to add to the order_" + orderName);
        }

        model.addAttribute("Message", result);
        OrderEntity currOrder = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        List<Line> lineItems = currOrder.getLines();
        model.addAttribute("lineItems", lineItems);
        model.addAttribute("currOrder", currOrder);
        model.addAttribute("newOrder", new OrderEntity());
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);

        return "customer_shopping";
    }
    @PostMapping("/shopping/deleteItem/{storeName}/{customerId}/{orderName}/{lineId}")
    public String deleteLineItem(@PathVariable("customerId") String customerId,
                                  @PathVariable("storeName") String storeName,
                                  @PathVariable("orderName") String orderName,
                                  @PathVariable("lineId") long lineId, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        OrderEntity order = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        List<Item> items = itemRepo.findByStore(store);
        Line lineItem = lineRepo.findLineById(lineId);
        lineRepo.deleteLineById(lineId);
        boolean result=order.deleteLine(lineItem);
        if (result) {
            orderRepo.save(order);
            logger.info(WebMessages.SuccessMessage("Line with Item_" + lineItem.getItemName() + " removed from order_" + orderName));
        } else {
            logger.error("Line_" + lineItem.getItemName() + " failed to be deleted from the order_" + orderName);
        }
        List<OrderEntity> orders=orderRepo.findAll();
        model.addAttribute("Message", "Requested LineItem deleted");
        OrderEntity currOrder = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        List<Line> lineItems = currOrder.getLines();
        model.addAttribute("lineItems", lineItems);
        model.addAttribute("currOrder", currOrder);
        model.addAttribute("newOrder", new OrderEntity());
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);

        return "customer_shopping";
    }

    @PostMapping("/shopping/cancelOrder/{storeName}/{customerId}")
    public String purchaseOrder(@PathVariable("customerId") String customerId,
                                @PathVariable("storeName") String storeName,
                                @RequestParam("orderName") String orderName, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        OrderEntity order = orderRepo.findOrdersByStoreAndOrderName(store, orderName);

        order.cancelOrder();
        orderRepo.delete(order);
        logger.info("Order_" + orderName + "is canceled by customer_" + customerId);


        List<Item> items = itemRepo.findByStore(store);
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);
        model.addAttribute("Message", "Order_" + orderName + "is canceled by customer_" + customerId);

        if (orders.size() > 0) {
            OrderEntity currOrder = orders.get(0);
            model.addAttribute("currOrder", currOrder);
            List<Line> lineItems = currOrder.getLines();
            model.addAttribute("lineItems", lineItems);
        }
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);
        model.addAttribute("newOrder", new OrderEntity());

        return "customer_shopping";
    }

    @PostMapping("/shopping/purchaseOrder/{storeName}/{customerId}")
    public String cancelOrder(@PathVariable("customerId") String customerId,
                              @PathVariable("storeName") String storeName,
                              @RequestParam("orderName") String orderName, Model model) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        OrderEntity order = orderRepo.findOrdersByStoreAndOrderName(store, orderName);

        String result = customer.finishPurchaseWeb(order);
        if (result.equals("Purchase Confirmed!")) {
            order.cancelOrder();
            orderRepo.delete(order);
            logger.info("Order_" + orderName + "is purchased by customer_" + customerId);
        } else {

            logger.info("Customer_" + customerId + " attempted to purchased order_" + orderName);
        }
        List<Item> items = itemRepo.findByStore(store);
        List<OrderEntity> orders = orderRepo.findOrdersByStoreAndCustomer(store, customer);
        model.addAttribute("Message", result);

        if (orders.size() > 0) {
            OrderEntity currOrder = orders.get(0);
            model.addAttribute("currOrder", currOrder);
            List<Line> lineItems = currOrder.getLines();
            model.addAttribute("lineItems", lineItems);
        }
        model.addAttribute("storeName", storeName);
        model.addAttribute("customerId", customer.getCustomerId());
        model.addAttribute("Items", items);
        model.addAttribute("Orders", orders);
        model.addAttribute("newOrder", new OrderEntity());

        return "customer_shopping";
    }

}

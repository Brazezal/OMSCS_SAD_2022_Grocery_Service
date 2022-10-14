package edu.gatech.cs6310.Entity;


import java.util.Scanner;


import edu.gatech.cs6310.Controller.AdminController;
import edu.gatech.cs6310.Service.*;
import edu.gatech.cs6310.utility.Messages;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("deliveryService")
public class DeliveryService {
    Logger logger = LogManager.getLogger(DeliveryService.class);

    @Autowired
    @Qualifier("storeService")
    private GroceryStoreService storeService;

    @Autowired
    @Qualifier("pilotService")
    private PilotService pilotService;

    @Autowired
    @Qualifier("customerService")
    private CustomerService customerService;

    @Autowired
    @Qualifier("droneService")
    private DroneService droneService;

    @Autowired
    @Qualifier("itemService")
    private ItemService itemService;

    @Autowired
    @Qualifier("orderService")
    private OrderService orderService;


    public void commandLoop() {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";

        while (true && commandLineInput.hasNext()) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);

                if (tokens[0].equals("make_store")) {
                    String stoName = tokens[1];
                    try {
                        int stoRevenue = Integer.parseInt(tokens[2]);
                        GroceryStore newStore = new GroceryStore(stoName, stoRevenue);
                        storeService.save(newStore);
                        Messages.changeCompleted();

                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_stores")) {
                    try {
                        storeService.displayStoreInfo();
                        Messages.displayCompleted();
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }

                } else if (tokens[0].equals("sell_item")) {
                    String storeName = tokens[1];
                    String itmName = tokens[2];

                    try {
                        int itemWT = Integer.parseInt(tokens[3]);
                        var store = storeService.findByStoreName(storeName);
                        if (store != null) {
                            Item newItem = new Item(itmName, itemWT, store);
                            Boolean result = itemService.save(newItem);
                            if (result) {
                                Messages.changeCompleted();
                            }
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_items")) {
                    String stoName = tokens[1];

                    try {
                        GroceryStore store = storeService.findByStoreName(stoName);
                        if (store != null) {
                            itemService.displayItemsAlphabetic(store);
                            Messages.displayCompleted();
                        }
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }

                } else if (tokens[0].equals("make_pilot")) {
                    String dpltAccount = tokens[1];
                    String dpltFirstName = tokens[2];
                    String dpltLastName = tokens[3];
                    String dpltPhoneNum = tokens[4];
                    String dpltTaxId = tokens[5];
                    String dpltLicense = tokens[6];

                    try {
                        int dpltExperience = Integer.parseInt(tokens[7]);
                        Pilot newPilot = new Pilot(dpltAccount, dpltFirstName, dpltLastName,
                                dpltPhoneNum, dpltTaxId, dpltLicense, dpltExperience);
                        Boolean result = pilotService.save(newPilot);
                        if (result) {
                            Messages.changeCompleted();
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_pilots")) {
                    try {
                        pilotService.display();
                        Messages.displayCompleted();
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }

                } else if (tokens[0].equals("make_drone")) {
                    String stoName = tokens[1];
                    try {
                        int droneId = Integer.parseInt(tokens[2]);
                        int droneCapacity = Integer.parseInt(tokens[3]);
                        int droneFuel = Integer.parseInt(tokens[4]);
                        GroceryStore store = storeService.findByStoreName(stoName);
                        Drone drone = new Drone(store, droneId, droneCapacity, droneFuel);
                        if (store != null) {
                            storeService.makeDrone(store, drone);
                            Messages.changeCompleted();
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_drones")) {
                    try {
                        String stoName = tokens[1];
                        GroceryStore store = storeService.findByStoreName(stoName);
                        if (store != null) {
                            droneService.displayDronesByStore(store);
                            Messages.displayCompleted();
                        }
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }

                } else if (tokens[0].equals("fly_drone")) {
                    String stoName = tokens[1];
                    String pilotId = tokens[3];
                    try {
                        int droneId = Integer.parseInt(tokens[2]);
                        GroceryStore store = storeService.findByStoreName(stoName);
                        if (store != null) {
                            Drone drone = droneService.findByStoreAndDroneId(store, droneId);
                            if (drone != null) {
                                Pilot pilot = pilotService.findByName(pilotId);
                                if (pilot != null) {
                                    storeService.flyDrone(drone, pilot);

                                }
                            }
                        }

                    } catch (NumberFormatException e) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("make_customer")) {
                    String ctmAccount = tokens[1];
                    String ctmFirstName = tokens[2];
                    String ctmLastName = tokens[3];
                    String ctmPhoneNum = tokens[4];
                    try {
                        int ctmRating = Integer.parseInt(tokens[5]);
                        int ctmCredit = Integer.parseInt(tokens[6]);
                        Customer customer = new Customer(ctmAccount, ctmFirstName, ctmLastName,
                                ctmPhoneNum, ctmRating, ctmCredit);
                        Boolean result = customerService.addNewCustomer(customer);
                        if (result) {
                            Messages.changeCompleted();
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_customers")) {
                    try {
                        customerService.displayCustomers();
                        Messages.displayCompleted();
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }
                } else if (tokens[0].equals("start_order")) {
                    String storeName = tokens[1];
                    String orderId = tokens[2];
                    String ctmAccount = tokens[4];
                    try {
                        int droneId = Integer.parseInt(tokens[3]);
                        GroceryStore store = storeService.findByStoreName(storeName);
                        if (store != null) {
                            if (orderService.findByStoreAndOrderName(store, orderId) == null) {
                                Drone drone = droneService.findByStoreAndDroneId(store, droneId);
                                if (drone != null) {
                                    Customer customer = customerService.findCustomerByCustomerId(ctmAccount);
                                    if (customer != null) {
                                        OrderEntity order = new OrderEntity(orderId, store, customer, drone);
                                        customerService.save(customer);
                                        orderService.save(order);
                                        Messages.changeCompleted();
                                    }
                                }
                            } else {
                                Messages.displayErrorMessage("order_identifier_already_exists");
                            }
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("display_orders")) {
                    try {
                        String stoName = tokens[1];
                        GroceryStore store = storeService.findByStoreName(stoName);
                        if (store != null) {
                            orderService.displayOrdersOfStore(store);
                            Messages.displayCompleted();
                        }
                    } catch (Exception e) {
                        Messages.exceptionMessage("Please check the comment format");
                    }

                } else if (tokens[0].equals("request_item")) {
                    String storeName = tokens[1];
                    String orderId = tokens[2];
                    String itemName = tokens[3];
                    try {
                        int quantity = Integer.parseInt(tokens[4]);
                        int unitPrice = Integer.parseInt(tokens[5]);
                        GroceryStore store = storeService.findByStoreName(storeName);
                        if (store != null) {
                            OrderEntity order = orderService.findByStoreAndOrderName(store, orderId);
                            if (order != null) {
                                Item item = itemService.findByStoreAndItemName(store, itemName);
                                if (item != null) {
                                    Customer customer = order.getCustomer();
                                    Line lineItem = new Line(item, order, quantity, unitPrice);
                                    Drone drone = order.getDrone();
                                    Boolean result = customer.requestItem(order, lineItem, drone);
                                    if (result) {
                                        orderService.save(order);
                                        Messages.changeCompleted();
                                    }
                                }
                            } else {
                                Messages.displayErrorMessage("order_identifier_does_not_exist");
                            }
                        }
                    } catch (NumberFormatException n) {
                        Messages.exceptionMessage("Please input all the numbers as the integer");
                    }

                } else if (tokens[0].equals("purchase_order")) {
                    String storeName = tokens[1];
                    String orderId = tokens[2];
                    GroceryStore store = storeService.findByStoreName(storeName);
                    if (store != null) {
                        OrderEntity order = orderService.findByStoreAndOrderName(store, orderId);
                        if (order != null) {
                            Customer customer = order.getCustomer();
                            Boolean result = customer.finishPurchase(order);
                            if (result) {
                                order.cancelOrder();
                                orderService.delete(order);
                                Messages.changeCompleted();
                            }
                        } else {
                            Messages.displayErrorMessage("order_identifier_does_not_exist");
                        }
                    }
                } else if (tokens[0].equals("cancel_order")) {
                    String storeName = tokens[1];
                    String orderId = tokens[2];
                    GroceryStore store = storeService.findByStoreName(storeName);
                    if (store != null) {
                        OrderEntity order = orderService.findByStoreAndOrderName(store, orderId);
                        if (order != null) {
                            order.cancelOrder();
                            orderService.delete(order);
                            Messages.changeCompleted();
                        } else {
                            Messages.displayErrorMessage("order_identifier_does_not_exist");
                        }
                    }

                } else if (tokens[0].equals("stop")) {
                    System.out.println("stop acknowledged");
                    break;

                } else if (tokens[0].startsWith("//")) {
//                    System.out.println(tokens[0]);
                } else {
                    System.out.println("command " + tokens[0] + " NOT acknowledged");
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println();
            }
        }
        System.out.println("simulation terminated");
        commandLineInput.close();
    }
}

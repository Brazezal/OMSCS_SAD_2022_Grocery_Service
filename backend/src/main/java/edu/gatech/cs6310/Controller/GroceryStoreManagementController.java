package edu.gatech.cs6310.Controller;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/storeManagement")
public class GroceryStoreManagementController {

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

    @PostMapping("/addStore")
    public String addStore(GroceryStore store, Model model) {
        if (storeRepo.findStoreByStoreName(store.getStoreName()) == null) {
            storeRepo.save(store);
            logger.info(WebMessages.
                    SuccessMessage("Store_" + store.getStoreName() + " added!"));
            model.addAttribute("Message", WebMessages.SuccessMessage("Store_" + store.getStoreName() + " added!"));
        } else {
            logger.info(WebMessages.ErrorMessage("Store_" + store.getStoreName() + " exists!"));
            model.addAttribute("Message", WebMessages.ErrorMessage("Store_" + store.getStoreName() + " exists!"));
        }
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("newStore", new GroceryStore());
        model.addAttribute("stores", stores);
        return "admin_store_manage";
    }

    @PostMapping("editStoreName/{name}")
    public String editStoreName(@PathVariable("name") String storeName,
                                @RequestParam("newName") String newName,
                                Model model) {
        if (storeRepo.findStoreByStoreName(storeName) != null) {
            GroceryStore oldStore = storeRepo.findStoreByStoreName(storeName);
            oldStore.setStoreName(newName);
            storeRepo.save(oldStore);
            logger.info(WebMessages.
                    SuccessMessage("Store_" + storeName +
                            "name changed to: " + newName));
            model.addAttribute("Message", WebMessages.SuccessMessage(
                    "Store_" + storeName) + " edited!");
        }
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("newStore", new GroceryStore());
        model.addAttribute("stores", stores);
        return "admin_store_manage";
    }

    @PostMapping("editStoreRevenue/{name}")
    public String editStoreRevenue(@PathVariable("name") String storeName,
                                   @RequestParam("newRevenue") int newRevenue,
                                   Model model) {
        if (storeRepo.findStoreByStoreName(storeName) != null) {
            GroceryStore oldStore = storeRepo.findStoreByStoreName(storeName);
            oldStore.setRevenue(newRevenue);
            storeRepo.save(oldStore);
            logger.info(WebMessages.
                    SuccessMessage("Store_" + storeName +
                            "revenue changed to: " + newRevenue));
            model.addAttribute("Message",
                    WebMessages.SuccessMessage("Store_" + storeName + " edited!"));
        }
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("newStore", new GroceryStore());
        model.addAttribute("stores", stores);
        return "admin_store_manage";
    }

    @PostMapping("/deleteStore/{storeName}")
    public String deleteStore(@PathVariable String storeName, Model model) {
        if (storeRepo.findStoreByStoreName(storeName) != null) {
            GroceryStore store = storeRepo.findStoreByStoreName(storeName);
            storeRepo.delete(store);
            logger.info(WebMessages.SuccessMessage("Store deleted!"));
        }
        List<GroceryStore> stores = storeRepo.findAll();
        model.addAttribute("newStore", new GroceryStore());
        model.addAttribute("stores", stores);
        return "admin_store_manage";
    }

    @PostMapping("/viewDroneList/{storeName}")
    public String viewDroneList(@PathVariable String storeName, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        List<Drone> drones = droneRepo.findDronesByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("drones", drones);
        model.addAttribute("newDrone", new Drone());
        return "admin_store_droneList";
    }

    @PostMapping("/addDrone/{storeName}")
    public String addDroneToStore(Drone drone, @PathVariable("storeName") String storeN, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        if (droneRepo.findDroneByStoreAndDroneId(store, drone.getDroneIdentifier()) == null) {

            drone.updateRemainWT();
            store.getDrones().add(drone);
            drone.setStore(store);
            storeRepo.save(store);
            logger.info(WebMessages.SuccessMessage("Drone added!"));
            model.addAttribute("Message", WebMessages.SuccessMessage("Drone added!"));
        } else {
            logger.info(WebMessages.ErrorMessage("Drone id exist!"));
            model.addAttribute("Message", WebMessages.ErrorMessage("Drone id exist!"));
        }

        List<Drone> drones = droneRepo.findDronesByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("drones", drones);
        model.addAttribute("newDrone", new Drone());
        return "admin_store_droneList";
    }

    @PostMapping("/deleteDrone/{storeName}/{droneId}")
    public String deleteItem(@PathVariable("droneId") long droneId,
                             @PathVariable("storeName") String storeN, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Drone drone = droneRepo.findDroneByStoreAndId(store, droneId);
        store.deleteDrone(drone);
        droneRepo.delete(drone);
        storeRepo.save(store);
        logger.info(WebMessages.SuccessMessage("drone_" + drone.getDroneIdentifier() + " deleted!"));
        GroceryStore currStore = storeRepo.findStoreByStoreName(storeN);
        List<Drone> drones = droneRepo.findDronesByStore(currStore);
        model.addAttribute("currentStore", currStore);
        model.addAttribute("drones", drones);
        model.addAttribute("newDrone", new Drone());
        return "admin_store_droneList";
    }
    @PostMapping("/addFuelDrone/{storeName}/{droneId}")
    public String addfuel(@PathVariable("droneId") long droneId,
                             @PathVariable("storeName") String storeN, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Drone drone = droneRepo.findDroneByStoreAndId(store, droneId);
        drone.setRemainingTrips(3); //default drone maintenance
        droneRepo.save(drone);
        logger.info(WebMessages.SuccessMessage("drone_" + drone.getDroneIdentifier() + " got fuel!"));
        GroceryStore currStore = storeRepo.findStoreByStoreName(storeN);
        List<Drone> drones = droneRepo.findDronesByStore(currStore);
        model.addAttribute("currentStore", currStore);
        model.addAttribute("drones", drones);
        model.addAttribute("newDrone", new Drone());
        return "admin_store_droneList";
    }

    @PostMapping("/viewItemList/{storeName}")
    public String viewItemList(@PathVariable String storeName, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }

    @PostMapping("/addItem/{storeName}")
    public String addItemToStore(Item item, @PathVariable("storeName") String storeN, Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        if (itemRepo.findByStoreAndName(store, item.getName()) != null) {
            logger.error(WebMessages.ErrorMessage("Item exists!"));
            model.addAttribute("Message", WebMessages.ErrorMessage("Item exists!"));

        }else {

        item.setStore(store);
        itemRepo.save(item);
        logger.error(WebMessages.SuccessMessage("Item_" + item.getName() + " added!"));
        model.addAttribute("Message", WebMessages.SuccessMessage("Item_" + item.getName() + " added!"));
        }

        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }
    @PostMapping("/editItemName/{storeName}/{itemName}")
    public String editItemName(@PathVariable("itemName") String itemName,
                             @PathVariable("storeName") String storeN,
                               @RequestParam("newName") String newName,Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Item item = itemRepo.findByStoreAndName(store, itemName);
        item.setName(newName);
        itemRepo.save(item);

        logger.info(WebMessages.SuccessMessage("Item_"+item.getName()+" changed to_"
        +newName));

        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }

    @PostMapping("/editItemPrice/{storeName}/{itemName}")
    public String editItemPrice(@PathVariable("itemName") String itemName,
                             @PathVariable("storeName") String storeN,
                             @RequestParam("newPrice") int newPrice,
                             Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Item item = itemRepo.findByStoreAndName(store, itemName);
        item.setUnitePrice(newPrice);
        itemRepo.save(item);
        logger.info(WebMessages.SuccessMessage("Item_"+itemName+"changed price to: "+newPrice));

        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }
    @PostMapping("/editItemWeight/{storeName}/{itemName}")
    public String editItemName(@PathVariable("itemName") String itemName,
                               @PathVariable("storeName") String storeN,
                               @RequestParam("newWeight") int newWeight,Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Item item = itemRepo.findByStoreAndName(store, itemName);
        item.setWeight(newWeight);
        itemRepo.save(item);

        logger.info(WebMessages.SuccessMessage("Item_"+item.getName()+" changed weight to: "
                +newWeight));

        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }
    @PostMapping("/deleteItem/{storeName}/{itemName}")
    public String deleteItem(@PathVariable("itemName") String itemName,
                               @PathVariable("storeName") String storeN,
                             Model model) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeN);
        Item item = itemRepo.findByStoreAndName(store, itemName);

        store.deleteItem(item);
        storeRepo.save(store);
        itemRepo.delete(item);

        logger.info(WebMessages.SuccessMessage("Item_"+item.getName()+" deleted from store_"
                +storeN));

        List<Item> items = itemRepo.findByStore(store);
        model.addAttribute("currentStore", store);
        model.addAttribute("items", items);
        model.addAttribute("newItem", new Item());
        return "admin_store_itemList";
    }


}
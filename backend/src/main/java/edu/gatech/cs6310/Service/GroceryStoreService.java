package edu.gatech.cs6310.Service;



import javax.transaction.Transactional;

import edu.gatech.cs6310.Entity.Drone;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Pilot;
import edu.gatech.cs6310.Repo.DroneRepo;
import edu.gatech.cs6310.Repo.ItemRepo;
import edu.gatech.cs6310.Repo.PilotRepo;
import edu.gatech.cs6310.Repo.StoreRepo;
import edu.gatech.cs6310.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("storeService")
@Configurable
public class GroceryStoreService {

    @Autowired
    @Qualifier("storeRepo")
    private StoreRepo storeRepo;

    @Autowired
    @Qualifier("droneRepo")
    private DroneRepo droneRepo;

    @Autowired
    @Qualifier("itemRepo")
    private ItemRepo itemRepo;

    @Autowired
    @Qualifier("pilotRepo")
    private PilotRepo pilotRepo;

    public void save(GroceryStore store) {
        if (storeRepo.findStoreByStoreName(store.getStoreName()) != null) {
            Messages.displayErrorMessage("store_identifier_already_exists");
            return;
        }
        storeRepo.save(store);
    }

    public void update(GroceryStore store) {
        storeRepo.save(store);
    }

    public List<GroceryStore> findAll() {
        List<GroceryStore> stores = storeRepo.findAll();
        return stores;
    }

    public List<Drone> getDronesByStoreName(String storeName) {
        return storeRepo.findDronesByStoreName(storeName);
    }

    public void displayStoreInfo() {
        List<GroceryStore> stores= findAll();
        stores.forEach(GroceryStore::displayStoreInfo);
    }

    @Transactional
    public GroceryStore findByStoreName(String storeName) {
        GroceryStore store = storeRepo.findStoreByStoreName(storeName);
        if (store != null) {
            store.getDrones().size();
            store.getOrders().size();
            store.getItems().size();
            return store;
        }
        Messages.displayErrorMessage("store_identifier_does_not_exist");
        return null;
    }
    public boolean makeDrone(GroceryStore store,Drone drone) {
        if (drone != null) {
            if(droneRepo.findDroneByStoreAndDroneId(store,drone.getDroneIdentifier())!=null){
                Messages.displayErrorMessage("drone_identifier_already_exists");
                    return false;
            }
            store.getDrones().add(drone);
            storeRepo.save(store);
            return true;
        }
        return false;
    }
    public void flyDrone(Drone drone, Pilot pilot){
        //if drone is assigned pilot,remove drone from old pilot first
        if (drone.getPilot() != null) {
            Pilot oldPilot = drone.getPilot();
            oldPilot.setDrone(null);
            drone.setPilot(pilot);
            pilotRepo.save(oldPilot);
        }
            //If not, remove assigned drone from pilot
            Drone oldDrone=pilot.getDrone();
            if (oldDrone!=null){
                oldDrone.setPilot(null);
            }
            pilot.setDrone(drone);
            pilotRepo.save(pilot);
            Messages.changeCompleted();


    }



//    public Item findItemByStoreNameAndItemName(String storeName, String itemName) {
//        return storeRepo.findItemByStoreName_AndItemsName(storeName, itemName);
//    }

}

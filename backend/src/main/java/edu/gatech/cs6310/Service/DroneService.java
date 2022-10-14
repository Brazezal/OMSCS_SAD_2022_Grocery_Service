package edu.gatech.cs6310.Service;


import javax.transaction.Transactional;

import edu.gatech.cs6310.Entity.Drone;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Repo.DroneRepo;
import edu.gatech.cs6310.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("droneService")
public class DroneService {

    @Autowired
    @Qualifier("droneRepo")
    private DroneRepo droneRepo;


    @Transactional
    public Drone findByStoreAndDroneId(GroceryStore store, int droneId) {
        Drone drone = droneRepo.findDroneByStoreAndDroneId(store, droneId);

        if (drone != null) {
            if (drone.getPilot() != null) {
                drone.getPilot().getTaxID();
                drone.getOrderEntities().size();
            }
            return drone;
        }
        Messages.displayErrorMessage("drone_identifier_does_not_exist");
        return null;
    }

    @Transactional
    public void displayDronesByStore(GroceryStore store) {
        List<Drone> drones = droneRepo.findDronesByStore(store);
        drones.forEach(Drone::display);
    }
}

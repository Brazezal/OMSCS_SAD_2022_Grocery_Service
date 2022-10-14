package edu.gatech.cs6310.Repo;


import java.util.List;

import edu.gatech.cs6310.Entity.Drone;
import edu.gatech.cs6310.Entity.GroceryStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("droneRepo")
public interface DroneRepo extends JpaRepository<Drone, Long> {

    @Query("select drone from Drone drone where drone.store=:store and drone.droneIdentifier=:droneId")
    Drone findDroneByStoreAndDroneId(GroceryStore store, int droneId);

    Drone findDroneByStoreAndId(GroceryStore store, long id);
    List<Drone> findDronesByStore(GroceryStore store);

}

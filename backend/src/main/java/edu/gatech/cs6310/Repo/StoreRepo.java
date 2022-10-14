package edu.gatech.cs6310.Repo;


import java.util.List;

import edu.gatech.cs6310.Entity.Drone;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("storeRepo")
public interface StoreRepo extends JpaRepository<GroceryStore, Long> {

    GroceryStore findStoreByStoreName(String storeName);

    @Query(value="SELECT drone.* FROM drone drone JOIN grocery_store store ON store.id = drone.store_id "
        + "WHERE store.store_name = :storeName", nativeQuery = true)
    List<Drone> findDronesByStoreName(String storeName);

    Item findItemByStoreName_AndItemsName(String storeName, String itemName);
}

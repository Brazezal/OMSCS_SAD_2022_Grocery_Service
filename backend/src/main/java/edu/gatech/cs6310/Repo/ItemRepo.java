package edu.gatech.cs6310.Repo;


import java.util.List;

import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.Item;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("itemRepo")
public interface ItemRepo extends JpaRepository<Item, Long> {


    Item findByStoreAndName(GroceryStore store, String name);

    List<Item> findByStore(GroceryStore store);


}

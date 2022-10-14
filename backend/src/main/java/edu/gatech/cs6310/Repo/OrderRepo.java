package edu.gatech.cs6310.Repo;

import java.util.List;


import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.OrderEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("orderRepo")
public interface OrderRepo extends JpaRepository<OrderEntity, Long> {


    List<OrderEntity> findOrdersByStore(GroceryStore store);
    List<OrderEntity> findOrdersByStoreAndCustomer(GroceryStore store, Customer customer);


    OrderEntity findOrdersByStoreAndOrderName(GroceryStore store, String orderName);

}

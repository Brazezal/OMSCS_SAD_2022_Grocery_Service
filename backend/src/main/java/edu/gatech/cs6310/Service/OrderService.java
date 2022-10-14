package edu.gatech.cs6310.Service;


import java.util.List;
import javax.transaction.Transactional;

import edu.gatech.cs6310.Entity.GroceryStore;
import edu.gatech.cs6310.Entity.OrderEntity;
import edu.gatech.cs6310.Repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("orderService")
public class OrderService {

    @Autowired
    @Qualifier("orderRepo")
    private OrderRepo orderRepo;


    public void save(OrderEntity order) {
        orderRepo.save(order);
    }

    public void delete(OrderEntity order) {
        orderRepo.delete(order);
    }

    @Transactional
    public void displayOrdersOfStore(GroceryStore store) {
        List<OrderEntity> orders = orderRepo.findOrdersByStore(store);
        for (OrderEntity orderEntity : orders) {
            orderEntity.getLines().size();
        }
        orders.forEach(OrderEntity::display);
    }

    @Transactional
    public OrderEntity findByStoreAndOrderName(GroceryStore store, String orderName) {
        OrderEntity order = orderRepo.findOrdersByStoreAndOrderName(store, orderName);
        if (order != null) {
            order.getLines().size();
            order.getDrone().getOrderEntities().size();
            order.getCustomer().getOrderEntities().size();
            order.getStore().getOrders().size();
        }
        return order;
    }

}

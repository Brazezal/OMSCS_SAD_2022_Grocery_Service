package edu.gatech.cs6310.Entity;

import java.util.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class GroceryStore {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String storeName;
    private int revenue;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Drone> drones = new ArrayList<>();

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Item> items = new ArrayList<>();

    @OneToMany(mappedBy = "store",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> orders = new ArrayList<>();

    public GroceryStore() {

    }

    public GroceryStore(String storeName, int revenue) {
        this.storeName = storeName;
        this.revenue = revenue;
    }

    public GroceryStore(GroceryStore other) {
        this.id = other.id;
        this.storeName = other.storeName;
        this.revenue = other.revenue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getRevenue() {
        return revenue;
    }

    public void setRevenue(int revenue) {
        this.revenue = revenue;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public void sellItem(String itemName, int weight) {
        getItems().add(new Item(itemName, weight, this));
    }

    public void displayStoreInfo() {
        System.out.println("name:" + storeName + ",revenue:" + revenue);
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Drone getDroneForOrder(int weight){
        if (!drones.isEmpty()){
        Drone drone= Collections.max(drones, Comparator.comparingInt(Drone::getRemainingCap));
        if (drone.getRemainingCapacity()>=weight){
            return drone;
        }
        }
        return null;
    }
    public void getPayment(OrderEntity order) {
        revenue += order.getPendingCost();
    }

    public void deleteOrder(OrderEntity order) {
        orders.remove(order);
    }
    public void deleteDrone(Drone drone) {
        drones.remove(drone);
    }
    public void deleteItem(Item item) {
        items.remove(item);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroceryStore store = (GroceryStore) o;
        return Objects.equals(storeName, store.storeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeName);
    }
}

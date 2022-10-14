package edu.gatech.cs6310.Entity;


import edu.gatech.cs6310.utility.Messages;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "CUSTOMER")
public class Customer extends User {

    @Column(unique = true)
    private String customerId;
    private int rating;
    private int restCredits;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<OrderEntity> orderEntities = new ArrayList<>();

    public Customer() {
    }

    public Customer(String customerId, String firstName, String lastName, String phoneNumber,
        int rating, int credits) {
        super(firstName, lastName, phoneNumber);
        this.customerId = customerId;
        this.rating = rating;
        this.restCredits = credits;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRestCredits() {
        return restCredits;
    }

    public void setRestCredits(int restCredits) {
        this.restCredits = restCredits;
    }

    public void display() {
        System.out.println(
            "name:" + firstName + "_" + lastName + ",phone:" + phoneNumber + ",rating:" + rating
                + ",credit:" + restCredits);
    }

    public void removeOrder(OrderEntity order) {
        orderEntities.remove(order);
    }

    public int getCredit() {
        return restCredits;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

public boolean requestItem(OrderEntity order,Line lineItem,Drone drone) {
    int ctmRemainCredit = this.getCredit();
    int droneRemainCap = drone.getRemainingCapacity();
    if (droneRemainCap >=lineItem.getTotalWeight() ) {
        if (ctmRemainCredit>= order.getPendingCost()+lineItem.getTotalCost()) {
            return order.addLine(lineItem);
        } else {
            Messages.displayErrorMessage("customer_cant_afford_new_item");
        }
    } else {
        Messages.displayErrorMessage("drone_cant_carry_new_item");
    }
    return false;
}
    public String requestItemWeb(OrderEntity order,Line lineItem,Drone drone) {
        int ctmRemainCredit = this.getCredit();
        int droneRemainCap = drone.getRemainingCapacity();
        if (droneRemainCap >=lineItem.getTotalWeight() ) {
            if (ctmRemainCredit>= order.getPendingCost()+lineItem.getTotalCost()) {
                order.addLine(lineItem);
                return "Line Item added successfully!";
            } else {
                return "ERROR: customer_cant_afford_new_item";
            }
        } else {
            return "Error: drone_cant_carry_new_item";
        }
    }

    public Boolean finishPurchase(OrderEntity order){
        Drone drone=order.getDrone();
        GroceryStore store=order.getStore();
        if (drone.isAssigned()) {
            if (drone.getFuel()> 0) {
                //the number of remaining deliveries  for the drone -1
                drone.delivery(order);
                //Add Exp to pilot
                drone.getPilot().updateExp();
                //Reduce customer credit
                this.payStore(order);
                // store deliver order and get incoming
                store.getPayment(order);
                return true;
            } else {
                Messages.displayErrorMessage("drone_needs_fuel");
                return false;
            }
        } else {
            Messages.displayErrorMessage("drone_needs_pilot");
            return false;
        }
    }
    public String finishPurchaseWeb(OrderEntity order){
        Drone drone=order.getDrone();
        GroceryStore store=order.getStore();

        if (drone.isAssigned()) {
            if (drone.getFuel()> 0) {
                //the number of remaining deliveries  for the drone -1
                drone.delivery(order);
                //Add Exp to pilot
                drone.getPilot().updateExp();
                //Reduce customer credit
                this.payStore(order);
                // store deliver order and get incoming
                store.getPayment(order);
                return "Purchase Confirmed!";
            } else {
                return "Error: drone_needs_fuel";
            }
        } else {
            return "Error: drone_needs_pilot";

        }
    }
    public void deposit(int amount){
        this.restCredits+=amount;
    }

    public void payStore(OrderEntity order) {
        restCredits -= order.getPendingCost();
        orderEntities.remove(order);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(customerId, customer.customerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), customerId);
    }
    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", rating=" + rating +
                ", restCredits=" + restCredits +
                ", orderEntities=" + orderEntities +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }
}

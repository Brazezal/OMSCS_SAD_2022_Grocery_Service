package edu.gatech.cs6310.Entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private int droneIdentifier;
    private int capacity;
    private int remainingTrips;
    private int remainingCap;


    @OneToOne(mappedBy = "drone", fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private Pilot pilot;

    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderEntity> orderEntities = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private GroceryStore store;

    public Drone() {
    }

    public Drone(GroceryStore store, int droneIdentifier, int capacity, int remainingTrips) {
        this.droneIdentifier = droneIdentifier;
        this.capacity = capacity;
        this.remainingTrips = remainingTrips;
        this.remainingCap = capacity;
        this.store = store;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDroneIdentifier() {
        return droneIdentifier;
    }

    public void setDroneIdentifier(int droneIdentifier) {
        this.droneIdentifier = droneIdentifier;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getRemainingTrips() {
        return remainingTrips;
    }

    public void setRemainingTrips(int remainingTrips) {
        this.remainingTrips = remainingTrips;
    }

    public int getRemainingCap() {
        return remainingCap;
    }

    public void setRemainingCap(int remainingCap) {
        this.remainingCap = remainingCap;
    }

    public Pilot getPilot() {
        return pilot;
    }

    public void setPilot(Pilot pilot) {
        this.pilot = pilot;
    }

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }

    public GroceryStore getStore() {
        return store;
    }

    public void setStore(GroceryStore store) {
        this.store = store;
    }


    public void delivery(OrderEntity order) {
        remainingTrips--;
        deleteOrder(order);
    }

    public boolean isAssigned() {
        return pilot != null;
    }

    public int getFuel() {
        return remainingTrips;
    }

    public void deleteOrder(OrderEntity order) {
        orderEntities.remove(order);
        updateRemainWT();
    }

    public int getRemainingCapacity() {
        updateRemainWT();
        return remainingCap;
    }

    public void updateRemainWT() {
        getOrderEntities().size();
        remainingCap = capacity - getDronePendingWeight();
    }

    public int getDronePendingWeight() {
        int sumWeight = orderEntities.stream().mapToInt(OrderEntity::getPendingWeight).sum();
        return sumWeight;
    }

    public void display() {
        StringBuilder result = new StringBuilder(
            "droneId:" + droneIdentifier + ",total_cap:" + capacity + ",num_orders:" + getOrderEntities().size()
                + ",remaining_cap:" + getRemainingCapacity() + ",trips_left:" + remainingTrips);
        if (pilot != null) {
            result.append(",flown_by:" + pilot.getName());
        }
        System.out.println(result);
    }
    public String webDisplay() {
        StringBuilder result = new StringBuilder(
                "Total orders carried: " + getOrderEntities().size()
                        + ".Remaining_cap: " + getRemainingCapacity() );
        if (pilot != null) {
            result.append("Flown_by:" + pilot.getName());
        }
        return  result.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Drone drone = (Drone) o;
        return droneIdentifier == drone.droneIdentifier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(droneIdentifier);
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id=" + id +
                ", droneIdentifier=" + droneIdentifier +
                ", capacity=" + capacity +
                ", remainingTrips=" + remainingTrips +
                ", remainingCap=" + remainingCap +
                ", pilot=" + pilot +
                ", orderEntities=" + orderEntities +
                ", store=" + store +
                '}';
    }
}

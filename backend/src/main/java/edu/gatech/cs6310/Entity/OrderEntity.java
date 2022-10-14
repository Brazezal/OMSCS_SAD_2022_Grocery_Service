package edu.gatech.cs6310.Entity;


import java.util.ArrayList;
import java.util.Comparator;
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

@Entity
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String orderName;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private GroceryStore store;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Customer customer;


    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Drone drone;

    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Line> lines = new ArrayList<>();

    public OrderEntity() {
    }

    public OrderEntity(String orderName, GroceryStore store, Customer customer, Drone drone) {
        this.orderName = orderName;
        this.store = store;
        this.customer = customer;
        this.drone = drone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public GroceryStore getStore() {
        return store;
    }

    public void setStore(GroceryStore store) {
        this.store = store;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public void display() {
        System.out.println("orderName:" + orderName);
        if (!getLines().isEmpty()) {
            getLines().sort(Comparator.comparing(Line::getItemName));
            getLines().forEach(Line::display);
        }
    }

    public int getPendingWeight() {
        int sum=lines.stream().mapToInt(Line::getTotalWeight).sum();
        return sum;
    }

    public int getPendingCost() {
        int sum=lines.stream().mapToInt(Line::getTotalCost).sum();
        return sum;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void cancelOrder() {
        customer.removeOrder(this);
        drone.deleteOrder(this);
        store.deleteOrder(this);
    }

    public boolean addLine(Line line) {
        Line oldLine=null;
        for (Line allLine : lines) {
            if (allLine != null && allLine.equals(line)) {
                oldLine = allLine;
            }
        }
        if (oldLine != null) {
            //Only sum up quantity,keep unitPrice the same
            mergeLine(oldLine, line);
        } else {
            lines.add(line);
        }
        drone.updateRemainWT();
        return true;
    }
    public boolean deleteLine(Line line) {
        Boolean result=lines.remove(line);
        drone.updateRemainWT();
        return result;
    }
    public void mergeLine(Line oldLine,Line newLine){
        oldLine.setQuantity(oldLine.getQuantity() + newLine.getQuantity());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderEntity)) return false;
        OrderEntity that = (OrderEntity) o;
        return Objects.equals(orderName, that.orderName) &&
                Objects.equals(store, that.store);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderName, store);
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                ", orderName='" + orderName + '\'' +
                ", store=" + store +
                ", customer=" + customer +
                ", drone=" + drone +
                ", lines=" + lines +
                '}';
    }
}
package edu.gatech.cs6310.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int quantity;
    private int unitPrice=0;

    @OneToOne(fetch = FetchType.EAGER)
    private Item item;

    @ManyToOne(fetch = FetchType.EAGER)
    private OrderEntity orderEntity;

    public Line() {
    }

    public Line(Item item, OrderEntity orderEntity, int quantity, int unitPrice) {
        this.item = item;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderEntity = orderEntity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getUnitPrice() {
        return this.unitPrice;
    }

    public void setUnitPrice(int price) {
        this.unitPrice = price;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getTotalCost() {
        return quantity * unitPrice;
    }

    public int getTotalWeight() {
        return quantity * item.getWeight();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getItemName() {
        return item.getName();
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public void display() {
        if (item != null) {
            System.out.println(
                    "item_name:" + item.getName() + ",total_quantity:" + quantity + ",total_cost:"
                            + getTotalCost() + ",total_weight:" + getTotalWeight());
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Line)) return false;
        Line line = (Line) o;
        return Objects.equals(item, line.item) &&
                Objects.equals(orderEntity, line.orderEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    @Override
    public String toString() {
        return "Line{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", item=" + item +
                ", orderEntity=" + orderEntity +
                '}';
    }
}

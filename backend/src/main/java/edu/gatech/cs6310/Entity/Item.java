package edu.gatech.cs6310.Entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
public class Item {

    @ManyToOne(fetch = FetchType.EAGER)
    GroceryStore store;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private int weight;
    private int unitePrice;

    public int getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(int unitePrice) {
        this.unitePrice = unitePrice;
    }

    public Item() {
    }

    public Item(String name, int weight, GroceryStore store) {
        this.name = name;
        this.weight = weight;
        this.store = store;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public GroceryStore getStore() {
        return store;
    }

    public void setStore(GroceryStore store) {
        this.store = store;
    }

    public void display() {
        System.out.println(getName() + "," + getWeight());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;
        Item item = (Item) o;
        return Objects.equals(store, item.store) &&
                Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Item{" +
                "store=" + store +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", weight=" + weight +
                '}';
    }
}


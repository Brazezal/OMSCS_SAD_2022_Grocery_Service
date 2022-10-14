package edu.gatech.cs6310.Entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorValue(value = "PILOT")
public class Pilot extends User {


    @Column(unique = true)
    private String accountName;

    @Column
    private String taxID;

    @Column(unique = true)
    private String licenseID;

    @Column
    private int experience;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Drone drone;

    public Pilot() {
    }

    public Pilot(String accountName, String firstName, String lastName, String phoneNumber,
        String taxID, String licenseID, int experience) {
        super(firstName, lastName, phoneNumber);
        this.taxID = taxID;
        this.accountName = accountName;
        this.licenseID = licenseID;
        this.experience = experience;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getLicenseID() {
        return licenseID;
    }

    public void setLicenseID(String licenseID) {
        this.licenseID = licenseID;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public boolean isFlyingDrone() {
        return (drone != null);
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public void deleteDrone() {
        this.drone = null;
    }

    public void updateExp() {
        this.experience+=1;
    }

    public String getTaxID() {
        return taxID;
    }

    public void setTaxID(String taxID) {
        this.taxID = taxID;
    }

    public void display() {
        System.out.println("name:" + firstName + "_" + lastName + ",phone:" + phoneNumber
            + ",taxID:" + getTaxID() + ",licenseID:" + licenseID + ",experience:" + experience);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pilot)) return false;
        if (!super.equals(o)) return false;
        Pilot pilot = (Pilot) o;
        return Objects.equals(accountName, pilot.accountName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), accountName);
    }
    @Override
    public String toString() {
        return "Pilot{" +
                ", taxID='" + taxID + '\'' +
                ", licenseID='" + licenseID + '\'' +
                ", experience=" + experience +
                ", drone=" + drone +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                '}';
    }

}

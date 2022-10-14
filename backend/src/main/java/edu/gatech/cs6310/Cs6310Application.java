package edu.gatech.cs6310;

import edu.gatech.cs6310.Entity.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Cs6310Application implements CommandLineRunner {

    @Autowired
    DeliveryService simulator = new DeliveryService();

    public static void main(String[] args) {
        SpringApplication.run(Cs6310Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Welcome to the Grocery Express Delivery Service!");
        simulator.commandLoop();
    }
}

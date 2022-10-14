package edu.gatech.cs6310.Service;


import java.util.List;
import javax.transaction.Transactional;

import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Repo.CustomerRepo;
import edu.gatech.cs6310.Repo.OrderRepo;
import edu.gatech.cs6310.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService {

    @Autowired
    @Qualifier("customerRepo")
    private CustomerRepo customerRepo;

    @Autowired
    @Qualifier("orderRepo")
    private OrderRepo orderRepo;

    public void save(Customer customer) {
        customerRepo.save(customer);
    }

    @Transactional
    public boolean addNewCustomer(Customer customer) {
        if (customerRepo.findCustomerByCustomerId(customer.getCustomerId()) != null) {
            Messages.displayErrorMessage("customer_identifier_already_exists");
            return false;
        }
        customerRepo.save(customer);
        return true;
    }

    public List<Customer> findAll() {
        List<Customer> customers=customerRepo.findAll();
        return customers;
    }

    @Transactional
    public Customer findCustomerByCustomerId(String customerId) {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        if (customer != null) {
        customer.getOrderEntities().size();
            }
        else {
            Messages.displayErrorMessage("customer_identifier_does_not_exist");
        }
        return customer;
    }

    public void displayCustomers() {
        List<Customer> customers = findAll();
        customers.forEach(Customer::display);
    }
}

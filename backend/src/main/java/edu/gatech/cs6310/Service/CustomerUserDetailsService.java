package edu.gatech.cs6310.Service;

import edu.gatech.cs6310.Entity.CustomUserDetails;
import edu.gatech.cs6310.Entity.Customer;
import edu.gatech.cs6310.Repo.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomerUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Customer customer = customerRepo.findCustomerByCustomerId(customerId);
        if (customer == null) {
            throw new UsernameNotFoundException("Customer not found!");
        }
        return new CustomUserDetails(customer);
    }
}

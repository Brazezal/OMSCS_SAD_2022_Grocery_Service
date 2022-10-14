package edu.gatech.cs6310.Repo;




import edu.gatech.cs6310.Entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("customerRepo")
public interface CustomerRepo extends JpaRepository<Customer, Long> {

    Customer findCustomerByCustomerId(String customerId);


}

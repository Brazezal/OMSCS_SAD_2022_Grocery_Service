package edu.gatech.cs6310.Repo;


import edu.gatech.cs6310.Entity.Pilot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("pilotRepo")
public interface PilotRepo extends JpaRepository<Pilot, Long> {

    Pilot findPilotByAccountName(String pilotName);

    Pilot findPilotByLicenseID(String licenseID);



}

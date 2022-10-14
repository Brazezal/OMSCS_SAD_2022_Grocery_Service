package edu.gatech.cs6310.Service;

import edu.gatech.cs6310.Entity.Pilot;
import edu.gatech.cs6310.Repo.PilotRepo;
import edu.gatech.cs6310.utility.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pilotService")
public class PilotService {

    @Autowired
    @Qualifier("pilotRepo")
    private PilotRepo pilotRepo;

    public boolean save(Pilot pilot) {

        if (pilotRepo.findPilotByAccountName(pilot.getAccountName()) != null) {
            Messages.displayErrorMessage("pilot_identifier_already_exists");
            return false;
        } else if (pilotRepo.findPilotByLicenseID(pilot.getLicenseID()) != null) {
            Messages.displayErrorMessage("pilot_license_already_exists");
            return false;
        }
        pilotRepo.save(pilot);
        return true;
    }

    public List<Pilot> findAll() {
        List<Pilot> pilots = pilotRepo.findAll(Sort.by("accountName"));
        return pilots;
    }

    public Pilot findByName(String pilotName) {
        Pilot pilot = pilotRepo.findPilotByAccountName(pilotName);
        if (pilot == null) {
            Messages.displayErrorMessage("pilot_identifier_does_not_exist");
        }
        return pilot;
    }

    public void display() {
        List<Pilot> pilots = findAll();
        pilots.forEach(Pilot::display);
    }

}

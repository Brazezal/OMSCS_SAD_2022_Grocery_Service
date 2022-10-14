package edu.gatech.cs6310.Repo;

import edu.gatech.cs6310.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("roleRepo")
public interface RoleRepo extends JpaRepository<Role,Integer> {

    Role findRoleByName(String roleName);
}

package com.FK.Notes.Secrets.Repo;

import com.FK.Notes.Secrets.Models.AppRole;
import com.FK.Notes.Secrets.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(AppRole appRole);
}

package com.dissertation.security.repository;

import com.dissertation.security.model.Role;
import com.dissertation.security.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByName(RoleName roleName);
}

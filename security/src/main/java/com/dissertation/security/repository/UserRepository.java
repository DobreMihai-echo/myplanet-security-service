package com.dissertation.security.repository;

import com.dissertation.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);
}

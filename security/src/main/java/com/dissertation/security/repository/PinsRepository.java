package com.dissertation.security.repository;

import com.dissertation.security.model.Pins;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinsRepository extends JpaRepository<Pins,Long> {
}

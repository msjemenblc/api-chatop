package com.apichatop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apichatop.model.Rental;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {

}

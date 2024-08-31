package com.apichatop.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apichatop.dto.responses.RentalDTO;
import com.apichatop.model.Rental;
import com.apichatop.repository.RentalRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public Optional<RentalDTO> getRental(Long id) {
        Optional<Rental> rental = rentalRepository.findById(id);
        return rental.map(this::convertToDTO);
    }

    public Rental saveRental(Rental rental) {
        Rental savedRental = rentalRepository.save(rental);
        return savedRental;
    }

    private RentalDTO convertToDTO(Rental rental) {
        return new RentalDTO(
            rental.getId(),
            rental.getName(),
            rental.getSurface(),
            rental.getPrice(),
            rental.getPicture(),
            rental.getDescription(),
            rental.getCreatedAt(),
            rental.getUpdatedAt(),
            rental.getOwner().getId()
        );
    }

}

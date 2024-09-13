package com.apichatop.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.apichatop.dto.responses.RentalDTO;
import com.apichatop.model.Rental;
import com.apichatop.model.User;
import com.apichatop.repository.RentalRepository;
import com.apichatop.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private UserRepository userRepository;

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

    public Optional<Rental> getRentalEntity(Long id) {
        return rentalRepository.findById(id);
    }

    public Rental createRental(Rental rental) {
        return rentalRepository.save(rental);
    }

    public RentalDTO updateRental(Long id, RentalDTO rentalDTO) {
        Optional<Rental> rentalOptional = rentalRepository.findById(id);

        if (!rentalOptional.isPresent()) {
            throw new RuntimeException("Rental not found with id: " + id);
        }

        Rental rental = rentalOptional.get();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setPicture(rentalDTO.getPicture());
        rental.setDescription(rentalDTO.getDescription());
        rental.setUpdatedAt(LocalDateTime.now());

        Rental updatedRental = rentalRepository.save(rental);
        return convertToDTO(updatedRental);
    }

    public RentalDTO convertToDTO(Rental rental) {
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

    public Rental convertToEntity(RentalDTO rentalDTO) {
        Rental rental = new Rental();
        rental.setName(rentalDTO.getName());
        rental.setSurface(rentalDTO.getSurface());
        rental.setPrice(rentalDTO.getPrice());
        rental.setPicture(rentalDTO.getPicture());
        rental.setDescription(rentalDTO.getDescription());
        rental.setCreatedAt(rentalDTO.getCreatedAt());
        rental.setUpdatedAt(rentalDTO.getUpdatedAt());

        // Associer le propriétaire (User) à partir de l'ID
        User owner = userRepository.findById(rentalDTO.getOwnerId())
                        .orElseThrow(() -> new RuntimeException("Owner not found"));
        rental.setOwner(owner);

        return rental;
    }

}

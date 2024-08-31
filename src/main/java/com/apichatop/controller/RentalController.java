package com.apichatop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.dto.responses.RentalDTO;
import com.apichatop.model.Rental;
import com.apichatop.service.RentalService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public List<RentalDTO> getAllRentals() {
        return rentalService.getAllRentals();
    }

	@GetMapping("/{id}")
	public RentalDTO getRental(@PathVariable("id") final Long id) {
		Optional<RentalDTO> rentalDTO = rentalService.getRental(id);
        
        if (rentalDTO.isPresent()) {
            return rentalDTO.get();
        } else {
            throw new RuntimeException("Rental not found with id: " + id);
        }
	}

    @PostMapping
    public ResponseEntity<RentalDTO> createRental(@RequestBody RentalDTO rentalDTO) {
        Rental rental = rentalService.convertToEntity(rentalDTO);

        Rental newRental = rentalService.createRental(rental);

        RentalDTO newRentalDTO = rentalService.convertToDTO(newRental);

        return new ResponseEntity<>(newRentalDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public RentalDTO updateRental(@PathVariable("id") Long id, @RequestBody RentalDTO rentalDTO) {
        return rentalService.updateRental(id, rentalDTO);
    }

}

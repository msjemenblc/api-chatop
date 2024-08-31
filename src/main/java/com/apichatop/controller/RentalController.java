package com.apichatop.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.dto.responses.RentalDTO;
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

}

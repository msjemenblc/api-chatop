package com.apichatop.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.apichatop.model.Rental;
import com.apichatop.service.RentalService;

@RestController
public class RentalController {

    @Autowired
    private RentalService rentalService;

    /**
    * Read - Get one rental 
    * @param id The id of the rental
    * @return An Rental object full filled
    */
	@GetMapping("/api/rentals/{id}")
	public Rental getRental(@PathVariable("id") final Long id) {
		Optional<Rental> rental = rentalService.getRental(id);
		if(rental.isPresent()) {
			return rental.get();
		} else {
			return null;
		}
	}

    /**
    * Read - Get all rentals
    * @return - An Iterable object of Rental full filled
    */
    @GetMapping("/api/rentals")
    public Iterable<Rental> getRentals() {
        return rentalService.getRentals();
    }

    /**
    * Create - Add a new rental
    * @param rental An object rental
    * @return The rental object saved
    */
	@PostMapping("/api/rentals")
	public Rental createRental(@RequestBody Rental rental) {
		return rentalService.saveRental(rental);
	}

}

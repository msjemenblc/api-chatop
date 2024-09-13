package com.apichatop.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<RentalDTO> createRental(
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("ownerId") Long ownerId,
            @RequestParam("picture") MultipartFile picture) {

        // Sauvegarder l'image et renvoyer son chemin
        String filePath = saveImage(picture);

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);
        rentalDTO.setOwnerId(ownerId);
        rentalDTO.setPicture(filePath);

        // Convertir en entité et enregistrer dans la base
        Rental rental = rentalService.convertToEntity(rentalDTO);
        Rental newRental = rentalService.createRental(rental);

        // Convertir en DTO pour la réponse
        RentalDTO newRentalDTO = rentalService.convertToDTO(newRental);

        return new ResponseEntity<>(newRentalDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public RentalDTO updateRental(@PathVariable("id") Long id, @RequestBody RentalDTO rentalDTO) {
        return rentalService.updateRental(id, rentalDTO);
    }


    private String saveImage(MultipartFile picture) {
        try {
            // Dossier où l'image sera stockée
            String folder = "src/main/resources/assets/";
            byte[] bytes = picture.getBytes();
            Path path = Paths.get(folder + picture.getOriginalFilename());
            Files.write(path, bytes);

            // Retourner le chemin relatif de l'image
            return "assets/" + picture.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}

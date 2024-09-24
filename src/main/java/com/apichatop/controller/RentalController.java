package com.apichatop.controller;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.apichatop.dto.responses.RentalDTO;
import com.apichatop.model.Rental;
import com.apichatop.model.User;
import com.apichatop.service.RentalService;
import com.apichatop.service.UserService;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtDecoder jwtDecoder;

    private static final String ASSETS_DIR = "src/main/resources/assets/";
    private static final String BASE_URL = "http://localhost:3001/api/rentals/images/";

    @GetMapping
    public ResponseEntity<Map<String, List<RentalDTO>>> getAllRentals() {

        Map<String, List<RentalDTO>> response = new HashMap<>();
        response.put("rentals", rentalService.getAllRentals());

        return ResponseEntity.ok(response);
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
    public ResponseEntity<Map <String, String>> createRental(
            @RequestHeader("Authorization") String token,
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description,
            @RequestParam("picture") MultipartFile picture) {

        // Sauvegarder l'image et renvoyer son chemin
        String filePath = saveImage(picture);

        if (token == null || !token.startsWith("Bearer ")) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        String jwtToken = token.substring(7);

        Jwt decodedJwt = jwtDecoder.decode(jwtToken);
        String emailOrUsername = decodedJwt.getSubject();

        User user = userService.findByEmailOrUsername(emailOrUsername)
                               .orElse(null);

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);
        rentalDTO.setOwnerId(user.getId());
        rentalDTO.setPicture(filePath);

        // Convertir en entité et enregistrer dans la base
        Rental rental = rentalService.convertToEntity(rentalDTO);
        rentalService.createRental(rental);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Rental created!");

        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}", consumes = {"multipart/form-data"})
    public RentalDTO updateRental(
            @PathVariable("id") Long id, 
            @RequestParam("name") String name,
            @RequestParam("surface") BigDecimal surface,
            @RequestParam("price") BigDecimal price,
            @RequestParam("description") String description) {

        RentalDTO rentalDTO = new RentalDTO();
        rentalDTO.setName(name);
        rentalDTO.setSurface(surface);
        rentalDTO.setPrice(price);
        rentalDTO.setDescription(description);

        return rentalService.updateRental(id, rentalDTO);
    }

    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(ASSETS_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_TYPE, Files.probeContentType(filePath));
                return new ResponseEntity<>(resource, headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private String saveImage(MultipartFile picture) {
        try {
            // Dossier où l'image sera stockée
            byte[] bytes = picture.getBytes();
            Path path = Paths.get(ASSETS_DIR + picture.getOriginalFilename());
            Files.write(path, bytes);

            // Retourner le chemin relatif de l'image
            return BASE_URL + picture.getOriginalFilename();
        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }
}

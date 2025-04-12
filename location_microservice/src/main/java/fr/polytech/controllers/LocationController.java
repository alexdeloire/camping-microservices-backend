package fr.polytech.controllers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import fr.polytech.models.Asset;
import fr.polytech.models.Image;
import fr.polytech.models.Location;
import fr.polytech.repositories.AssetRepository;
import fr.polytech.repositories.ImageRepository;
import fr.polytech.repositories.LocationRepository;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/locations/")
public class LocationController {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping
    public List<Location> getAllLocations() {
        return locationRepository.findByIsAvailableTrue();
    }

    @GetMapping("/{id}")
    public Location getLocationById(@PathVariable Long id) {
        return locationRepository.findById(id).orElse(null);
    }

    @GetMapping("/user/{userId}")
    public List<Location> getLocationsByUserId(@PathVariable Integer userId) {
        System.out.println("\\n" + //
                        "userId: " + userId);
        return locationRepository.findByUserId(userId);
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(
        @RequestParam Integer userId,
        @RequestParam String name,
        @RequestParam String description,
        @RequestParam String address,
        @RequestParam Integer nbPersons,
        @RequestParam Double latitude,
        @RequestParam Double longitude,
        @RequestParam List<Integer> assetIds, 
        @RequestParam List<MultipartFile> images 
    ) {
        try {
            Location location = new Location();
            location.setUserId(userId);
            location.setName(name);
            location.setDescription(description);
            location.setAddress(address);
            location.setNbPersons(nbPersons);
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setIsAvailable(true);

            List<Long> assetIdsLong = assetIds.stream().map(Integer::longValue).collect(Collectors.toList());
            Set<Asset> assets = new HashSet<>(assetRepository.findAllById(assetIdsLong));
            if (assets.isEmpty() && !assetIds.isEmpty()) {
                return ResponseEntity.status(400).body(null); 
            }
            location.setAssets(assets);
            Location savedLocation = locationRepository.save(location);

            for (MultipartFile file : images) {
                Image image = new Image();
                image.setLocationId(savedLocation.getLocationId());
                image.setData(file.getBytes());  
                imageRepository.save(image);
            }
            savedLocation.setImages(imageRepository.findByLocationId(savedLocation.getLocationId()));

            return ResponseEntity.ok(savedLocation);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PutMapping("/{id}")
    public Location update(@PathVariable Long id, @RequestBody Location location) {
        Location existingLocation =  locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Location not found"));
        BeanUtils.copyProperties(location, existingLocation, "locationId");
        return locationRepository.saveAndFlush(existingLocation);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationRepository.deleteById(id);
    }

    // Associate assets with a location
    @PostMapping("/{id}/assets")
    public Location addAssetsToLocation(@PathVariable Long id, @RequestBody List<Long> assetIds) {
        Location location = locationRepository.findById(id).orElseThrow(() -> new RuntimeException("Location not found"));
        List<Asset> assets = assetRepository.findAllById(assetIds);
        location.getAssets().addAll(assets);
        return locationRepository.save(location);
    
    }

    private final RestTemplate restTemplate = new RestTemplate();

@GetMapping("/filtered")
public List<Location> getFilteredLocations(
        @RequestParam int nbPersons,
        @RequestParam String name,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
        HttpServletRequest request) {

    // Récupérer le token du header
    String authHeader = request.getHeader("Authorization");
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        throw new RuntimeException("Token d'authentification manquant ou invalide.");
    }
    String token = authHeader.substring(7); // Supprimer "Bearer " pour obtenir le token

    // Récupérer les locations
    List<Location> locations = locationRepository.findByNbPersonsGreaterThanEqualAndNameContainingAndIsAvailableTrue(nbPersons, name);

    if (locations.isEmpty()) {
        System.out.println("Aucune location trouvée avec les critères de nbPersons et name.");
        return locations;
    }

    String locationIds = locations.stream()
                                  .map(location -> location.getLocationId().toString())
                                  .collect(Collectors.joining(","));

    System.out.println("Locations sélectionnées avant le filtrage des réservations: " + locationIds);

    // Construire l'URL du service
    String reservationServiceUrl = String.format(
            "http://reservation-service:8082/api/v1/reservations/check?locationIds=%s&startDate=%s&endDate=%s",
            locationIds, startDate, endDate);

    // Ajouter le token dans l'en-tête de la requête
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Bearer " + token);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // Appeler le microservice des réservations avec RestTemplate
    ResponseEntity<List> response = restTemplate.exchange(
            reservationServiceUrl, 
            HttpMethod.GET, 
            entity, 
            List.class
    );

    List<Integer> rejectedLocationIds = response.getBody();

    System.out.println("IDs des locations à exclure en raison des réservations: " + rejectedLocationIds);

    // Filtrer les locations pour exclure celles avec une réservation
    List<Location> filteredLocations = locations.stream()
            .filter(location -> !rejectedLocationIds.contains(location.getLocationId()))
            .collect(Collectors.toList());

    System.out.println("IDs des locations après filtrage: " + filteredLocations.stream()
                                                                              .map(Location::getLocationId)
                                                                              .collect(Collectors.toList()));

    return filteredLocations;
}


    @GetMapping("/services")
    public List<Asset> getAllServices() {
        return assetRepository.findByTypeAsset("Service");
    }

    @GetMapping("/equipments")
    public List<Asset> getAllEquipments() {
        return assetRepository.findByTypeAsset("Equipment");
    }

    @GetMapping("/all-assets")
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @DeleteMapping("/assets/delete-admin/{id}")
    public ResponseEntity<Void> deleteAsset(@PathVariable Long id) {
        if (assetRepository.existsById(id)) {
            assetRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/assets/add-admin")
    public ResponseEntity<Asset> createAsset(@RequestBody Asset asset) {
        // Save the asset to the repository
        Asset savedAsset = assetRepository.save(asset);
        // Return the saved asset with a 201 Created response
        return ResponseEntity.status(201).body(savedAsset);
    }

    @GetMapping("/details")
    public ResponseEntity<List<Location>> getLocationsByIds(@RequestParam("locationIds") List<Long> locationIds) {
        List<Location> locations = locationRepository.findAllById(locationIds);
        return ResponseEntity.ok(locations);
    }

    @PutMapping("/{id}/delete")
    public ResponseEntity<String> deleteLocationLogically(@PathVariable Long id) {
        Location location = locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found"));

        String reservationServiceUrl = String.format(
                "http://reservation-service:8082/api/v1/reservations/location/%d/delete", id);

        try {
            restTemplate.put(reservationServiceUrl, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Location marked as deleted, but failed to notify reservation service");
        }

        location.setIsAvailable(false);
        location.setName("(Deleted)");
        location.setDescription("(Deleted)");
        location.setAddress("(Deleted)");
        location.setNbPersons(0);
        location.setLatitude(0.0);
        location.setLongitude(0.0);
        locationRepository.save(location);


        return ResponseEntity.ok("Location deleted successfully, reservations updated");
    }

    @Transactional
    @DeleteMapping("/user/{userId}/delete")
    public ResponseEntity<String> deleteLocationsByUserId(@PathVariable Integer userId) {
        try {
            // Find all locations for the given user ID
            List<Location> locations = locationRepository.findByUserId(userId);

            if (locations.isEmpty()) {
                return ResponseEntity.noContent().build(); // No locations to delete
            }

            for (Location location : locations) {
                // Perform logical deletion for each location
                String reservationServiceUrl = String.format(
                        "http://reservation-service:8082/api/v1/reservations/location/%d/delete", location.getLocationId());

                try {
                    restTemplate.put(reservationServiceUrl, null); // Notify reservation service
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Failed to notify reservation service for location ID: " + location.getLocationId());
                    // Continue processing other locations even if one fails
                }

                // Update location to mark it as logically deleted
                location.setIsAvailable(false);
                location.setName("(Deleted)");
                location.setDescription("(Deleted)");
                location.setAddress("(Deleted)");
                location.setNbPersons(0);
                location.setLatitude(0.0);
                location.setLongitude(0.0);
                locationRepository.save(location); // Persist changes
            }

            return ResponseEntity.ok("Locations deleted successfully, reservations updated");
        } catch (Exception e) {
            System.err.println("Failed to delete locations for user ID " + userId + ": " + e.getMessage());
            return ResponseEntity.status(500).body("Failed to delete locations");
        }
    }



}

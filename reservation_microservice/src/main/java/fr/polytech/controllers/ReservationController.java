package fr.polytech.controllers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.models.Reservation;
import fr.polytech.repositories.ReservationRepository;

@RestController
@RequestMapping("/api/v1/reservations/")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    // Obtenir toutes les réservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Reservation>> getReservationsByLocationIds(
        @RequestParam List<Integer> locationIds
    ) {
        if (locationIds.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        List<Reservation> reservations = reservationRepository.findByLocationIdIn(locationIds);

        if (!reservations.isEmpty()) {
            return ResponseEntity.ok(reservations);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // Récupérer toutes les réservations pour un location_id donné avec rating_client_star et rating_client_comment non nuls
    @GetMapping("/location/{locationId}/ratings")
    public List<Reservation> getReservationsWithRatingsByLocationId(@PathVariable Long locationId) {
        return reservationRepository.findByLocationIdAndRatingClientStarIsNotNullAndRatingClientCommentIsNotNull(locationId);
    }

    @GetMapping("/location/{locationId}/pastNotRefused")
    public List<Reservation> getPastReservationsByLocationIdAndStateNotRefusedOrCanceled(@PathVariable Long locationId) {
        List<String> excludedStates = Arrays.asList("REFUSED", "CANCELED");
        LocalDate today = LocalDate.now();
        return reservationRepository.findByLocationIdAndStateNotInAndEndDateGreaterThanEqual(locationId, excludedStates, today);
    }

    @GetMapping("/check")
    public List<Integer> checkReservations(
        @RequestParam("locationIds") String locationIds,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {


        List<Integer> locationIdList = Arrays.stream(locationIds.split(","))
                                            .map(Integer::parseInt)
                                            .collect(Collectors.toList());

        List<Reservation> reservations = reservationRepository.findByLocationIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(locationIdList, endDate, startDate);
        System.out.println("Reservations found: " + reservations.stream()
        .map(Reservation::getLocationId)
                        .distinct()
        .collect(Collectors.toList()));

        return reservations.stream()
                        .map(Reservation::getLocationId)
                        .distinct()
                        .collect(Collectors.toList());
    }


    // Obtenir une réservation par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationRepository.findById(id);
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUserId(@PathVariable Integer userId) {
        List<Reservation> reservations = reservationRepository.findByUserId(userId);
        if (!reservations.isEmpty()) {
            return ResponseEntity.ok(reservations);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    // Créer une nouvelle réservation
    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        reservation.setReservationDate(LocalDate.now());  // Set today's date for reservation_date
        reservation.setState("PENDING");
        return reservationRepository.save(reservation);
    }

    // Mettre à jour une réservation existante
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservationDetails) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (reservationDetails.getRatingClientStar() != null) {
            reservation.setRatingClientStar(reservationDetails.getRatingClientStar());
        }
        if (reservationDetails.getRatingClientComment() != null) {
            reservation.setRatingClientComment(reservationDetails.getRatingClientComment());
        }
        if (reservationDetails.getRatingHostStar() != null) {
            reservation.setRatingHostStar(reservationDetails.getRatingHostStar());
        }
        if (reservationDetails.getRatingHostComment() != null) {
            reservation.setRatingHostComment(reservationDetails.getRatingHostComment());
        }
        if (reservationDetails.getState() != null) {
            reservation.setState(reservationDetails.getState());
        }
        if (reservationDetails.getNbPersons() != null) {
            reservation.setNbPersons(reservationDetails.getNbPersons());
        }
        if (reservationDetails.getStartDate() != null) {
            reservation.setStartDate(reservationDetails.getStartDate());
        }
        if (reservationDetails.getEndDate() != null) {
            reservation.setEndDate(reservationDetails.getEndDate());
        }
        if (reservationDetails.getMessageRequest() != null) {
            reservation.setMessageRequest(reservationDetails.getMessageRequest());
        }
        if (reservationDetails.getLocationId() != null) {
            reservation.setLocationId(reservationDetails.getLocationId());
        }
        if (reservationDetails.getUserId() != null) {
            reservation.setUserId(reservationDetails.getUserId());
        }
        if (reservationDetails.getReservationDate() != null) {
            reservation.setReservationDate(reservationDetails.getReservationDate());
        }

        Reservation updatedReservation = reservationRepository.save(reservation);
        return ResponseEntity.ok(updatedReservation);
    }

    // Supprimer une réservation
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        if (reservationRepository.existsById(id)) {
            reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/location/{locationId}/delete")
    public ResponseEntity<String> refuseReservationsByLocation(@PathVariable Long locationId) {

        List<Reservation> reservations = reservationRepository.findByLocationId(locationId);

        if (reservations.isEmpty()) {
            return ResponseEntity.status(200).body("No reservations found for location " + locationId);
        }

        reservations.forEach(reservation -> {
            if ("PENDING".equals(reservation.getState()) || "CONFIRMED".equals(reservation.getState())) {
                reservation.setState("REFUSED");
            }
        });

        reservationRepository.saveAll(reservations);

        return ResponseEntity.ok("All associated reservations have been marked as REFUSED");
    }


}

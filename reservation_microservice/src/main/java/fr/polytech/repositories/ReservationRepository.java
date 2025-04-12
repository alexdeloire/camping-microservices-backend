package fr.polytech.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.polytech.models.Reservation;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByLocationIdAndRatingClientStarIsNotNullAndRatingClientCommentIsNotNull(Long locationId);
    
   // ReservationRepository.java
    List<Reservation> findByLocationIdInAndStartDateLessThanEqualAndEndDateGreaterThanEqual(List<Integer> locationIds, LocalDate endDate, LocalDate startDate);

    
    // List<Reservation> findByLocationIdAndStartDateAfterAndStateNot(Integer locationId, LocalDate currentDate, String state);
    List<Reservation> findByLocationIdAndStateNotInAndEndDateGreaterThanEqual(Long locationId, List<String> states, LocalDate date);


    List<Reservation> findByLocationIdAndStateNotAndEndDateBefore(Long locationId, String state, LocalDate endDate);

    List<Reservation> findByUserId(Integer userId);

    List<Reservation> findByLocationIdIn(List<Integer> locationIds);

    List<Reservation> findByLocationId(Long locationId);

    @Transactional
    int deleteByUserId(Integer userId);
}

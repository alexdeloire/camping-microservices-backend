package fr.polytech.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.polytech.models.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {

 List<Location> findByIsAvailableTrue();

 // List<Location> findByNbPersonsGreaterThanEqualAndNameContaining(int nbPersons, String name);

 List<Location> findByNbPersonsGreaterThanEqualAndNameContainingAndIsAvailableTrue(int nbPersons, String name);

 List<Location> findByUserId(Integer userId);

 int deleteByUserId(Integer userId);
}

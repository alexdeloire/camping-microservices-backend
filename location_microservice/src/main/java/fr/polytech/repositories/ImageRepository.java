package fr.polytech.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.polytech.models.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

 Set<Image> findByLocationId(Integer locationId);
}

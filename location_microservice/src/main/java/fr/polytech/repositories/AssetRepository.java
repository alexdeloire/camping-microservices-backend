package fr.polytech.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.polytech.models.Asset;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByTypeAsset(String typeAsset);
}

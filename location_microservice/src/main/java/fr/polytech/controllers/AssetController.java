package fr.polytech.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.polytech.models.Asset;
import fr.polytech.repositories.AssetRepository;

@RestController
@RequestMapping("/api/v1/assets")
public class AssetController {

    @Autowired
    private AssetRepository assetRepository;

    @GetMapping("/services")
    public List<Asset> getAllServices() {
        return assetRepository.findByTypeAsset("Service");
    }

    @GetMapping("/equipments")
    public List<Asset> getAllEquipments() {
        return assetRepository.findByTypeAsset("Equipment");
    }
}

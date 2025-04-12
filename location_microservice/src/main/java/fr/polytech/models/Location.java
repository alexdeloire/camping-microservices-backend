package fr.polytech.models;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "location")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;

    @Column(name = "nb_persons")
    private Integer nbPersons;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "location_asset",
            joinColumns = @JoinColumn(name = "location_id"),
            inverseJoinColumns = @JoinColumn(name = "asset_id")
    )
    private Set<Asset> assets;

    @OneToMany(
        fetch = FetchType.EAGER,
        cascade = CascadeType.ALL, 
        orphanRemoval = true)
    @JoinColumn(
            name = "location_id",
            referencedColumnName = "locationId"
    )
    private Set<Image> images;

    // Constructors, getters, and setters
    public Location() {
        // Default constructor
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getNbPersons() {
        return nbPersons;
    }

    public void setNbPersons(Integer nbPersons) {
        this.nbPersons = nbPersons;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(Boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Location{"
                + "locationId=" + locationId
                + ", userId=" + userId
                + ", name='" + name + '\''
                + ", description='" + description + '\''
                + ", address='" + address + '\''
                + ", nbPersons=" + nbPersons
                + ", latitude=" + latitude
                + ", longitude=" + longitude
                + ", isAvailable=" + isAvailable
                + ", assets=" + assets
                + '}';
    }
}

package fr.polytech.models;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "reservation")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer reservationId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "reservation_date", nullable = false)
    private LocalDate reservationDate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "message_request")
    private String messageRequest;
    
    @Column(name = "nb_persons")
    private Integer nbPersons;

    @Column(name = "rating_client_star")
    private Integer ratingClientStar;

    @Column(name = "rating_client_comment")
    private String ratingClientComment;

    @Column(name = "rating_host_star")
    private Integer ratingHostStar;

    @Column(name = "rating_host_comment")
    private String ratingHostComment;

    @Column(name = "state", nullable = false)
    private String state;

    // constuctor
    public Reservation(){

    }

    // Getters and Setters
    public Integer getReservationId() {
        return reservationId;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getMessageRequest() {
        return messageRequest;
    }

    public void setMessageRequest(String messageRequest) {
        this.messageRequest = messageRequest;
    }

    public Integer getRatingClientStar() {
        return ratingClientStar;
    }

    public void setRatingClientStar(Integer ratingClientStar) {
        this.ratingClientStar = ratingClientStar;
    }

    public String getRatingClientComment() {
        return ratingClientComment;
    }

    public void setRatingClientComment(String ratingClientComment) {
        this.ratingClientComment = ratingClientComment;
    }

    public Integer getRatingHostStar() {
        return ratingHostStar;
    }

    public void setRatingHostStar(Integer ratingHostStar) {
        this.ratingHostStar = ratingHostStar;
    }

    public String getRatingHostComment() {
        return ratingHostComment;
    }

    public void setRatingHostComment(String ratingHostComment) {
        this.ratingHostComment = ratingHostComment;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getNbPersons() {
        return nbPersons;
    }

    public void setNbPersons(Integer nbPersons) {
        this.nbPersons = nbPersons;
    }
}

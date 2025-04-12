package fr.polytech.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
@Access(AccessType.FIELD)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "name", length = 30)
    private String name;

    @Column(name = "username", nullable = false, length = 30, unique = true)
    private String username;

    @Column(name = "email", nullable = false, length = 80, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Column(name = "telephone", length = 20)
    private String telephone;

    @Column(name = "requested_deletion", nullable = false)
    private Boolean requestedDeletion = false;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin = false;

    // Constructors, getters, and setters

    public User() {
        // Default constructor
    }

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public Boolean getRequestedDeletion() {
        return requestedDeletion;
    }

    public void setRequestedDeletion(Boolean requestedDeletion) {
        this.requestedDeletion = requestedDeletion;
    }

    public Boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", name='" + name + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", requestedDeletion=" + requestedDeletion +
                ", isAdmin=" + isAdmin +
                '}';
    }
}

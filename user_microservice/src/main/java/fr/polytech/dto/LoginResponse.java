package fr.polytech.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    private Long userId;
    private String username;
    private String token;

    @JsonProperty("isAdmin")
    private boolean isAdmin;

    public LoginResponse(Long userId, String username, String token, boolean isAdmin) {
        this.userId = userId;
        this.username = username;
        this.token = token;
        this.isAdmin = isAdmin;
    }

    // Getters
    public Long getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getToken() { return token; }
    public boolean isAdmin() { return isAdmin; }
}

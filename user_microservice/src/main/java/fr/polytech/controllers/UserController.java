package fr.polytech.controllers;

import fr.polytech.KafkaProducerService;
import fr.polytech.dto.LoginRequest;
import fr.polytech.dto.LoginResponse;
import fr.polytech.models.User;
import fr.polytech.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users/")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaProducerService kafkaProducerService;

    // Inject the JWT secret and expiration properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    // GET endpoint to return all users
    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

    // GET endpoint to return a specific user by ID
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found");
        }
        return user.get();
    }

    // POST endpoint to create a new user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody final User user) {
        return userRepository.saveAndFlush(user);
    }

    // Helper method to generate a JWT token
    private String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("userId", user.getUserId())
                .claim("isAdmin", user.getIsAdmin())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getBytes())
                .compact();
    }

    // POST endpoint for user registration
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@RequestBody User user) {
        // Check if username or email already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }

        // Save user
        userRepository.saveAndFlush(user);

        // Generate JWT token
        String token = generateToken(user);

        // Return LoginResponse
        return new LoginResponse(user.getUserId(), user.getUsername(), token, user.getIsAdmin());
    }

    // POST endpoint for user login
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        User user = userOptional.get();

        // Check if password matches
        if (!Objects.equals(loginRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // Generate JWT token
        String token = generateToken(user);

        kafkaProducerService.sendMessage("login-topic", "User logged in: " + user.getUsername());

        return new LoginResponse(user.getUserId(), user.getUsername(), token, user.getIsAdmin());
    }

    @PostMapping("/{id}/request-deletion")
    @Transactional
    public void requestDeletion(@PathVariable Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID " + id + " not found");
        }

        User user = userOptional.get();
        user.setRequestedDeletion(true);
        userRepository.save(user);
    }

    // DELETE endpoint to delete a user by ID
    @RequestMapping(value = "/delete-by-admin/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        System.out.println("User deleted: " + id);

        userRepository.deleteById(id);
        // Send a message to the Kafka topic
        kafkaProducerService.sendMessage("delete-reservation-topic", id.toString());
        kafkaProducerService.sendMessage("delete-location-topic", id.toString());
    }

    // PUT endpoint to update a user by ID
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userRepository.getById(id);
        BeanUtils.copyProperties(user, existingUser, "userId");
        return userRepository.saveAndFlush(existingUser);
    }
}

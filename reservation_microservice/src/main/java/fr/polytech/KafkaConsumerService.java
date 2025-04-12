package fr.polytech;

import fr.polytech.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class KafkaConsumerService {

    @Autowired
    private ReservationRepository reservationRepository;

    @KafkaListener(topics = "login-topic", groupId = "bivouacngo")
    public void listen(String message) {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nReceived message: " + message);
    }

    @KafkaListener(topics = "delete-reservation-topic", groupId = "bivouacngo")
    public void listenDeleteReservationTopic(String message) {
        System.out.println("Received message from delete-reservation-topic: " + message);

        // Parse the message to extract the user ID
        Integer userId = extractUserIdFromMessage(message);

        if (userId != null) {
            // Delete all reservations for the given user ID
            deleteReservationsByUserId(userId);
        }
    }

    @Transactional
    private void deleteReservationsByUserId(Integer userId) {
        try {
            int deletedCount = reservationRepository.deleteByUserId(userId);
            System.out.println("Deleted " + deletedCount + " reservations for user ID: " + userId);
        } catch (Exception e) {
            System.err.println("Failed to delete reservations for user ID " + userId + ": " + e.getMessage());
        }
    }

    // Utility method to extract user ID from the message
    private Integer extractUserIdFromMessage(String message) {
        try {
            return Integer.parseInt(message);
        } catch (NumberFormatException e) {
            System.err.println("Invalid user ID in message: " + message);
            return null;
        }
    }
}

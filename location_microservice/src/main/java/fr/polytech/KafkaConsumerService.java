package fr.polytech;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KafkaConsumerService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String LOCATION_SERVICE_BASE_URL = "http://localhost:8081/api/v1/locations";

    @KafkaListener(topics = "delete-location-topic", groupId = "bivouacngo")
    public void listenDeleteLocationTopic(String message) {
        System.out.println("Received message from delete-location-topic: " + message);

        Integer userId = extractUserIdFromMessage(message);
        if (userId != null) {
            // Make an HTTP request to delete locations by user ID
            deleteLocationsViaHttpRequest(userId);
        }
    }

    private void deleteLocationsViaHttpRequest(Integer userId) {
        try {
            String url = LOCATION_SERVICE_BASE_URL + "/user/" + userId + "/delete";
            restTemplate.delete(url);
            System.out.println("Successfully deleted locations for user ID: " + userId);
        } catch (Exception e) {
            System.err.println("Failed to delete locations for user ID " + userId + ": " + e.getMessage());
        }
    }

    private Integer extractUserIdFromMessage(String message) {
        try {
            return Integer.parseInt(message);
        } catch (NumberFormatException e) {
            System.err.println("Invalid user ID in message: " + message);
            return null;
        }
    }
}

-- docker exec -it reservation_db psql -U reservation_user -d reservation_db
DROP TABLE IF EXISTS reservation;

CREATE TABLE IF NOT EXISTS reservation (
    reservation_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL, 
    location_id INTEGER NOT NULL, 
    reservation_date DATE NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    message_request TEXT,
    nb_persons INTEGER,
    rating_client_star INTEGER CHECK (rating_client_star BETWEEN 1 AND 5),
    rating_client_comment TEXT,
    rating_host_star INTEGER CHECK (rating_host_star BETWEEN 1 AND 5),
    rating_host_comment TEXT,
    state VARCHAR(50) CHECK (state IN ('PENDING', 'CONFIRMED', 'COMPLETED', 'CANCELED', 'REFUSED'))
);


INSERT INTO reservation (user_id, location_id, reservation_date, start_date, end_date, message_request, nb_persons, rating_client_star, rating_client_comment, rating_host_star, rating_host_comment, state)
VALUES 
    (1, 1, '2023-01-01', '2023-01-10', '2023-01-15', 'Looking forward to my stay!', 2, NULL, NULL, NULL, NULL, 'PENDING'),
    (1, 1, '2023-01-01', '2023-01-10', '2023-01-15', 'Looking forward to my stay!', 2, NULL, NULL, NULL, NULL, 'CONFIRMED'),
    (1, 1, '2023-01-01', '2023-01-10', '2023-01-15', 'Looking forward to my stay!', 2, NULL, NULL, NULL, NULL, 'COMPLETED'),
    (1, 1, '2023-01-01', '2023-01-10', '2023-01-15', 'Looking forward to my stay!', 2, NULL, NULL, NULL, NULL, 'CANCELED'),
    (1, 1, '2023-01-01', '2023-01-10', '2023-01-15', 'Looking forward to my stay!', 2, NULL, NULL, NULL, NULL, 'REFUSED'),
    (1, 2, '2023-02-01', '2023-02-05', '2023-02-10', 'Can I have early check-in?', 1, 4, 'Lovely stay.', 5, 'Very polite guest.', 'COMPLETED'),
    (2, 1, '2023-02-01', '2023-02-05', '2023-02-10', 'Can I have early check-in?', 1, NULL, NULL, NULL, NULL, 'CONFIRMED'),
    (3, 1, '2023-03-01', '2023-03-12', '2023-03-15', 'Do you have parking?', 2, 3, 'It was okay.', 3, 'Guest was okay.', 'COMPLETED'),
    (2, 2, '2023-02-01', '2023-02-05', '2023-02-10', 'Can I have early check-in?', 2, 4, 'Lovely stay.', 5, 'Very polite guest.', 'COMPLETED'),
    (3, 3, '2023-03-01', '2023-03-12', '2023-03-15', 'Do you have parking?', 1, NULL, NULL, NULL, NULL, 'CANCELED'),
    (4, 1, '2025-04-01', '2025-04-08', '2023-04-12', 'Excited for the city!', 2, NULL, NULL, NULL, NULL, 'REFUSED');

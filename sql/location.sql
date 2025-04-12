-- docker exec -it location_db psql -U location_user -d location_db
DROP TABLE IF EXISTS image;
DROP TABLE IF EXISTS location_asset;
DROP TABLE IF EXISTS asset;
DROP TABLE IF EXISTS location;

CREATE TABLE IF NOT EXISTS location (
    location_id SERIAL PRIMARY KEY,
    user_id INTEGER NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    address VARCHAR(255),
    nb_persons INTEGER,
    latitude DOUBLE PRECISION,
    longitude DOUBLE PRECISION,
    is_available BOOLEAN DEFAULT TRUE
);

INSERT INTO location (user_id, name, description, address, nb_persons, latitude, longitude, is_available)
VALUES 
    (1, 'Cozy Cottage', 'A small, cozy cottage in the woods.', '123 Forest Lane', 4, 45.6789, -123.4567, TRUE),
    (2, 'Beach House', 'A beautiful beach house with ocean view.', '456 Beach Ave', 6, 34.0522, -118.2437, TRUE),
    (3, 'Mountain Cabin', 'A rustic cabin in the mountains.', '789 Hilltop Road', 5, 39.7392, -104.9903, FALSE),
    (4, 'City Apartment', 'A modern apartment in the city center.', '321 Downtown St', 3, 40.7128, -74.0060, TRUE);

CREATE TABLE IF NOT EXISTS asset (
    asset_id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type_asset VARCHAR(50)
);

INSERT INTO asset (name, type_asset)
VALUES 
    -- Services
    ('WiFi', 'Service'),
    ('Pool', 'Service'),
    ('Parking', 'Service'),
    ('24/7 Self Check-in', 'Service'),
    ('Room Service', 'Service'),
    ('Bike Rentals', 'Service'),
    ('Daily Housekeeping', 'Service'),
    ('Concierge', 'Service'),
    ('Airport Shuttle', 'Service'),
    ('Breakfast Included', 'Service'),
    ('Pet-Friendly', 'Service'),

    -- Equipments
    ('Air Conditioning', 'Equipment'),
    ('Kitchen', 'Equipment'),
    ('TV', 'Equipment'),
    ('Washer', 'Equipment'),
    ('Dryer', 'Equipment'),
    ('Refrigerator', 'Equipment'),
    ('Microwave', 'Equipment'),
    ('Coffee Maker', 'Equipment'),
    ('Dishwasher', 'Equipment'),
    ('Fireplace', 'Equipment'),
    ('Outdoor Grill', 'Equipment');

CREATE TABLE IF NOT EXISTS location_asset (
    location_id INTEGER NOT NULL,
    asset_id INTEGER NOT NULL,
    PRIMARY KEY (location_id, asset_id),
    FOREIGN KEY (location_id) REFERENCES location(location_id) ON DELETE CASCADE,
    FOREIGN KEY (asset_id) REFERENCES asset(asset_id) ON DELETE CASCADE
);

INSERT INTO location_asset (location_id, asset_id)
VALUES 
    (1, 1), -- Cozy Cottage with WiFi
    (1, 3), -- Cozy Cottage with Air Conditioning
    (2, 1), -- Beach House with WiFi
    (2, 2), -- Beach House with Pool
    (3, 3), -- Mountain Cabin with Air Conditioning
    (4, 4); -- City Apartment with Parking

CREATE TABLE IF NOT EXISTS image (
    image_id SERIAL PRIMARY KEY,
    location_id INTEGER, 
    description TEXT,
    "order" INTEGER,
    data BYTEA,
    FOREIGN KEY (location_id) REFERENCES location(location_id) ON DELETE CASCADE
);

INSERT INTO image (location_id, description, "order")
VALUES 
    (1, 'Front of the cottage', 1),
    (1, 'Living room', 2),
    (2, 'Beach view', 1),
    (2, 'Bedroom', 2),
    (3, 'Mountain view', 1),
    (3, 'Kitchen', 2),
    (4, 'City view', 1),
    (4, 'Bathroom', 2);
